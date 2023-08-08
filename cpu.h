#ifndef CPU_H
#define CPU_H

#include <pthread.h>
#include <unistd.h>
#include "mem.h"

typedef struct stack_node {
    int data;
    struct stack_node *next;
} stack_node;

typedef struct stack {
    stack_node *top;
} stack;

typedef struct process {
    int id;
    int running;
    int num_pages;
    int size;
    page **pages;
    int A, B, C, D, IP, flag_z;
    stack *cpu_stack;
} process;

typedef struct queue_node {
    process *pr;
    struct queue_node *next;
} queue_node;

typedef struct queue {
    queue_node *head;
    queue_node *tail;
    pthread_mutex_t lock;
    pthread_cond_t cond;
} queue;

extern int pids;
extern int num_running;
extern queue *process_queue;

void *thread();
char *read_memory(int IP, process *current);
int jump_to(char* str, process *current);
void free_process(process *current);
int start_cpu();
void push_process(process *pr);
process *pop_process();
void push_stack(stack *s, int data);
int pop_stack(stack *s);
void free_stack(stack *s);

#endif // CPU_H