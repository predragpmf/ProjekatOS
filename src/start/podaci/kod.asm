JMP start

loop:
INC A
CMP B, A
JNZ loop
RET

start:
MOV B, 7
MOV A, 0
CALL loop
HLT