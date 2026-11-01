#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>


int main(int argc, char * argv[]){
    setbuf(stdout, NULL);
    if (argc<3)
    {
        printf("ERRO - formato ficha3_3 <N> <S>\n");
        return 1;
    }

    int N = atoi(argv[1]);
    if (N==0)
    {
        printf("ERRO - N nao e' numero\n");
        return 1;
    }
    
    int S = atoi(argv[2]);
    if (S==0)
    {
        printf("ERRO - S nao e' numero\n");
        return 1;
    }

    for (int i = 0; i < N; i++)
    {
        printf("*");
        sleep(1);
    }
    

    return 0;
}