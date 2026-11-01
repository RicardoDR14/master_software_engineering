#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char* argv[]) {
    if (argc != 2) {
        printf("\nPreciso de 2 argumentos. Da-me o PID do sorteia\n");
        return 1;
    }

    printf("PID: %d\n", getpid());

    int pid;
    pid = atoi(argv[1]);
    union sigval val;

    while (1) {
        printf("\nQual o numero? ");
        scanf(" %d", &val.sival_int);
        if (val.sival_int == 0)
            break;
        sigqueue(pid, SIGUSR1, val);
    }
    return 0;
}