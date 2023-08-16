#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "mem.h"

page *init_mem() {
    page *head = NULL;
    page *prev = NULL;
    for (int i = 0; i < NUM_PAGES; i++) {
        page *new_page = malloc(sizeof(page));
        new_page->id = i;
        new_page->free = 1;
        new_page->size = 0;
        new_page->data = calloc(PAGE_SIZE + 1, sizeof(char));
        new_page->next = NULL;
        if (i == 0) {
            head = new_page;
            prev = new_page;
        } else {
            prev->next = new_page;
            prev = new_page;
        }
    }
    return head;
}

void print_mem(page *head) {
    page *temp = head;
    int mem_size = NUM_PAGES * PAGE_SIZE;
    int byte_count = 0;
    printf("Memory block overview (1 = free):\n");
    for (int i = 0; i < NUM_PAGES; i++) {
        byte_count += temp->size;
        printf("|%d|-", temp->free);
        temp = temp->next;
    }
    printf("\nTOTAL: %dB\n", mem_size);
    printf("USED: %dB\n", byte_count);
}

page **load_data(page *head, char *data, int *ret_num_pages) {
    int data_length = strlen(data);
    int num_pages =(int) ceil((double) data_length / (double) PAGE_SIZE);
    *ret_num_pages = num_pages;
    char *subarray = calloc(PAGE_SIZE + 1, sizeof(char));
    if (subarray == NULL) {
        printf("Error allocating memory\n");
        return NULL;
    }
    char *p = data;
    page **pages = calloc(num_pages, sizeof(page *));
    if (pages == NULL) {
        printf("Error allocating memory\n");
        free(subarray);
        return NULL;
    }
    for (int i = 0; i < num_pages; i++) {
        int len = strlen(p);
        if (len > PAGE_SIZE) {
            len = PAGE_SIZE;
        }
        strncpy(subarray, p, len);
        subarray[len] = '\0';
        p += len;
        pages[i] = write_page(subarray, head);
    }
    free(subarray);
    subarray = NULL;
    return pages;
}

page *write_page(char *data, page *head) {
    page *temp = head;
    for (int i = 0; i < NUM_PAGES; i++) {
        if (temp->free) {
            strcpy(temp->data, data);
            temp->size = strlen(data);
            temp->free = 0;
            break;
        } else {
            temp = temp->next;
        }
    }
    return temp;
}
