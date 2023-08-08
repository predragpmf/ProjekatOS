#ifndef DISK_H
#define DISK_H

#define BLOCK_SIZE 32
#define NUM_BLOCKS 512
#define DISK_SIZE (BLOCK_SIZE - 1) * NUM_BLOCKS


void create_disk();
int *write_data(char *data);
int write_block(char *block, FILE *fp);
int clear_data(int *blocks, int num_blocks);
void clear_block(int block, FILE *fp);
int format_disk();
char *read_data(int *blocks, int num_blocks, int data_length);
void read_block(int n);
int freespace(FILE *fp);

#endif // DISK_H