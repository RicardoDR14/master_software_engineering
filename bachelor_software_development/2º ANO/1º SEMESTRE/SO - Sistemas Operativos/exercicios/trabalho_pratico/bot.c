#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>
#include "bot.h"

#define NCOL 40
#define NLIN 16

char RUNNING = 1;

void termina(int sig, siginfo_t *info, void *ucontext){
	RUNNING = 0;
}

int main(int argc, char* argv[]){
	int x, y, interval, duration;
	struct sigaction sa;


	if(argc!=3){
		fprintf(stderr, "[ERRO] Number pf command line args!\n             ./bot <interval> <duration>\n");
		return 1;
	}

	if(sscanf(argv[1],"%d", &interval) != 1 ||
	   sscanf(argv[2],"%d", &duration) != 1) {
		fprintf(stderr, "[ERRO] All args must be intergers!\n");
	   return 2;
	}

	srand(time(NULL));
	sa.sa_flags = SA_SIGINFO;
	sa.sa_sigaction = termina;
	sigaction(SIGINT, &sa, NULL);

	while (RUNNING){
		sleep(interval);
		x = rand() % NCOL;
		y = rand() % NLIN;
		printf("%d %d %d\n", x, y, duration);
		//sigqueue(pid,SIGURS1, x);
		fflush(stdout);
		}
	return 0;
}
