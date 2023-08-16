#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#include "filesystem.h"
#include "disk.h"
#include "asm.h"
#include "cpu.h"


int main() {

    create_disk();
    node *root = create_node("root", 0, NULL, 1);
    page *mem_head = init_mem();
    node *location = root;
    if (start_cpu() == 0) {
        printf("CPU started.\n");
    } else {
        printf("CPU error.\n");
    }
    char *path = calloc(4096, sizeof(char));
    while (1) {

        char line[4096];
        printf("%s> ", path);
        fgets(line, sizeof(line), stdin);
        if (line[strlen(line) - 1] != '\n') {
            printf("Input is too long!\n");
            while ((getchar()) != '\n');
            continue;
        }

        if (strcmp(line, "\n") == 0) {
            continue;
        }

        line[strlen(line) - 1] = '\0';

        if (strcmp(line, "exit") == 0) {
            break;
        }

        if (strncmp(line, "mkdir ", 6) == 0) {
            char *dir_name = line + 6;
            node *new_dir = create_node(dir_name, 0, NULL, 1);
            if (new_dir == NULL) {
                printf("Error creating directory!\n");
            } else {
                add_child(location, new_dir);
            }
            continue;
        }

        if (strncmp(line, "mkfile ", 7) == 0) {
            char *file_name = line + 7;
            node *new_file = create_node(file_name, 0, NULL, 0);
            if (new_file == NULL) {
                printf("Error creating file!\n");
            } else {
                add_child(location, new_file);
            }
            continue;
        }

        if (strncmp(line, "write ", 6) == 0) {
            char *file_name = line + 6;
            node *current = location->first_child;
            while (current != NULL) {
                if (strcmp(current->name, file_name) == 0) {
                    if (current->is_directory == 1) {
                        printf("Can't write to a directory!\n");
                        break;
                    }
                    printf("Enter new file content:\n");
                    int data_size = 4096;
                    char *data = calloc(data_size, sizeof(char));
                    fgets(data, data_size, stdin);
                    if (data[strlen(data) - 1] != '\n') {
                        printf("Input is too long!\n");
                        free(data);
                        data = NULL;
                        while ((getchar()) != '\n');
                        break;
                    }
                    data[strlen(data) - 1] = '\0';
                    int size = strlen(data);
                    int block_size = BLOCK_SIZE - 1;
                    int num_blocks =(int) ceil((double) size / (double) block_size);
                    int *blocks = calloc(num_blocks, sizeof(int));
                    if (blocks == NULL) {
                        printf("Error allocating memory!\n");
                        free(data);
                        data = NULL;
                        break;
                    }
                    if (current->blocks != NULL) {
                        if (clear_data(current->blocks, current->num_blocks) == -1) {
                            printf("Error clearing data!\n");
                            printf("Blocks:\n");
                            for (int i = 0; i < current->num_blocks; i++) {
                                printf("%d\n", current->blocks[i]);
                        }
                        current->blocks = NULL;
                        current->num_blocks = 0;
                        current->size = 0;
                        }
                    }
                    int *locations = write_data(data);
                    if (locations == NULL) {
                        printf("Error writing data!\n");
                        free(blocks);
                        blocks = NULL;
                        free(data);
                        data = NULL;
                        break;
                    }
                    for (int i = 0; i < num_blocks; i++) {
                        blocks[i] = locations[i];
                    }
                    current->size = size;
                    current->num_blocks = num_blocks;
                    current->blocks = blocks;
                    blocks = NULL;
                    free(locations);
                    locations = NULL;
                    free(data);
                    data = NULL;
                    break;
                }
                current = current->next_sibling;
            }
            if (current == NULL) {
                printf("File not found\n");
            }
            continue;
        }

        if (strncmp(line, "print ", 6) == 0) {
            char *file_name = line + 6;
            node *current = location->first_child;
            while (current != NULL) {
                if (strcmp(current->name, file_name) == 0) {
                    if (current->is_directory == 1) {
                        printf("Can't print directory!\n");
                    } else {
                        if (current->blocks == NULL) {
                            printf("File is empty!\n");
                            break;
                        }
                        char *data = read_data(current->blocks, current->num_blocks, current->size);
                        if (data == NULL) {
                            printf("Error reading data!\n");
                            break;
                        }
                        printf("%s\n", data);
                        free(data);
                        data = NULL;
                    }
                    break;
                }
                current = current->next_sibling;
            }
            if (current == NULL) {
                printf("File not found\n");
            }
            continue;
        }

        if (strcmp(line, "formatdisk") == 0) {
            printf("Are you sure you want to format the disk? (y/n)\n");
            char answer[2];
            fgets(answer, sizeof(answer), stdin);
            if (answer[0] == 'y') {
                location = root;
                node *current = root->first_child;
                while (current != NULL) {
                    node *next = current->next_sibling;
                    delete_node(current);
                    current = next;
                }
                format_disk();
                printf("Disk formatted!\n");
            }
            continue;
        }

        if (strcmp(line, "ls") == 0) {
            struct node *current = location->first_child;
            while (current != NULL) {
                if (current->is_directory == 1) {
                    printf("%s/\t", current->name);
                } else {
                    printf("%s\t", current->name);
                }
                current = current->next_sibling;
            }
            printf("\n");
            continue;
        }

        if (strncmp(line, "cd ", 3) == 0) {
            char *dirName = line + 3;
            if (strcmp(dirName, "..") == 0) {
                if (location->parent != NULL) {
                    location = location->parent;
                }
                char *loc = strrchr(path, '/');
                if (loc != NULL) {
                    *loc = '\0';
                }
                continue;
            }
            node *current = location->first_child;
            while (current != NULL) {
                if (strcmp(current->name, dirName) == 0) {
                    if (current->is_directory == 0) {
                        printf("Not a directory!\n");
                        break;
                    }
                    char new_path[4096];
                    sprintf(new_path, "%s/%s", path, current->name);
                    strcpy(path, new_path);
                    location = current;
                    break;
                }
                current = current->next_sibling;
            }
            if (current == NULL) {
                printf("Directory not found\n");
            }
            continue;
        }

        if (strncmp(line, "rm ", 3) == 0) {
            char *name = line + 3;
            node *current = location->first_child;
            while (current != NULL) {
                if (strcmp(current->name, name) == 0) {
                    delete_node(current);
                    break;
                }
                current = current->next_sibling;
            }
            continue;
        }

        if (strncmp(line, "readblock ", 10) == 0) {
            int num;
            if (sscanf(line + 10, "%d", &num) == 1) {
                read_block(num);
            } else {
                printf("Invalid\n");
            }
            continue;
        }

        if (strncmp(line, "clearblock ", 11) == 0) {
            int num;
            if (sscanf(line + 11, "%d", &num) == 1) {
                FILE *fp = fopen("disk", "rb+");
                if (fp == NULL) {
                    printf("Error opening disk!\n");
                    continue;
                }
                clear_block(num, fp);
            } else {
                printf("Invalid\n");
            }
            continue;
        }

        if (strncmp(line, "asm ", 4) == 0) {
            char *file_name = line + 4;
            node *current = location->first_child;
            while (current != NULL) {
                if (strcmp(current->name, file_name) == 0) {
                    if (current->is_directory == 1) {
                        printf("Can't assemble a directory!\n");
                    } else {
                        if (current->blocks == NULL) {
                            printf("File is empty!\n");
                            break;
                        }
                        char *data = read_data(current->blocks, current->num_blocks, current->size);
                        if (data == NULL) {
                            printf("Error reading data!\n");
                            break;
                        }
                        char *binary = assemble(data, current->size);
                        printf("Result: %s\n", binary);
                        free(data);
                        data = NULL;

                        node *new_file = create_node(strcat(file_name, ".exe"), 0, NULL, 0);
                        if (new_file == NULL) {
                            printf("Error creating file\n");
                            free(binary);
                            binary = NULL;
                            break;
                        } else {
                            add_child(location, new_file);
                        }
                        int size = strlen(binary);
                        int block_size = BLOCK_SIZE - 1;
                        int num_blocks =(int) ceil((double) size / (double) block_size);
                        int *blocks = calloc(num_blocks, sizeof(int));
                        if (blocks == NULL) {
                            printf("Error allocating memory!\n");
                            free(binary);
                            binary = NULL;
                            break;
                        }
                        int *locations = write_data(binary);
                        if (locations == NULL) {
                            printf("Error writing data!\n");
                            free(binary);
                            binary = NULL;
                            free(blocks);
                            blocks = NULL;
                            break;
                        }
                        for (int i = 0; i < num_blocks; i++) {
                            blocks[i] = locations[i];
                        }
                        new_file->size = size;
                        new_file->num_blocks = num_blocks;
                        new_file->blocks = blocks;
                        blocks = NULL;
                        free(locations);
                        locations = NULL;
                        free(binary);
                        binary = NULL;
                    }
                    break;
                }
                current = current->next_sibling;
            }
            if (current == NULL) {
                printf("File not found\n");
            }
            continue;
        }
        if (strcmp(line, "mem") == 0) {
            print_mem(mem_head);
            continue;
        }
        if (strncmp(line, "run ", 4) == 0) {
            char *file_name = line + 4;
            node *current = location->first_child;
            while (current != NULL) {
                if (strcmp(current->name, file_name) == 0) {
                    if (current->is_directory == 1) {
                        printf("Can't run a directory!\n");
                    } else {
                        if (current->blocks == NULL) {
                            printf("File is empty!\n");
                            break;
                        }
                        char *data = read_data(current->blocks, current->num_blocks, current->size);
                        int num_pages = 0;
                        page **pages = load_data(mem_head, data, &num_pages);
                        process *pr = malloc(sizeof(process));
                        pids++;
                        num_running++;
                        pr->id = pids;
                        pr->running = 0;    
                        pr->num_pages = num_pages;
                        pr->size = current->size;
                        pr->pages = pages;
                        pr->A = 0;
                        pr->B = 0;
                        pr->C = 0;
                        pr->D = 0;
                        pr->IP = 0;
                        pr->flag_z = 0;
                        pr->cpu_stack = malloc(sizeof(stack));
                        pr->cpu_stack->top = NULL;
                        push_process(pr);
                        break;
                    }
                }
                current = current->next_sibling;
            }
            if (current == NULL) {
                printf("File not found\n");
            }
            continue;
        }

        printf("Command not found\n");
    }

    printf("Goodbye!\n");
    return 0;
}