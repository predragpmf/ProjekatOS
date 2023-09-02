#include <stdio.h>
#include <stdlib.h>
#include <bsd/string.h>
#include <math.h>
#include <unistd.h>

#include "disk.h"

void create_disk() {
    if (access("disk", F_OK) == 0) {
        //printf("Disk found!\n");
    } else {
        FILE *fp = fopen("disk", "wb");
        if (fp == NULL) {
            printf("Error creating disk!\n");
            return;
        }
        char null_byte = '\0';
        char zero_byte = '0';
        for (int i = 0; i < NUM_BLOCKS; i++) {
            // write zero byte at start of block
            fwrite(&zero_byte, sizeof(char), 1, fp);
            // write remaining null bytes in block
            for (int j = 1; j < BLOCK_SIZE; j++) {
                fwrite(&null_byte, sizeof(char), 1, fp);
            }
        }
        fclose(fp);
    }
}

int *write_data(char *data) {
    int block = BLOCK_SIZE - 1;
    int size = strlen(data);
    int num_blocks =(int) ceil((double) size / (double) block);

    FILE *fp = fopen("disk", "rb+");
    if (fp == NULL) {
        printf("Error opening disk!\n");
        return NULL;
    }

    if (num_blocks > freespace(fp)) {
        printf("Out of space!\n");
        fclose(fp);
        return NULL;
    }
    char *subarray = calloc(BLOCK_SIZE, sizeof(char));
    if (subarray == NULL) {
        printf("Error allocating memory\n");
        fclose(fp);
        return NULL;
    }
    char *p = data;
    int *locations = calloc(num_blocks, sizeof(int));
    if (locations == NULL) {
        printf("Error allocating memory\n");
        free(subarray);
        fclose(fp);
        return NULL;
    }
    for (int i = 0; i < num_blocks; i++) {
        int len = strlen(p);
        if (len > block) {
            len = block;
        }
        strncpy(subarray, p, len);
        subarray[len] = '\0';
        p += len;

        locations[i] = write_block(subarray, fp);
        if (locations[i] == -1) {
            printf("Error writing to disk\n");
            fclose(fp);
            free(locations);
            locations = NULL;
            free(subarray);
            subarray = NULL;
            p = NULL;
            return NULL;
        }
    }

    free(subarray);
    subarray = NULL;
    p = NULL;
    fclose(fp);
    return locations;
}



int write_block(char *block, FILE *fp) {
    for (int i = 0; i < NUM_BLOCKS; i++) {
        if (fseek(fp, i * BLOCK_SIZE, SEEK_SET) != 0) {
            printf("Error seeking to block %d\n", i);
            return -1;
        }
        char c;
        // Read the first byte of the block:
        fread(&c, sizeof(char), 1, fp);
        // If the first byte is 0, then the block is free:
        if (c == '0') {
            char one_byte = '1';
            fseek(fp, i * BLOCK_SIZE, SEEK_SET);
            fwrite(&one_byte, sizeof(char), 1, fp);
            fwrite(block, sizeof(char), strlen(block), fp);
            return i;
        }
        fseek(fp, (i + 1) * BLOCK_SIZE, SEEK_SET);
    }
    return -1;
}

int clear_data(int *blocks, int num_blocks) {
    if (blocks == NULL) {
        return 0;
    }
    FILE *fp = fopen("disk", "rb+");
    if (fp == NULL) {
        printf("Error opening disk file\n");
        return -1;
    }
    for (int i = 0; i < num_blocks; i++) {
        clear_block(blocks[i], fp);
    }
    fclose(fp);
    return 0;
}

void clear_block(int block, FILE *fp) {
    char zero_byte = '0';
    char null_byte = '\0';
    fseek(fp, block * BLOCK_SIZE, SEEK_SET);
    fwrite(&zero_byte, sizeof(char), 1, fp);
    for (int i = 1; i < BLOCK_SIZE; i++) {
        fwrite(&null_byte, sizeof(char), 1, fp);
    }
}

int format_disk() {
    FILE *fp = fopen("disk", "wb");
    if (fp == NULL) {
        printf("Error creating disk!\n");
        return -1;
    }
    char null_byte = '\0';
    char zero_byte = '0';
    for (int i = 0; i < NUM_BLOCKS; i++) {
        // write zero byte at start of block
        fwrite(&zero_byte, sizeof(char), 1, fp);
        // write remaining null bytes in block
        for (int j = 1; j < BLOCK_SIZE; j++) {
            fwrite(&null_byte, sizeof(char), 1, fp);
        }
    }
    fclose(fp);
    return 0;
}

char *read_data(int *blocks, int num_blocks, int data_length) {
    FILE *fp = fopen("disk", "rb");
    if (fp == NULL) {
        printf("Error opening disk file\n");
        return NULL;
    }
    int data_size = (BLOCK_SIZE - 1) * num_blocks + 1;
    char *data = calloc(data_size, sizeof(char));
    if (data == NULL) {
        printf("Error allocating memory\n");
        fclose(fp);
        return NULL;
    }
    char *p = data;
    for (int i = 0; i < num_blocks; i++) {
        fseek(fp, blocks[i] * BLOCK_SIZE + 1, SEEK_SET);
        fread(p, sizeof(char), BLOCK_SIZE - 1, fp);
        p += BLOCK_SIZE - 1;
    }
    p = NULL;
    fclose(fp);
    return data;
}

void read_block(int n) {
    FILE *fp = fopen("disk", "rb");
    if (fp == NULL) {
        printf("Error opening disk file\n");
        return;
    }
    char *data = calloc(BLOCK_SIZE, sizeof(char));
    fseek(fp, (n * BLOCK_SIZE) + 1, SEEK_SET);
    fread(data, sizeof(char), BLOCK_SIZE - 1, fp);
    data[BLOCK_SIZE - 1] = '\0';
    printf("Block %d: |%s|\n", n, data);
    free(data);
    data = NULL;
    fclose(fp);
    fp = NULL;
}

int freespace(FILE *fp) {
    int count = 0;
    for (int i = 0; i < NUM_BLOCKS; i++) {
        if (fseek(fp, i * BLOCK_SIZE, SEEK_SET) != 0) {
            printf("Error seeking to block %d\n", i);
            fclose(fp);
            exit(1);
        }
        char c;
        fread(&c, sizeof(char), 1, fp);
        if (c == '0') {
            count++;
        }
        fseek(fp, (i + 1) * BLOCK_SIZE, SEEK_SET);
    }
    return count;
}