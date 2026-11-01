#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
    char* dir_pt[] = {"batata", "carro", "garrafa", "oculos", "computador"};
    char* dir_eng[] = {"potato", "car", "bottle", "glasses", "computer"};
    char palavra[20];

    printf("PID RDING: %d", getpid());
    printf("<ING> Insira uma palavra: ");
    fgets(palavra, sizeof(palavra), stdin);  // Recebe pelo stdin
    palavra[strlen(palavra) - 1] = '\0';

    for (int i = 0; i < 5; i++) {
        if (strcmp(palavra, dir_pt[i]) == 0) {
            printf("%s\n", dir_eng[i]);
            exit(0);
        }
    }

    printf("<ING> unknown\n");

    return 0;
}
