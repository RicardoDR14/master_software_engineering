#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>

int main()
{
    int a = 10;
    pid_t pid = fork();
    if (pid == 0)
    {
        printf("Filho: pid devolvido pelo fork foi %d\n", pid);
        pid_t pidFilho = getpid();
        printf("Filho: meu pid e' %d\n", pidFilho);
        printf("Filho: pid do pai e' %d\n", getppid());
        a++;
    }
    else
    {

        printf("Pai: pid devolvido pelo fork foi %d\n", pid);
        printf("Pai: meu pid e' %d\n", getpid());
        a--;
        int status;
        pid_t pidFilho = pid;
        waitpid(pidFilho, &status, 0);
        if (WIFEXITED(status)){
            printf("o processo filho com o pid: %d terminou com o status code %d\n",pidFilho,WEXITSTATUS(status));
        }else{
            printf("o processo com pid : %d terminou de forma abrupta\n", pidFilho);
        }

        
    }

    printf("a = %d\n", a);
    exit(100);
    //return 0;
}