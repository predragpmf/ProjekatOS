#include <stdio.h>
#include <stdlib.h>
#include <bsd/string.h>

#include "filesystem.h"
#include "disk.h"


node *create_node(char *name, int size, int *blocks, int is_directory) {
    node *new_node = malloc(sizeof(node));
    strlcpy(new_node->name, name, sizeof(new_node->name));
    new_node->size = size;
    new_node->num_blocks = 0;
    new_node->blocks = blocks;
    new_node->is_directory = is_directory;
    new_node->first_child = NULL;
    new_node->next_sibling = NULL;
    return new_node;
}

void add_child(node *parent, node *child) {
    child->parent = parent;
    if (parent->first_child == NULL) {
        parent->first_child = child;
    } else {
        node *current = parent->first_child;
        while (current->next_sibling != NULL) {
            current = current->next_sibling;
        }
        current->next_sibling = child;
    }
}

void delete_node(node *current) {
    if (current == NULL) {
        return;
    }
    if (current->is_directory == 1) {
        node *child = current->first_child;
        while (child != NULL) {
            node *next = child->next_sibling;
            delete_node(child);
            child = next;
        }
    }
    if (current->parent->first_child == current) {
        current->parent->first_child = current->next_sibling;
    } else {
        node *prev = current->parent->first_child;
        while (prev->next_sibling != current) {
            prev = prev->next_sibling;
        }
        prev->next_sibling = current->next_sibling;
    }
    if (clear_data(current->blocks, current->num_blocks) == -1) {
        printf("Error clearing data!\n");
        printf("Blocks:\n");
        for (int i = 0; i < current->num_blocks; i++) {
            printf("%d\n", current->blocks[i]);
        }
    }
    free(current->blocks);
    current->blocks = NULL;
    free(current);
    current = NULL;
}