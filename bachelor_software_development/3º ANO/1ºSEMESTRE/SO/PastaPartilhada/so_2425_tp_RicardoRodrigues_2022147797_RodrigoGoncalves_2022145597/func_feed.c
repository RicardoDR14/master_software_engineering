#include "utils.h"

void* listen_for_messages(void* arg) {
    ThreadArgs* args = (ThreadArgs*)arg;
    char message[MAX_MSG_LENGTH];

    while (1) {
        int bytes_read = read(args->manager_to_client_fd, message, sizeof(message));
        if (bytes_read > 0) {
            message[bytes_read] = '\0'; 
            printf("\n%s\n", message);
        }
    }
    return NULL;
}

void sigterm_handler(int signum) {
    printf("\nDesconectado pelo manager.\n");
    exit(0); 
}