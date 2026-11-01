#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>

#define NCOL 40
#define NLIN 16

struct DadosSinal {
    int x;
    int y;
    int duration;
};

char RUNNING = 1;

void termina(int sig, siginfo_t *info, void *ucontext) {
   RUNNING = 0;
}

int main(int argc, char * argv[]) {
   int x, y, interval, duration, pid_motor;
   struct sigaction sa;

   if (argc!=4) {
      fprintf(stderr, "[ERROR] Number of command line args!\n       ./bot <interval> <duration> <PID motor>\n");
      return 1;
   }
   if (sscanf(argv[1],"%d", &interval) != 1 ||
       sscanf(argv[2],"%d", &duration) != 1 ||
       sscanf(argv[3],"%d", &pid_motor) != 1) {
      fprintf(stderr, "[ERROR] All args must be integers!\n");
      return 2;
   }
   srand(time(NULL));
   sa.sa_flags = SA_SIGINFO;
   sa.sa_sigaction = termina;
   sigaction(SIGINT, &sa, NULL);

   while (RUNNING) {
      sleep(interval);
      x = rand() % NCOL;
      y = rand() % NLIN;
      struct DadosSinal dados;
        dados.x = x;
        dados.y = y;
        dados.duration = duration;
      printf("%d %d %d\n", x, y, duration);
      sigqueue(pid_motor, SIGUSR1, *(union sigval *)&dados);
      fflush(stdout);
   }
   return 0;
}
