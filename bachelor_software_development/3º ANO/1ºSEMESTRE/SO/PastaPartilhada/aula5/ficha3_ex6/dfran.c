#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
    // Verifica se o número de argumentos é válido
    if (argc != 2) {
        printf("Uso: %s <palavra_portuguesa>\n", argv[0]);
        return 1;
    }

    // Matrizes com palavras em português e suas traduções em francês
    char *palavras_portugues[] = {"casa", "gato", "carro", "sol", "livro"};
    char *traducoes_frances[] = {"maison", "chat", "voiture", "soleil", "livre"};

    // Variável para armazenar se a tradução foi encontrada
    int encontrada = 0;

    // Ciclo para procurar a palavra
    for (int i = 0; i < 5; i++) {
        if (strcmp(argv[1], palavras_portugues[i]) == 0) {
            printf("Tradução: %s\n", traducoes_frances[i]);
            encontrada = 1;
            break;
        }
    }

    // Se a palavra não foi encontrada, imprime "unknown"
    if (!encontrada) {
        printf("Tradução: unknown\n");
    }

    return 0;
}
