#include <stdio.h>
#include <stdlib.h>

char **environ;

int main(int argc, char *argv[], char *envp[]) {
    // cada string do array envp tem KEY=value
    //for (int i = 0; envp[i] != NULL; i++) {
    //    printf("%s\n", envp[i]);
    //}

    //usando a variavel environ
    //for (int i = 0; environ!= NULL && environ[i] != NULL; i++) {
    //    printf("%s\n", environ[i]);
    //}

    char * env_VAR = getenv("SO_AULA");

    if (env_VAR==NULL)
    {
        printf("Variavel SO_AULA nao existe!\n");
    }
    else
    {
        printf("SO_AULA= %s\n", env_VAR);
    }
    return 0;
}
