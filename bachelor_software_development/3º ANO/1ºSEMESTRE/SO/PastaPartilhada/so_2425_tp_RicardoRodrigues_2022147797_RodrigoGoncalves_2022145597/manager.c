#include "utils.h"

User users[MAX_USERS];
int user_count = 0;
Topic topics[MAX_TOPICS];
int topic_count = 0;
pthread_mutex_t messages_mutex = PTHREAD_MUTEX_INITIALIZER;

int main()
{

    signal(SIGINT, sigint_handler);
    printf("Manager iniciado. Aguardando conexões de Utilizadores...\n");
    pthread_mutex_init(&messages_mutex, NULL);

    char *filename = getenv("MSG_FICH");
    if (filename == NULL)
    {
        printf("Ficheiro de mensagens nao definido\n");
        exit(EXIT_FAILURE);
    }

    FILE *msgFile = fopen(filename, "w+");
    if (!msgFile)
    {
        perror("Erro ao abrir o ficheiro de autenticacao");
        exit(EXIT_FAILURE);
    }

    load_persistent_messages();

    pthread_t lifetime_thread;
    pthread_create(&lifetime_thread, NULL, decrement_lifetime, NULL);

    if (access(CONNECT_PIPE, F_OK) == 0)
    {
        printf("O FIFO '%s' já existe.\n", CONNECT_PIPE);
    }
    else
    {
        // Criar o FIFO
        if (mkfifo(CONNECT_PIPE, 0666) == -1)
        {
            perror("Erro ao criar o FIFO");
            exit(EXIT_FAILURE);
        }
        printf("FIFO '%s' criado com sucesso.\n", CONNECT_PIPE);
    }

    // Fazer algo com o FIFO (opcional)
    printf("Pronto para usar o FIFO '%s'.\n", CONNECT_PIPE);

    int connect_fd = open(CONNECT_PIPE, O_RDONLY);
    if (connect_fd < 0)
    {
        perror("\nErro ao abrir o connect_pipe");
        exit(1);
    }
    pthread_t commands_thread;
    pthread_create(&commands_thread, NULL, manager_commands_handler, NULL);
    pthread_detach(commands_thread);

    while (1)
    {
        char buffer[MAX_USERNAME_LEN + 10];

        if (read(connect_fd, buffer, sizeof(buffer)) > 0)
        {
            char name[MAX_USERNAME_LEN];
            pid_t pid;
            sscanf(buffer, "%s %d", name, &pid);

            if (user_count >= MAX_USERS)
            {
                printf("\nNÚMERO DE UTILIZADORES EXCEDIDO\n");
                continue;
            }

            if (user_exist(users, name))
            {
                printf("\nO nome já existe\n");
                continue;
            }

            // Inicia um novo Utilizador e configura os pipes
            User *new_user = &users[user_count++];
            strncpy(new_user->username, name, MAX_USERNAME_LEN);
            new_user->pid = pid;

            char client_to_manager_pipe[100];
            char manager_to_client_pipe[100];

            sprintf(client_to_manager_pipe, "ctm_%s", new_user->username);
            sprintf(manager_to_client_pipe, "mtc_%s", new_user->username);

            mkfifo(client_to_manager_pipe, 0666);
            mkfifo(manager_to_client_pipe, 0666);

            new_user->client_to_manager_fd = open(client_to_manager_pipe, O_RDONLY);
            new_user->manager_to_client_fd = open(manager_to_client_pipe, O_WRONLY);

            if (new_user->client_to_manager_fd < 0 || new_user->manager_to_client_fd < 0)
            {
                perror("Erro ao abrir os pipes dedicados do Utilizador");
                continue;
            }

            printf("Utilizador %s conectado (PID: %d).\n", new_user->username, new_user->pid);

            // Sempre que um cliente se conecta é criada uma thread para lidar com esse cliente
            pthread_t client_thread;
            if (pthread_create(&client_thread, NULL, client_handler, new_user) != 0)
            {
                perror("Erro ao criar thread para o Utilizador");
                continue;
            }
            pthread_detach(client_thread);
        }
    }
    close(connect_fd);
    unlink(CONNECT_PIPE);
    // save_persistent_messages();
    pthread_mutex_destroy(&messages_mutex);
    return 0;
}
