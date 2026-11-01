#include "utils.h"

int can_create_new_topic()
{
    return topic_count < MAX_TOPICS;
}
// Função para adicionar mensagem a um tópico
void add_message_to_topic(Topic *topic, const char *text, const char *author, int is_persistent, int lifetime)
{
    if (topic->message_count >= 5)
    {
        printf("Limite de mensagens persistentes para o tópico '%s' alcançado.\n", topic->name);
        return;
    }
    if (topic->is_locked == 1)
    {
        printf("\nTópico '%s' bloqueado", topic->name);
        return;
    }
    Message *message = malloc(sizeof(Message));
    strncpy(message->text, text, MAX_MSG);
    strncpy(message->author, author, MAX_USERNAME_LEN);
    message->is_persistent = is_persistent;
    message->lifetime = lifetime;

    topic->is_locked = 0;

    if (message->is_persistent)
    topic->messages[topic->message_count++] = message;
}
// Função para verificar se o tópico existe ou criá-lo
Topic *get_or_create_topic(const char *topic_name)
{
    for (int i = 0; i < topic_count; i++)
    {
        if (strcasecmp(topics[i].name, topic_name) == 0)
        {
            return &topics[i];
        }
    }

    if (topic_count < MAX_TOPICS)
    {
        Topic *new_topic = &topics[topic_count++];
        strncpy(new_topic->name, topic_name, sizeof(new_topic->name));
        new_topic->message_count = 0;
        printf("\nNovo tópico criado: %s", topic_name);
        return new_topic;
    }
    printf("\nNúmero máximo de tópicos alcançado.\n");
    return NULL;
}

void listTopics(int client_fd)
{
    char buffer[MAX_MSG * MAX_TOPICS];
    int offset = 0;

    for (int i = 0; i < topic_count; i++)
    {
        char *state = "Desbloqueado";
        if (topics[i].is_locked)
            state = "Bloqueado";
        int written = snprintf(buffer + offset, sizeof(buffer) - offset, "%d -> %s %d %s\n", i + 1, topics[i].name, topics[i].message_count, state);
        if (written < 0 || offset + written >= sizeof(buffer))
        {
            printf("Erro ao construir a lista de tópicos.\n");
            return;
        }
        offset += written;
    }

    if (topic_count == 0)
    {
        snprintf(buffer + offset, sizeof(buffer) - offset, "Lista de Tópicos vazia\n");
    }

    if (write(client_fd, buffer, strlen(buffer) + 1) < 0)
    {
        perror("Erro ao enviar a lista de tópicos para o cliente");
    }
}

Topic *get_topic_by_name(const char *topic_name)
{
    for (int i = 0; i < topic_count; i++)
    {
        if (strcasecmp(topics[i].name, topic_name) == 0)
        {
            return &topics[i];
        }
    }
    return NULL;
}

void add_subscriber_to_topic(Topic *topic, User *user)
{
    if (topic->subscribers_count >= MAX_USERS)
    {
        printf("Erro: Número máximo de subscritores atingido para o tópico '%s'.\n", topic->name);
        return;
    }

    pthread_mutex_lock(&topic->topics_lock); // Garantir thread safety

    for (int i = 0; i < topic->subscribers_count; i++)
    {
        if (topic->subscribers[i] == user)
        {
            // Utilizador já subscrito
            pthread_mutex_unlock(&topic->topics_lock);
            return;
        }
    }

    topic->subscribers[topic->subscribers_count++] = user;

    pthread_mutex_unlock(&topic->topics_lock);

    printf("Utilizador '%s' subscrito ao tópico '%s'.\n", user->username, topic->name);
}

void notify_subscribers(Topic *topic, const char *message, const char *sender)
{
    pthread_mutex_lock(&topic->topics_lock);

    for (int i = 0; i < topic->subscribers_count; i++)
    {
        User *subscriber = topic->subscribers[i];
        if (subscriber)
        {
            char formatted_message[MAX_MSG];
            snprintf(formatted_message, sizeof(formatted_message),
                     "[%s] %s: %s\n", topic->name, sender, message);
            write(subscriber->manager_to_client_fd, formatted_message, strlen(formatted_message) + 1);
            fsync(subscriber->manager_to_client_fd);
        }
    }

    pthread_mutex_unlock(&topic->topics_lock);
}

void remove_subscriber(Topic *topic, User *user)
{

    if (!topic)
    {
        printf("\nTópico não existe");
        return;
    }
    if (!user)
    {
        printf("\nUtilizador não existe");
        return;
    }

    pthread_mutex_lock(&topic->topics_lock);

    int found = 0;
    for (int i = 0; i < topic->subscribers_count; i++)
    {
        if (topic->subscribers[i] == user)
        {
            for (int j = i; j < topic->subscribers_count - 1; j++)
            {
                topic->subscribers[j] = topic->subscribers[j + 1];
            }
            topic->subscribers[--topic->subscribers_count] = NULL;
            found = 1;
            break;
        }
    }
    pthread_mutex_unlock(&topic->topics_lock);

    if (found)
    {
        printf("\nUtilizador %s removido como subscritor do tópico %s", user->username, topic->name);
    }
    else
    {
        printf("\nUtilizador %s não estava subscrito no tópico %s", user->username, topic->name);
    }
}
// Verificar se um Utilizador existe pelo nome, também puderia ser verificado pelo PID que teria o mesmo funcionamento
int user_exist(User users[MAX_USERS], const char *name)
{
    for (int i = 0; i < user_count; i++)
    {
        if (strcasecmp(users[i].username, name) == 0)
        {
            return 1;
        }
    }
    return 0;
}

void terminate_clients()
{
    for (int i = 0; i < user_count; i++)
    {
        printf("\nFinalizando Utilizador %s (PID %d)...", users[i].username, users[i].pid);
        kill(users[i].pid, SIGTERM);
        close(users[i].client_to_manager_fd);
        close(users[i].manager_to_client_fd);
    }
}

// THREAD QUE DECREMENTA O TEMP DAS MENSAGENS
void *decrement_lifetime(void *arg)
{
    while (1)
    {

        pthread_mutex_lock(&messages_mutex);

        for (int i = 0; i < topic_count; i++)
        {
            Topic *topic = &topics[i];
            for (int j = 0; j < topic->message_count; j++)
            {
                Message *msg = topic->messages[j];
                if (msg->lifetime > 0)
                {
                    msg->lifetime--;
                }
                if(msg->lifetime <=0){
                    for (int k = j; k < topic->message_count - 1; k++)
                    {
                        topic->messages[k] = topic->messages[k + 1];
                    }
                    topic->message_count--;
                    break;
                }
            }
        }
        pthread_mutex_unlock(&messages_mutex);
        sleep(1);
    }
    return NULL;
}

void save_persistent_messages()
{
    const char *FILENAME = getenv("MSG_FICH");
    FILE *file = fopen(FILENAME, "w");
    if (!file)
    {
        perror("Erro ao abrir arquivo para salvar mensagens persistentes");
        return;
    }
    for (int i = 0; i < topic_count; i++)
    {
        Topic *topic = &topics[i];
        for (int j = 0; j < topic->message_count; j++)
        {
            Message *msg = topic->messages[j];
            if (msg->lifetime > 0)
            {
                fprintf(file, "%s|%s|%s|%d|%d\n",
                        topic->name,
                        msg->author,
                        msg->text,
                        msg->is_persistent,
                        msg->lifetime);
            }
        }
    }

    fclose(file);
    printf("Mensagens persistentes salvas com sucesso.\n");
}

void load_persistent_messages()
{
    const char *FILENAME = getenv("MSG_FICH");
    FILE *file = fopen(FILENAME, "r");
    if (!file)
    {
        perror("Erro ao abrir arquivo para carregar mensagens persistentes");
        return;
    }

    char line[512];
    while (fgets(line, sizeof(line), file))
    {
        char topic_name[50], author[MAX_USERNAME_LEN], text[MAX_MSG];
        int is_persistent, lifetime;

        if (sscanf(line, "%[^|]|%[^|]|%[^|]|%d|%d",
                   topic_name,
                   author,
                   text,
                   &is_persistent,
                   &lifetime) == 5)
        {
            Topic *topic = get_or_create_topic(topic_name);
            if (topic)
            {
                Message *msg = malloc(sizeof(Message));
                strncpy(msg->text, text, MAX_MSG);
                strncpy(msg->author, author, MAX_USERNAME_LEN);
                msg->is_persistent = is_persistent;
                msg->lifetime = lifetime;

                topic->messages[topic->message_count++] = msg;
            }
        }
    }

    fclose(file);
    printf("Mensagens persistentes carregadas com sucesso.\n");
}

void send_persistent_messages_to_user(Topic *topic, int client_fd)
{
    for (int i = 0; i < topic->message_count; i++)
    {
        Message *msg = topic->messages[i];
        if (msg->is_persistent)
        {
            char buffer[MAX_MSG];
            snprintf(buffer, sizeof(buffer), "[%s] %s: %s\n",
                     topic->name,
                     msg->author,
                     msg->text);
            write(client_fd, buffer, strlen(buffer) + 1);
        }
    }
}
void listPersistentMessages(char *name)
{
    char buffer[2048];

    for (int i = 0; i < topic_count; i++)
    {
        if (strcmp(topics[i].name, name) == 0)
        {
            for (int j = 0; j < topics[i].message_count; j++)
            {
                strcat(buffer, topics[i].messages[j]);
                strcat(buffer, "\n");
            }
        }
        if (topic_count == 0)
        {
            printf("\nLista de tópicos vazia");
        }
        else
        {
            printf("%s", buffer);
        }
    }
}

void sigint_handler(int signum)
{
    printf("\nEncerrando o manager e desconectando todos os Utilizadores...\n");
    save_persistent_messages();
    terminate_clients();
    unlink(CONNECT_PIPE);
    exit(0);
}

void *client_handler(void *arg)
{
    User *user = (User *)arg;
    char buffer[MAX_MSG];

    while (read(user->client_to_manager_fd, buffer, sizeof(buffer)) > 0)
    {

        char command[20], topic_name[50], text[MAX_MSG];
        int duration;

        if (sscanf(buffer, "%s %s %d %[^\n]", command, topic_name, &duration, text) == 4)
        {
            if ((strcasecmp(command, "msg") == 0))
            {
                Topic *topic = get_or_create_topic(topic_name);
                if (topic && topic->is_locked == 0)
                {
                    int is_persistent = 0;
                    if (duration > 0)
                    {
                        is_persistent = 1;
                        save_persistent_messages();
                        send_persistent_messages_to_user(topic, user->manager_to_client_fd);
                    }
                    // int is_persistent = (duration > 0) ? 1 : 0;
                    // send_persistent_messages_to_user(topic, user->manager_to_client_fd);
                    add_subscriber_to_topic(topic, user);
                    add_message_to_topic(topic, text, user->username, is_persistent, duration);
                    notify_subscribers(topic, text, user->username);
                    printf("\nMensagem enviada para o tópico '%s' por %s.\n", topic_name, user->username);
                }
                else
                {
                    printf("\nTópico %s está bloqueado, impossivel enviar mensagens", topic_name);
                }
            }
            else
            {
                printf("\nComando mensagem errado");
            }
        }
        else if (sscanf(buffer, "%s %s", command, topic_name) == 2)
        {

            if (strcasecmp(command, "subscribe") == 0)
            {

                Topic *topic = get_topic_by_name(topic_name);
                if (!topic && can_create_new_topic())
                {
                    topic = get_or_create_topic(topic_name);
                    printf("\nNovo tópico '%s' criado.\n", topic_name);
                }

                if (topic)
                {
                    add_subscriber_to_topic(topic, user);
                    printf("\nUtilizador %s subscreveu ao tópico '%s'.\n", user->username, topic_name);
                    // Enviar mensagens persistentes se necessário

                    send_persistent_messages_to_user(topic, user->manager_to_client_fd);
                }
                else
                {
                    printf("\nErro: Não foi possível subscrever ao tópico '%s'.\n", topic_name);
                }
            }
            else if (strcasecmp(command, "unsubscribe") == 0)
            {
                Topic *topic = get_topic_by_name(topic_name);
                if (topic)
                {
                    remove_subscriber(topic, user);
                    printf("\nSubscritor removido do tópico %s", topic_name);
                }
                else
                {
                    printf("\nErro: Tópico %s não encontrado", topic_name);
                }
            }
            else
            {
                printf("\n Comando desconhecido %s", command);
            }
        }
        else if (strcasecmp(command, "topics") == 0)
        {
            listTopics(user->manager_to_client_fd);
        }
        else if (strcasecmp(buffer, "exit") == 0)
        {
            for (int i = 0; i < user_count; i++)
            {
                if (strcmp(users[i].username, user->username) == 0)
                    users[i] = users[user_count + 1];
            }
            user_count--;
            printf("Cliente %s desconectado.\n", user->username);
            signal(SIGINT, sigint_handler);
        }
        else
        {
            printf("Erro: Comando desconhecido ou incorreto.\n");
        }
    }

    // printf("\nUtilizador %s desconectado.", user->username);
    close(user->client_to_manager_fd);
    close(user->manager_to_client_fd);
    pthread_exit(NULL);
}

void listTopicsManager()
{
    char buffer[2048] = "Tópicos existentes:\n";

    for (int i = 0; i < topic_count; i++)
    {
        char num[1024];
        sprintf(num, "%d", topics[i].message_count);
        strcat(buffer, topics[i].name);
        strcat(buffer, "\t");
        strcat(buffer, num);
        strcat(buffer, "\n");
    }
    if (topic_count == 0)
    {
        printf("\nLista de tópicos vazia");
    }
    else
    {
        printf("%s", buffer);
    }
}

void remove_user(char *username)
{
    for (int i = 0; i < user_count; i++)
    {
        if (strcmp(users[i].username, username) == 0)
        {
            kill(users[i].pid, SIGTERM);
            close(users[i].client_to_manager_fd);
            close(users[i].manager_to_client_fd);

            // Remover utilizador do array de utilizadores
            for (int j = i; j < user_count - 1; j++)
            {
                users[j] = users[j + 1];
            }
            user_count--;

            printf("\n Utilizador %s Removido", username);
            return;
        }
    }
    printf("\nUtilizador não encontrado!");
    return;
}

void lock_topic(char *name, int lock)
{
    Topic *topic = get_topic_by_name(name);
    if (!topic)
    {
        printf("\nTópico %s não encontrado", name);
        return;
    }

    topic->is_locked = lock;
    if (lock)
    {
        printf("\nTópico %s bloqueado", name);
    }
    else
    {
        printf("\nTópico %s desbloqueado", name);
    }
}

void listUsers()
{
    if (user_count == 0)
    {
        printf("\n Nenhum utilizador conectado");
        return;
    }
    for (int i = 0; i < user_count; i++)
    {
        printf("\n %d->%s", i + 1, users[i].username);
    }
}

void close_manager()
{
    save_persistent_messages();
    for (int i = 0; i < user_count; i++)
    {
        kill(users[i].pid, SIGTERM);
        close(users[i].client_to_manager_fd);
        close(users[i].manager_to_client_fd);
    }
    printf("\nManager a terminar....\n");
    system("make clean");
    exit(0);
}

void *manager_commands_handler(void *arg)
{
    char buffer[200];
    while (1)
    {
        printf("\nManager-> ");
        fgets(buffer, sizeof(buffer), stdin);
        buffer[strcspn(buffer, "\n")] = 0;

        char command[50];
        char param[100];

        if (sscanf(buffer, "%s %[^\n]", command, param) == 2)
        {
            if (strcasecmp(command, "remove") == 0)
            {
                remove_user(param);
            }
            else if (strcasecmp(command, "show") == 0)
            {
                // if(get_topic_by_name(param))
                listPersistentMessages(param);
                // Mostrar todas as mensagens persistentes
            }
            else if (strcasecmp(command, "lock") == 0)
            {
                lock_topic(param, 1);
            }
            else if (strcasecmp(command, "unlock") == 0)
            {
                lock_topic(param, 0);
            }
        }
        else if (strcasecmp(command, "users") == 0)
        {
            listUsers();
        }
        else if (strcasecmp(command, "topics") == 0)
        {
            listTopicsManager(); // Função que lista os tópicos
        }
        else if (strcasecmp(command, "close") == 0)
        {
            // Encerrar o manager e todos os clientes ligados
            close_manager();
        }
        else
        {
            printf("\n Comando desconhecido [%s]", command);
        }
    }
}