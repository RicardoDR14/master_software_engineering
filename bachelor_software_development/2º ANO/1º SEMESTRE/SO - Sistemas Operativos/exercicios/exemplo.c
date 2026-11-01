#include <signal.h>
#include <stdio.h>
#include <unistd.h>

/*
union sigval{
    int sival_int;
    void *sival_ptr;
}
*/

void handler_funcSinal(int signum,siginfo_t *info, void *secret){
    printf("\n[SINAL - %d] %d\n", signum, valor);
    printf("Do pid [%d]\n", info->si_pid);
    printf("Com valor [%d]\n", )
}


int main(){
    int i;
    printf("PID [%d]\n", getpid());
    struct sigaction sa;
    //sa.sa_handler = handler_funcSinal;
    sa.sa_sigaction = handler_funcSinal;
    sa.sa_flags = SA_SIGINFO | SA_RESTART;
    sigaction(SIGINT, &sa, NULL);
    //alarm(200);
    scanf("%d", &i);
}




/*int main(){
    int pid;
    printf("\nqual o pid\n");
    scanf("%d", &pid);
    union sigval val;
    val.sigval_int = 123;
    sigqueue(pid, SIGINT, val);
}*/