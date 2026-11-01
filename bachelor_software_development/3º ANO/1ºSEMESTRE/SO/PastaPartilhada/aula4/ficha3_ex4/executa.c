#include <stdio.h>
#include <string.h>
#include <unistd.h>

int main(){
    char str[20]; 
    printf("port ou ing?\n");
    scanf("%s", &str);
    if (strcmp(str, "port")==0)
    {    //executar programa port
        execlp("./port", "port", NULL);
    }else if (strcmp(str, "ing")==0)
    {    //executar programa port
        execlp("./ing","ing",NULL);
    }

    printf("Missao cumprida\n");
    return 0;
    
    
}