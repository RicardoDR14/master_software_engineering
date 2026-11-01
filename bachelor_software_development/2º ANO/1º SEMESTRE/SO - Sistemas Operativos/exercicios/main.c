#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

static int count = 0;

// void signalHandler(int signal, siginfo_t *info, void *context) // → sa_sigaction
void signalHandler(int signal) {  // → sa_handler
    if (++count == 5) {
        printf("\nok, pronto\n");
        exit(0);
    }

    if (signal == SIGUSR1)
        printf("\nBATATA\n");

    printf("\nai\n");
}

int main() {
    char name[20];
    struct sigaction signal;

    signal.sa_handler = signalHandler;
    // signal.sa_sigaction = signalHandler;
    signal.sa_flags = SA_SIGINFO | SA_RESTART;
    sigaction(SIGINT, &signal, NULL);
    sigaction(SIGUSR1, &signal, NULL);

    do {
        printf("\nEnter your name: ");
        if (scanf("%s", name) != 1)
            printf("Nao introduzio um nome!\n");

        if (strcmp(name, "sair") != 0)
            printf("ola %s\n", name);
    } while (strcmp(name, "sair") != 0);

    return 0;
}
