#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "cpu.h"

queue *process_queue = NULL;
int pids = 0;
int num_running = 0;

void *thread() {
    while (1) {
        process *current = pop_process();
        current->running = 1;
        
        char *inst = read_memory(current->IP, current);
        if (strncmp(inst, "00000001", 8) == 0) {
            // Go to next byte:
            current->IP += 1;

            // Count length of JMP label:
            int temp_ip = current->IP;
            inst = read_memory(temp_ip, current);
            int len = 0;
            while (strncmp(inst, "00000000", 8) != 0) {
                len++;
                temp_ip += 1;
                inst = read_memory(temp_ip, current);
            }

            // Allocate memory for label:
            char *label = calloc((len * 8) + 9, sizeof(char));
            char *p_label = label;

            // Go back to start of label:
            inst = read_memory(current->IP, current);

            // Copy label to new str:
            for (int i = 0; i < len; i++) {
                memcpy(p_label, inst, 8);
                p_label += 8;
                current->IP += 1;
                inst = read_memory(current->IP, current);
            }
            memcpy(p_label, "00111010\0", 9);
            //*p_label = '\0';
            
            // JMP to label:
            current->IP = jump_to(label, current);
            if (current->IP == -1) {
                printf("Invalid JMP label!");
                free(label);
                label = NULL;
                p_label = NULL;
                free_process(current);
                num_running--;
                continue;
            }
            free(label);
            label = NULL;
            p_label = NULL;
        } else if (strncmp(inst, "00000100", 8) == 0) {
            printf("Process %d finished!\n", current->id);
            free_process(current);
            num_running--;
            continue;
        } else {
            current->IP++;
        }
        sleep(1);
        current->running = 0;
        push_process(current);
    }
    return 0;
}

char *read_memory(int IP, process *current) {
    // Position relative to the whole mem:
    int char_pos = IP * 8;
    // Page index of the current instruction to be executed:
    int current_page = char_pos / PAGE_SIZE;
    // Position relative to the current page:
    int page_pos = char_pos - (current_page * PAGE_SIZE);
    // Go to position:
    char *p = current->pages[current_page]->data + page_pos;
    
    return p;
}

int jump_to(char* str, process *current) {
    char *data = calloc((current->size * 8) + 1, sizeof(char));
    char *p_data = data;
    for (int i = 0; i < current->num_pages; i++) {
        memcpy(p_data, current->pages[i]->data, 32);
        p_data += PAGE_SIZE;
    }
    *p_data = '\0';

    char *pos = strstr(data, str);
    int index = 0;
    if (pos != NULL) {
        index = (pos - data) / 8;
    } else {
        printf("Error\n");
        free(data);
        data = NULL;
        p_data = NULL;
        return -1;
    }
    free(data);
    data = NULL;
    p_data = NULL;
    return index;
}

void free_process(process *current) {
    for (int i = 0; i < current->num_pages; i++) {
        free(current->pages[i]->data);
        current->pages[i]->data = calloc(PAGE_SIZE + 1, sizeof(char));
        current->pages[i]->size = 0;
        current->pages[i]->free = 1;
    }
    free(current->pages);
    current->pages = NULL;
    free(current);
    current = NULL;
}

int start_cpu() {
    process_queue = malloc(sizeof(queue));
    process_queue->head = NULL;
    process_queue->tail = NULL;
    if (pthread_mutex_init(&process_queue->lock, NULL) != 0) {
        printf("Mutex init error\n");
        return -1;
    }
    if (pthread_cond_init(&process_queue->cond, NULL) != 0) {
        printf("Condition init error\n");
        return -1;
    }
    pthread_t thread_id;
    if (pthread_create(&thread_id, NULL, thread, NULL) == 0) {
        return 0;
    }
    return -1;
}

void push_process(process *pr) {
    queue_node *new_node = malloc(sizeof(queue_node));
    new_node->pr = pr;
    new_node->next = NULL;
    pthread_mutex_lock(&process_queue->lock);
    if (process_queue->tail == NULL) {
        process_queue->head = new_node;
        process_queue->tail = new_node;
    } else {
        process_queue->tail->next = new_node;
        process_queue->tail = new_node;
    }
    pthread_cond_signal(&process_queue->cond);
    pthread_mutex_unlock(&process_queue->lock);
}

process *pop_process() {
    pthread_mutex_lock(&process_queue->lock);
    while (process_queue->head == NULL) {
        pthread_cond_wait(&process_queue->cond, &process_queue->lock);
    }
    queue_node *node = process_queue->head;
    process *pr = node->pr;
    process_queue->head = node->next;
    if (process_queue->head == NULL) {
        process_queue->tail = NULL;
    }
    free(node);
    pthread_mutex_unlock(&process_queue->lock);
    return pr;
}

void push_stack(stack *s, int data) {
    stack_node *node = malloc(sizeof(stack_node));
    node->data = data;
    node->next = s->top;
    s->top = node;
}

int pop_stack(stack *s) {
    if (s->top == NULL) {
        return -1;
    }
    int data = s->top->data;
    stack_node *temp = s->top;
    s->top = s->top->next;
    free(temp);
    return data;
}

void free_stack(stack *s) {
    while (s->top != NULL) {
        stack_node *temp = s->top;
        s->top = s->top->next;
        free(temp);
    }
    free(s);
}