#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main() {
    char* dir_pt[] = {"batata", "carro", "garrafa", "oculos", "computador"};
    char* dir_fr[] = {"pomme de terre", "voiture", "bouteille", "lunettes", "ordinateur"};
    char palavra[20];

    fgets(palavra, sizeof(palavra), stdin);  // Recebe pelo stdin, mas é dup pipe [read]
    palavra[strlen(palavra)] = '\0';

    for (int i = 0; i < 5; i++) {
        if (strcmp(palavra, dir_pt[i]) == 0) {
            printf("%s", dir_fr[i]);
            exit(0);
        }
    }

    printf("<FRAN> inconnu\n");

    return 0;
}
