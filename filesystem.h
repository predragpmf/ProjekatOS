#ifndef FILESYSTEM_H
#define FILESYSTEM_H

typedef struct node{
    char name[128];
    int size;
    int num_blocks;
    int *blocks;
    int is_directory;
    struct node *parent;
    struct node *first_child;
    struct node *next_sibling;
}node;

node *create_node(char *name, int size, int *blocks, int is_directory);
void add_child(node *parent, node *child);
void delete_node(node *current);

#endif // FILESYSTEM_H