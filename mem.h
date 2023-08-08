#ifndef MEM_H
#define MEM_H

#define PAGE_SIZE 32
#define NUM_PAGES 64

typedef struct page {
    int id;
    int free;
    int size;
    char *data;
    struct page *next;
} page;

page *init_mem();
void print_mem(page *head);
page **load_data(page *head, char *data, int *ret_num_pages);
page *write_page(char *data, page *head);

#endif // MEM_H