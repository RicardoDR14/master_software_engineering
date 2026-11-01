/*============================== Servidor basico UDP ==========================
  Este servidor UDP destina-se a mostrar os conteudos dos datagramas recebidos.
  O porto de escuta encontra-se definido pela constante SERV_UDP_PORT.
  Assume-se que as mensagens recebida sao cadeias de caracteres (ou seja,
  "strings").
===============================================================================*/

#include <stdio.h>
#include <winsock.h>
#include <windows.h>

#pragma comment(lib,"ws2_32.lib")

#define SERV_UDP_PORT 5001
#define BUFFERSIZE 4096

void Abort(char* msg);

/*________________________________ main ________________________________________
*/

int main(int argc, char* argv[])
{
	SOCKET sockfd;
	int iResult, nbytes, msg_len, lenght_addr, opt;
	struct sockaddr_in serv_addr, cli_addr;
	char buffer[BUFFERSIZE], msg;
	WSADATA wsaData;

	/*=============== INICIA OS WINSOCKS ==============*/

	iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*============ CRIA O SOCKET PARA RECEPCAO/ENVIO DE DATAGRAMAS UDP ============*/

	if ((sockfd = socket(PF_INET, SOCK_DGRAM, 0)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket");

	/*=============== ASSOCIA O SOCKET AO  ENDERECO DE ESCUTA ===============*/

	/*Define que pretende receber datagramas vindos de qualquer interface de
	rede, no porto pretendido*/

	memset((char*)&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET; /*Address Family: Internet*/
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /*Host TO Network Long*/
	serv_addr.sin_port = htons(SERV_UDP_PORT);  /*Host TO Network Short*/

	/*Associa o socket ao porto pretendido*/

	if (bind(sockfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de registar-se para escuta");


	/*================ PASSA A ATENDER CLIENTES INTERACTIVAMENTE =============*/

	while (1) {

		fprintf(stderr, "<SER1>Esperando datagrama...\n");

		/*==================== 8 - Opcional, por sleep do lado do servidor para testar o timeout  =============*/
		//NOTA: requer o #include <windows.h>
		Sleep(15000);

		lenght_addr = sizeof(cli_addr);
		nbytes = recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)&cli_addr, &lenght_addr);

		if (nbytes == SOCKET_ERROR) {
			Abort("Erro na recepcao de datagramas");
		}

		buffer[nbytes] = '\0'; /*Termina a cadeia de caracteres recebidos com '\0'*/

		/*================3 - MOSTRAR MENSAGEM E LOCALIZAÇÃO DE ORIGEM =============*/
		//Nota utilizei a solução do professor estão aqui as 2
		/*
		* O que pretendemos neste exercício é mostrar (além das mensagens) a
		* localização de quem enviou os datagramas e para isso podemos/devemos
		* usar a estrutura cli_addr usada na linha 54 dentro da função recvfrom.
		*
		* A função recvfrom recebe uma estrutura vazia e preenche-a com a informação
		* de quem enviou o datagrama (IP e porto - entre outros).
		* NOTA: Lembrem-se do desenho que fiz no quadro.
		*
		* Depois é só usar as funções auxiliares para traduzir algo que não conhecemos
		* para algo que conhecemos e podemos usar no printf.
		*
		*
		* P: Porque não podemos usar o getsockname nesta situação?
		* R: Lembrem-se que o getsockname permite obter informações associadas
		*	 ao socket. O socket do servidor contém informações do servidor e não
		*    dos clientes. Logo, se usassemos a função getsockname aqui iriamos
		*    estar a mostrar o IP e porto do servidor e não é isso que se pretende
		*    neste exercício (até porque nós já sabemos o IP e porto do servidor).
		*/

		printf("\n<SER1>Mensagem recebida {%s} de {IP: %s; porto: %d}\n",
			buffer, inet_ntoa(cli_addr.sin_addr), ntohs(cli_addr.sin_port));

		/*
		* Para quem não percebeu a estrutura ou como se usa o printf vou tentar explicar
		* (se mesmo assim não entenderem... peçam ajuda! Discord, aula, email, . . .)
		*
		* A função printf permite-nos escrever uma mensagem de texto na consola (terminal).
		* Além disso permite-nos formatar a mensagem inserindo variáveis em sitios específicos.
		* Esses "sítios" são especificados através de: {%s} ou {%d} (entre outros)
		* Isto quer dizer que nesses sítios será introduzida uma variável.
		* Essas variáveis podem ser:
		*	- arrays de caracteres daí usarmos {%s}
		*	- inteiros daí usarmos {%d}
		*	- entre outros
		*
		* As variáveis em si são introduzidas por ordem como parametros da função printf.
		* Ou seja, se tiverem 3 {%s} ou {%d} têm que especificar 3 variáveis.
		*
		* NOTA: é mais fácil explicar no quadro, lembrem-me na próxima aula :)
		*/

		/*======================2 - ENVIA MENSAGEM AO CLIENTE ==================*/

		if (sendto(sockfd, buffer, strlen(buffer) + 1, 0, (struct sockaddr*)&cli_addr, sizeof(cli_addr)) == SOCKET_ERROR)
			Abort("O subsistema de comunicacao nao conseguiu aceitar o datagrama");

		printf("<CLI1>Mensagem enviada ... a entrega nao e' confirmada.\n");

	}

}

/*________________________________ Abort________________________________________
  Mostra uma mensagem de erro e o código associado ao ultimo erro com Winsocks.
  Termina a aplicacao com "exit status" a 1 (constante EXIT_FAILURE)
________________________________________________________________________________*/

void Abort(char* msg)
{
	WSACleanup();
	fprintf(stderr, "<SERV>Erro fatal: <%s> (%d)\n", msg, WSAGetLastError());
	exit(EXIT_FAILURE);

}