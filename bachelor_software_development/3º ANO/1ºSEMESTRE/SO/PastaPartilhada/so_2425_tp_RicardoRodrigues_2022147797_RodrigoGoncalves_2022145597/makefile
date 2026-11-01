export MSG_FICH = 'mensagens.txt'

# Alvo padrão
all: broker feed 

# Compilação do executável manager
broker: manager.o func_manager.o
	@echo "Criando o executável do manager"
	gcc manager.o func_manager.o -o manager -lpthread

# Compilação do executável feed
feed:  feed.o func_feed.o
	@echo "Criando o executável do feed"
	gcc  feed.o func_feed.o -o feed -lpthread

# Compilar os arquivos .c em .o
feed.o: feed.c utils.h
	gcc -c feed.c

func_feed.o: func_feed.c utils.h
	gcc -c func_feed.c

manager.o: manager.c utils.h
	gcc -c manager.c

func_manager.o: func_manager.c utils.h
	gcc -c func_manager.c

# Executar o manager com a variável de ambiente MSG_FICH
run-manager: manager
	@echo "Executando o manager com a variável de ambiente MSG_FICH"
	MSG_FICH=$(MSG_FICH) ./manager

# Executar o feed com argumentos
run-feed: feed # Exemplo de uso: make run-feed ARGS="<username>"
	@echo "Executando o feed com argumento: $(ARGS)"
	./feed $(ARGS)

# Limpeza dos arquivos gerados
clean:
	rm -f manager feed
	rm -f connect_pipe ctm_* mtc_*
	rm -f client_to_manager_* manager_to_client_*
	rm -f CONNECT_PIPE
	rm -f mensagens.txt
	rm -f *o
