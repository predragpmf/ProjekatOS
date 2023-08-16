#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "asm.h"

char *assemble(char *data, int size) {
    char *word = strtok(data, " ");
    char *binary = calloc((size * 16) + 1, sizeof(char));
    char *p = binary;
    int count = 0;
    while (word != NULL) {
        if (strcmp(word, "JMP") == 0) {
            count += 8;
            memcpy(p, "00000001", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "CALL") == 0) {
            count += 8;
            memcpy(p, "00000010", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "RET") == 0) {
            count += 8;
            memcpy(p, "00000011", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "HLT") == 0) {
            count += 8;
            memcpy(p, "00000100", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "MOV") == 0) {
            count += 8;
            memcpy(p, "00000101", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "INC") == 0) {
            count += 8;
            memcpy(p, "00000110", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "DEC") == 0) {
            count += 8;
            memcpy(p, "00000111", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "CMP") == 0) {
            count += 8;
            memcpy(p, "00001000", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "JZ") == 0) {
            count += 8;
            memcpy(p, "00001001", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "JNZ") == 0) {
            count += 8;
            memcpy(p, "00001010", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "ADD") == 0) {
            count += 8;
            memcpy(p, "00001011", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }
        if (strcmp(word, "SUB") == 0) {
            count += 8;
            memcpy(p, "00001100", 8);
            p += 8;
            word = strtok(NULL, " ");
            continue;
        }

        int word_len = strlen(word);

        for (int i = 0; i < word_len; i++) {
            count += 8;
            int value = (int) word[i];

            char bin[8] = "00000000";

            for (int i = 0; i < 8; i++) {
                bin[7 - i] = ((value >> i) & 1) + '0'; 
            }

            memcpy(p, bin, 8);
            p += 8;
        }
        memcpy(p, "00000000", 8);
        count += 8;
        p += 8;
        word = strtok(NULL, " ");
    }
    binary[count] = '\0';
    word = NULL;
    p = NULL;
    return binary;
}