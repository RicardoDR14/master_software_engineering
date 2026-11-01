#ifndef UTILS_H
#define UTILS_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <signal.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>

#define MAX_USERS 10
#define MAX_MSG 1000
#define MAX_USERNAME_LEN 50
#define MAX_TOPICS 50
#define CONNECT_PIPE "connect_pipe"
#define MAX_MSG_LENGTH 300

typedef struct
{
    char text[MAX_MSG];
    char author[MAX_USERNAME_LEN];
    int is_persistent;
    int lifetime;
} Message;

typedef struct
{
    char username[MAX_USERNAME_LEN];
    pid_t pid;
    int client_to_manager_fd;
    int manager_to_client_fd;
    struct User *next;
} User;

typedef struct
{
    char name[50];
    Message *messages[100];
    int message_count;
    int is_locked;
    User *subscribers[MAX_USERS];
    int subscribers_count;
    pthread_mutex_t topics_lock;
} Topic;

typedef struct {
    int manager_to_client_fd;
} ThreadArgs;

//variaveis globais do manager
extern User users[MAX_USERS];
extern int user_count;
extern Topic topics[MAX_TOPICS];
extern int topic_count;
extern pthread_mutex_t messages_mutex;

//funcoes feed
void* listen_for_messages(void* arg);
void sigterm_handler(int signum);

//funcoes manager
int can_create_new_topic();
void add_message_to_topic(Topic *topic, const char *text, const char *author, int is_persistent, int lifetime);
Topic *get_or_create_topic(const char *topic_name);
void listTopics(int client_fd);
Topic *get_topic_by_name(const char *topic_name);
void add_subscriber_to_topic(Topic *topic, User *user);
void notify_subscribers(Topic *topic, const char *message, const char *sender);
void remove_subscriber(Topic *topic, User *user);
int user_exist(User users[MAX_USERS], const char *name);
void terminate_clients();
void *decrement_lifetime(void *arg);
void save_persistent_messages();
void load_persistent_messages();
void send_persistent_messages_to_user(Topic *topic, int client_fd);
void listPersistentMessages(char *name);
void sigint_handler(int signum);
void *client_handler(void *arg);
void listTopicsManager();
void remove_user(char *username);
void lock_topic(char *name, int lock);
void listUsers();
void close_manager();
void *manager_commands_handler(void *arg);


#endif // UTILS_H
