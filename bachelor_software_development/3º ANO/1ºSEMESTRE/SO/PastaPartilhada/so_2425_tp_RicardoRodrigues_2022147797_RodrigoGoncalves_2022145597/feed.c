#include "utils.h"

int main(int argc, char*argv[]) {
    setbuf(stdout,NULL);
    if(argc != 2){
        printf("SINTAXE: ./feed <username>");
        exit(-1);
    }
    
    if(strlen(argv[1])>= MAX_USERNAME_LEN){
         fprintf(stderr, "Erro: O nome de usuário não pode exceder %d caracteres.\n", MAX_USERNAME_LEN - 1);
        exit(-1);
    }

    //sleep(1);
    int connect_fd = open(CONNECT_PIPE, O_WRONLY);
    if (connect_fd < 0) {
        perror("Erro ao abrir o connect_pipe");
        exit(1);
    }

    signal(SIGTERM, sigterm_handler);
    
    pid_t pid = getpid();
    char msg[MAX_USERNAME_LEN + 10];
    snprintf(msg, sizeof(msg), "%s %d", argv[1], pid);

    write(connect_fd, msg, strlen(msg) + 1);
    close(connect_fd);

    char client_to_manager_pipe[100];
    char manager_to_client_pipe[100];
    
    sleep(1);//Temos este sleep porque se não quando o feed tenta acessar aos pipes dedicados estes ainda não foram abertos

    sprintf(client_to_manager_pipe, "ctm_%s", argv[1]);
    sprintf(manager_to_client_pipe, "mtc_%s", argv[1]);
    
    int ctm_fd = open(client_to_manager_pipe, O_WRONLY);
    int mtc_fd = open(manager_to_client_pipe, O_RDONLY);

    if (ctm_fd < 0 || mtc_fd < 0) {
        perror("Erro ao abrir os pipes dedicados");
        exit(1);
    }

    printf("Conectado ao manager com PID: %d. Você pode enviar e receber mensagens.\n", pid);


    //pthread_t listener_thread;
    //ThreadArgs args = { .manager_to_client_fd = mtc_fd };
    //pthread_create(&listener_thread, NULL, listen_for_messages, &args);

    while(1){
        char command[100];
        char input[300];
        char topic[50];
        int duration;
        char msg[MAX_MSG_LENGTH];
        char response[300];


        printf("\n\nMenu");
        printf("\nVer listagem de tópicos (topics)");
        printf("\nEnviar mensagem (msg <tópico> <duração> <Mensagem>)");
        printf("\nSubscrever tópico (subscribe <tópico>)");
        printf("\nRemover subscrição (unsubscribe <tópico>)");
        printf("\nSair (exit)\n");    
        
        printf("\nOpção->");
        fflush(stdout);
        fgets(input, sizeof(input), stdin);
	    printf("%s",input);
        
        
        if (sscanf(input, "%s", command) != 1) {
            printf("Erro: comando inválido.\n");
            continue;
        }
        
        if (strcasecmp("topics", command) == 0) {
            printf("\nComando TOPICS");
            write(ctm_fd, input, strlen(input)+1);
            int nbytes =  read(mtc_fd, response, sizeof(response));
            printf("\nTópicos disponíveis:\n%s\n", response);

        } else if (strcasecmp("msg", command) == 0) {
            if (sscanf(input, "msg %49s %d %292s[^\n]", topic, &duration, msg) == 3) {
                //printf(input, sizeof(input), "msg %s %d %s", topic, duration, msg);
                input[sizeof(input) - 1] = '\0'; 
                write(ctm_fd, input, strlen(input) + 1);
            } else {
                printf("\nErro: sintaxe incorreta para o comando msg.");
            }

        } else if (strcasecmp("subscribe", command) == 0) {
            if (sscanf(input, "subscribe %49s", topic) == 1) {
                printf("\nComando subscribe com sintaxe correta");
                printf("\nTópico: %s", topic);
                input[sizeof(input) - 1] = '\0'; 
                write(ctm_fd, input, strlen(input) + 1);
                
            } else {
                printf("\nErro: sintaxe incorreta para o comando subscribe.");
            }

        }else if(strcasecmp("unsubscribe", command) == 0){
            if(sscanf(input,"unsubscribe %60s", topic) == 1){
                printf("\nComando unsubscribe com sintaxe correta");
                printf("\nTópico: [%s]", topic);
                //input[sizeof(input) - 1] = '\0'; 
                write(ctm_fd, input, strlen(input) + 1);            
            }else{
                printf("\nErro: sintaxe incorreta para o comando unsubscribe.");
            }
        }else if(strcasecmp("exit", command) == 0){
            printf("Saindo do programa...\n");
            write(ctm_fd, "exit", strlen("exit") + 1);
            break;
        }else{
            printf("Erro: comando inválido.\n");
        }
               
    }

    close(ctm_fd);
    close(mtc_fd);
    return 0;
}
