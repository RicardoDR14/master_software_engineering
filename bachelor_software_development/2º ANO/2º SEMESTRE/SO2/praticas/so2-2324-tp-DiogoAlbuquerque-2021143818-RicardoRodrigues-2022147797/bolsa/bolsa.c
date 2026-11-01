#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <tchar.h>
#include <locale.h>
#include <windows.h>
#include <string.h>

#define FILE_MAP_NAME "SharedMemory"
#define EVENT_NAME "SharedEvent"
#define EVENT_BOARD "AvisaThrBoard"
#define BUFFER_SIZE 2048
#define MAX_ACOES 100
#define MAX_CLIENTES 20
#define MAX_NOME 20
#define PIPE_NAME _T("\\\\.\\pipe\\bolsa_pipe")

typedef struct {
	TCHAR nome[MAX_NOME];
	int preco;
	int quantidade;
} ACAO;

typedef struct {
	ACAO* acoes;
	int n_acoes_bolsa;
}BOLSA;

typedef struct {
	BOLSA* b;
	HANDLE* hMutex, hPipe, hEv_atualizaBoard;
	BOOL* continua;
}DATA_FUNCOES;

typedef struct {
	BOLSA* bolsa;
	HANDLE* hEv_atualizaBoard, hEv_avisaThrBoard;
	TCHAR* lpMapAddress;
	BOOL* continua;
}DATA_THREAD_BOARD;

typedef struct {
	TCHAR nome_cliente[MAX_NOME];
	TCHAR passe[5];
	DOUBLE saldo;
	BOLSA* carteira_acoes;
}CLIENTE;

typedef struct {
	CLIENTE* clientes;
	int n_utilizadores;
}UTILIZADORES;

typedef struct {
	BOLSA* bolsa;
	UTILIZADORES* utilizadores;
}DATA_THR_CLIENTE;


//// Fun��o para realizar a compra de a��es
void comprarAcao(ACAO acao, BOLSA* bolsa, CLIENTE* atual) {

	int indice = -1;

	for (int i = 0; i < bolsa->n_acoes_bolsa; i++)
	{
		if (_tcscmp(acao.nome, bolsa->acoes[i].nome) == 0)
		{
			indice = i;
			break;
		}
	}

	if (indice != -1 &&
		acao.quantidade > 0 &&
		acao.quantidade <= bolsa->acoes[indice].quantidade &&
		acao.quantidade * bolsa->acoes[indice].preco < atual->saldo) {

		for (int i = 0; i < atual->carteira_acoes->n_acoes_bolsa; i++)
		{
			if (_tcscmp(atual->carteira_acoes->acoes[i].nome, bolsa->acoes[i].nome) != 0)
				(atual->carteira_acoes->n_acoes_bolsa)++;
		}

		_tcscpy_s(atual->carteira_acoes[atual->carteira_acoes->n_acoes_bolsa].acoes->nome, MAX_NOME, acao.nome);
		atual->carteira_acoes[atual->carteira_acoes->n_acoes_bolsa].acoes->preco = bolsa->acoes[indice].preco;
		atual->carteira_acoes[atual->carteira_acoes->n_acoes_bolsa].acoes->quantidade += acao.quantidade;
		atual->saldo -= acao.quantidade * bolsa->acoes[indice].preco;

		_tprintf(_T("\nCompra Feita!!!\n"));
		return;
	}
	_tprintf(_T("\nCompra rejeitada!!!\n"));
}

//// Fun��o para realizar a venda de a��es
void venderAcao(ACAO acao, BOLSA* bolsa, CLIENTE* atual) {

	int indice = -1;

	for (int i = 0; i < atual->carteira_acoes->n_acoes_bolsa; i++)
	{
		if (_tcscmp(acao.nome, bolsa->acoes[i].nome) == 0)
		{
			indice = i;
			break;
		}
	}

	if (indice != -1 &&
		acao.quantidade > 0) {

		if (acao.quantidade == atual->carteira_acoes->acoes[indice].quantidade)
		{
			atual->carteira_acoes->acoes[indice].preco = -1;
			atual->carteira_acoes->acoes[indice].quantidade = -1;
			_tcscpy_s(atual->carteira_acoes->acoes[indice].nome, MAX_NOME, _T("x"));
			(atual->carteira_acoes->n_acoes_bolsa)--;
		}
		else {
			atual->saldo += acao.quantidade * bolsa->acoes[indice].preco;
			atual->carteira_acoes[atual->carteira_acoes->n_acoes_bolsa].acoes->quantidade -= acao.quantidade;
		}
		_tprintf(_T("\nVenda feita!!!\n"));
		return;
	}

	_tprintf(_T("\nVenda negada\n"));
}

void adicionarAcao(ACAO* nova, BOLSA* bolsa) {
	if (bolsa->n_acoes_bolsa < MAX_ACOES) {
		_tcscpy_s(bolsa->acoes[bolsa->n_acoes_bolsa].nome, MAX_NOME, nova->nome);
		bolsa->acoes[bolsa->n_acoes_bolsa].preco = nova->preco;
		bolsa->acoes[bolsa->n_acoes_bolsa].quantidade = nova->quantidade;
		(bolsa->n_acoes_bolsa)++;
	}
	else {
		_tprintf(_T("N�mero m�ximo de a��es atingido.\n"));
	}
}

void listarAcoes(BOLSA* bolsa) {
	_tprintf(_T("Lista de a��es:\n"));
	for (int i = 0; i < bolsa->n_acoes_bolsa; i++)
	{
		_tprintf(_T("* Nome: %s, Pre�o: %d, N� de a��es restantes: %d;\n"), bolsa->acoes[i].nome, bolsa->acoes[i].preco, bolsa->acoes[i].quantidade);
	}
}

// Fun��o para simular a varia��o dos pre�os das a��es
void listarUtilizadores(UTILIZADORES* u) {
	_tprintf_s(_T("Lista de utilizadores:\n"));
	for (int i = 0; i < u->n_utilizadores; i++) {
		_tprintf_s(_T("* Nome: %s, Passe: %s, Saldo: %.2f;\n"),
			u->clientes[i].nome_cliente, u->clientes[i].passe, u->clientes[i].saldo);
	}
}

DWORD WINAPI simularVariacaoPrecos(DATA_FUNCOES* d) {
	BOLSA* bolsa = d->b;
	HANDLE hM = d->hMutex;
	while (d->continua) {
		for (int i = 0; i < bolsa->n_acoes_bolsa; i++) {
			int variacao = rand() % 21 - 10;
			WaitForSingleObject(hM, INFINITE);
			bolsa->acoes[i].preco += variacao;
			if (bolsa->acoes[i].preco <= 0)
				bolsa->acoes[i].preco = 1;
			ReleaseMutex(hM);

		}
		//listarAcoes(bolsa);
		Sleep(3000);
	}
	return 0;
}

void redefinirPreco(ACAO* novo, BOLSA* bolsa) {
	for (int i = 0; i < bolsa->n_acoes_bolsa; i++) {
		if (_tcscmp(bolsa->acoes[i].nome, novo->nome) == 0) {
			bolsa->acoes[i].preco = novo->preco;
			return;
		}
	}
	_tprintf(_T("A��o n�o encontrada.\n"));
}

void lerAcoesDoFicheiro(const TCHAR* nomeFicheiro, BOLSA* bolsa) {
	FILE* file;
	_tfopen_s(&file, nomeFicheiro, _T("r"));
	if (file == NULL) {
		_tprintf(_T("Erro ao abrir o ficheiro!\n"));
		exit(1);
	}

	while (_ftscanf_s(file, _T("%19s %d %d"), bolsa->acoes[bolsa->n_acoes_bolsa].nome, (unsigned)_countof(bolsa->acoes[bolsa->n_acoes_bolsa].nome), &bolsa->acoes[bolsa->n_acoes_bolsa].preco, &bolsa->acoes[bolsa->n_acoes_bolsa].quantidade) != EOF) {
		(bolsa->n_acoes_bolsa)++;
	}
	fclose(file);
}

void lerUtilizadoresDoFicheiro(const TCHAR* nomeFicheiro, UTILIZADORES* utilizadores) {
	FILE* file;
	_tfopen_s(&file, nomeFicheiro, _T("r"));
	if (file == NULL) {
		_tprintf(_T("Erro ao abrir o ficheiro!\n"));
		exit(1);
	}

	while (_ftscanf_s(file, _T("%s %4s %lf"),
		utilizadores->clientes[utilizadores->n_utilizadores].nome_cliente,
		(unsigned)_countof(utilizadores->clientes[utilizadores->n_utilizadores].nome_cliente),
		utilizadores->clientes[utilizadores->n_utilizadores].passe,
		(unsigned)_countof(utilizadores->clientes[utilizadores->n_utilizadores].passe),
		&utilizadores->clientes[utilizadores->n_utilizadores].saldo) != EOF) {

		(utilizadores->n_utilizadores)++;
	}
	fclose(file);
}

DWORD WINAPI funcaoCliente(DATA_THR_CLIENTE* data) {
	BOLSA* bolsa = data->bolsa;
	UTILIZADORES* utilizadores = data->utilizadores;
	CLIENTE atual;
	_tcscpy_s(atual.nome_cliente, MAX_NOME, _T("x"));

	HANDLE hPipe;
	TCHAR buffer[BUFFER_SIZE];
	DWORD bytesRead;

	while (true) {
		TCHAR response[BUFFER_SIZE] = _T("Mensagem Recebida!!");
		DWORD bytesWritten;

		// Cria o named pipe
		hPipe = CreateNamedPipe(
			PIPE_NAME,               // Nome do pipe
			PIPE_ACCESS_DUPLEX,      // Pipe de leitura e escrita
			PIPE_TYPE_MESSAGE |      // Mensagens
			PIPE_READMODE_MESSAGE |  // Modo de leitura de mensagens
			PIPE_WAIT,               // Bloqueia at� que um cliente se conecte
			1,                       // N�mero m�ximo de inst�ncias
			BUFFER_SIZE,             // Tamanho do buffer de sa�da
			BUFFER_SIZE,             // Tamanho do buffer de entrada
			0,                       // Timeout (0 � o padr�o)
			NULL);                   // Atributos de seguran�a (NULL usa o padr�o)

		if (hPipe == INVALID_HANDLE_VALUE) {
			_tprintf(_T("Erro ao criar o pipe. C�digo do erro: %d\n"), GetLastError());
			return 1;
		}

		_tprintf(_T("Aguardando conex�o do cliente...\n"));

		// Aguarda a conex�o de um cliente
		BOOL isConnected = ConnectNamedPipe(hPipe, NULL) ? TRUE : (GetLastError() == ERROR_PIPE_CONNECTED);

		if (!isConnected) {
			_tprintf(_T("Erro ao conectar ao cliente. C�digo do erro: %d\n"), GetLastError());
			CloseHandle(hPipe);
			return 1;
		}

		_tprintf(_T("Cliente conectado. Aguardando dados...\n"));

		// Loop de comunica��o com o cliente
		BOOL isRunning = TRUE;
		while (isRunning) {
			// L� dados do pipe
			BOOL isSuccess = ReadFile(hPipe, buffer, BUFFER_SIZE * sizeof(TCHAR), &bytesRead, NULL);
			if (!isSuccess || bytesRead == 0) {
				_tprintf(_T("Erro ao ler do pipe. C�digo do erro: %d\n"), GetLastError());
				break;
			}

			// Null-termina a string recebida
			buffer[bytesRead / sizeof(TCHAR)] = '\0';
			_tprintf(_T("Dados recebidos: %s\n"), buffer);

			if (_tcscmp(buffer, _T("exit")) == 0)
			{
				CloseHandle(hPipe);
				break;
			}
			else if (_tcscmp(buffer, _T("balance")) == 0 && _tcscmp(atual.nome_cliente, _T("x")) != 0)
			{
				_tprintf(_T("\nSaldo dispon�vel: %f"), atual.saldo);
			}
			else
			{
				TCHAR comando[10];
				TCHAR arg1[50], arg2[50];
				sscanf_s(buffer, _T("%s %s %s"), comando, _countof(comando), arg1, _countof(arg1), arg2, _countof(arg2));

				if (_tcscmp(comando, _T("login")) == 0) {
					for (int i = 0; i < utilizadores->n_utilizadores; i++)
					{
						if (_tcscmp(arg1, utilizadores->clientes[i].nome_cliente) == 0 && _tcscmp(arg2, utilizadores->clientes[i].passe) == 0)
						{
							strcpy_s(atual.nome_cliente, MAX_NOME, utilizadores->clientes[i].nome_cliente);
							atual.saldo = utilizadores->clientes[i].saldo;
							_tprintf(_T("\nBem-vindo! Login feito com sucesso!\n"));
						}
					}
				}
				else if (_tcscmp(comando, _T("buy")) == 0) {
					ACAO comprar;
					strcpy_s(comprar.nome, MAX_NOME, arg1);
					comprar.quantidade = arg2;
					comprarAcao(comprar, &bolsa, &atual);
				}
				else if (_tcscmp(comando, _T("sell")) == 0) {
					ACAO vender;
					strcpy_s(vender.nome, MAX_NOME, arg1);
					vender.quantidade = arg2;
					venderAcao(vender, &bolsa, &atual);
				}
				else {
					_tprintf(_T("Comando desconhecido.\n"));
				}
			}

			// Envia uma resposta de volta ao cliente
			isSuccess = WriteFile(hPipe, response, (DWORD)((_tcslen(response) + 1) * sizeof(TCHAR)), &bytesWritten, NULL);
			if (!isSuccess) {
				_tprintf(_T("Erro ao enviar resposta. C�digo do erro: %d\n"), GetLastError());
				break;
			}

			_tprintf(_T("Resposta enviada: %s\n"), response);
		}

		// Fecha o handle do pipe ap�s a comunica��o
		CloseHandle(hPipe);
	}
}

DWORD WINAPI mostrarBolsaNoBoard(DATA_THREAD_BOARD* d) {
	BOLSA* bolsa = d->bolsa;
	while (d->continua) {
		WaitForSingleObject(d->hEv_atualizaBoard, INFINITE);
		TCHAR bufferAcao[BUFFER_SIZE];
		TCHAR* lista = (TCHAR*)malloc(BUFFER_SIZE * sizeof(TCHAR));
		if (lista == NULL) {
			_tprintf(_T("Erro de aloca��o de mem�ria!\n"));
			return NULL;
		}

		// Inicializa a lista com a string "Lista de a��es:\n"
		_sntprintf_s(lista, BUFFER_SIZE, _TRUNCATE, _T("Lista de a��es:\n"));

		// Concatena as informa��es de cada a��o na lista
		for (int i = 0; i < bolsa->n_acoes_bolsa; i++) {
			_sntprintf_s(bufferAcao, BUFFER_SIZE, _T("* Nome: %s, Pre�o: %d, N� de a��es restantes: %d;\n"),
				bolsa->acoes[i].nome, bolsa->acoes[i].preco, bolsa->acoes[i].quantidade);
			_tcscat_s(lista, BUFFER_SIZE, bufferAcao);
		}

		// Escrever na mem�ria compartilhada
		strncpy_s(d->lpMapAddress, wcslen(lista) * sizeof(TCHAR), lista, _TRUNCATE);
		SetEvent(d->hEv_avisaThrBoard);

		free(lista);
	}
}

int _tmain() {
	// Inicializa��o da semente para n�meros aleat�rios
	srand((unsigned int)time(NULL));

	// Configura��es para suportar UTF-8
	_tsetlocale(LC_ALL, _T(""));
	SetConsoleOutputCP(CP_UTF8);
	SetConsoleCP(CP_UTF8);

	//dados da bolsa
	BOLSA bolsa;
	ACAO acoes[MAX_ACOES];
	bolsa.acoes = acoes;
	bolsa.n_acoes_bolsa = 0;

	lerAcoesDoFicheiro(_T("acoes.txt"), &bolsa);
	listarAcoes(&bolsa);


	//dados dos utilizadores permitidos
	UTILIZADORES utilizadores;
	CLIENTE clientes[MAX_CLIENTES];
	utilizadores.clientes = clientes;
	utilizadores.n_utilizadores = 0;

	lerUtilizadoresDoFicheiro(_T("utilizadores.txt"), &utilizadores);
	listarUtilizadores(&utilizadores);

	///////outros dados
	HANDLE hMutex, hEv_atualizaBoard, hEv_avisaThrBoard, hThr_variarPrAcoes, hThr_board, hThr_cliente, hMapFile, hPipe;
	TCHAR* lpMapAddress;

	hMutex = CreateMutex(NULL, FALSE, NULL);
	if (hMutex == NULL) {
		_tprintf(_T("Erro ao criar o mutex!\n"));
		return 1;
	}
	hEv_atualizaBoard = CreateEvent(NULL, FALSE, FALSE, EVENT_NAME);
	if (hEv_atualizaBoard == NULL) {
		_tprintf(_T("Erro ao criar o evento: %d\n", GetLastError()));
		return 1;
	}
	hEv_avisaThrBoard = CreateEvent(NULL, FALSE, FALSE, EVENT_BOARD);
	if (hEv_avisaThrBoard == NULL) {
		_tprintf(_T("Erro ao criar o evento: %d\n", GetLastError()));
		return 1;
	}

	//usado para alterar os valores das a��es na bolsa
	DATA_FUNCOES d_variar;
	d_variar.b = &bolsa;
	d_variar.hMutex = &hMutex;
	d_variar.hEv_atualizaBoard = &hEv_atualizaBoard;
	d_variar.continua = TRUE;

	hThr_variarPrAcoes = CreateThread(NULL, 0, simularVariacaoPrecos, &d_variar, 0, NULL);
	if (hThr_variarPrAcoes == NULL) {
		_tprintf(_T("Erro ao criar a thread!\n"));
		CloseHandle(hMutex);
		return 1;
	}


	// Criar ou abrir o mapeamento de arquivo
	hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, BUFFER_SIZE, FILE_MAP_NAME);
	if (hMapFile == NULL) {
		_tprintf(_T("Erro ao criar o mapeamento de arquivo: %d\n", GetLastError()));
		return 1;
	}

	// Mapear a mem�ria compartilhada para um ponteiro
	lpMapAddress = MapViewOfFile(hMapFile, FILE_MAP_WRITE, 0, 0, BUFFER_SIZE);
	if (lpMapAddress == NULL) {
		_tprintf(_T("Erro ao mapear a mem�ria compartilhada: %d\n", GetLastError()));
		CloseHandle(hMapFile);
		return 1;
	}

	// dados para a thread que controla amostragem da bolsa no board
	DATA_THREAD_BOARD d_board;
	d_board.bolsa = &bolsa;
	d_board.hEv_atualizaBoard = &hEv_atualizaBoard;
	d_board.lpMapAddress = &lpMapAddress;
	d_board.hEv_avisaThrBoard = &hEv_avisaThrBoard;
	d_board.continua = TRUE;

	hThr_board = CreateThread(NULL, 0, mostrarBolsaNoBoard, &d_board, 0, NULL);
	if (hThr_board == NULL) {
		_tprintf(_T("Erro ao criar a thread!\n"));
		CloseHandle(hMutex);
		return 1;
	}

	DATA_THR_CLIENTE d_cliente;
	d_cliente.bolsa = &bolsa;
	d_cliente.utilizadores = &utilizadores;

	hThr_cliente = CreateThread(NULL, 0, funcaoCliente, &d_cliente, 0, NULL);


	TCHAR buffer[BUFFER_SIZE];

	while (TRUE) {
		_tprintf(_T("Digite um comando: "));
		_tscanf_s(_T("%s"), &buffer, (unsigned)_countof(buffer));

		size_t len = _tcslen(buffer);
		if (len > 0 && buffer[len - 1] == _T('\n')) {
			buffer[len - 1] = _T('\0');
		}
		_tprintf(_T("\n%s\n"), buffer);

		TCHAR comando[20];

		sscanf_s(buffer, _T("%s"), comando, (unsigned)_countof(comando));

		_tprintf("\ncomando: %s\n", comando);


		if (_tcscmp(comando, _T("addc")) == 0) {
			ACAO nova;
			_tscanf_s(buffer, _T("addc %s %d %d"), &nova.nome, &nova.preco, &nova.quantidade);
			WaitForSingleObject(hMutex, INFINITE);
			adicionarAcao(&nova, &bolsa);
			ReleaseMutex(hMutex);
			SetEvent(hEv_atualizaBoard);
		}
		else if (_tcscmp(comando, _T("listc")) == 0) {
			SetEvent(hEv_atualizaBoard);
		}
		else if (_tcscmp(comando, _T("users")) == 0) {
			listarUtilizadores(&utilizadores);
		}
		else if (_tcscmp(comando, _T("stock")) == 0) {
			ACAO novo;
			_tscanf_s(buffer, _T("stock %s %d"), &novo.nome, &novo.preco);
			WaitForSingleObject(hMutex, INFINITE);
			redefinirPreco(&novo, &bolsa);
			ReleaseMutex(hMutex);
			SetEvent(hEv_atualizaBoard);
		}
		else if (_tcscmp(comando, _T("pause")) == 0) {
			int segundos;
			_tscanf_s(buffer, _T("pause %d"), &segundos);
			Sleep(segundos * 1000);
		}
		else if (_tcscmp(comando, _T("close")) == 0) {
			break;
		}
		else {
			_tprintf(_T("Comando desconhecido.\n"));
		}
		memset(buffer, 0, sizeof(buffer));
	}

	// Sinalizar threads para terminarem
	d_variar.continua = false;
	d_board.continua = false;

	// Aguardar as threads terminarem
	WaitForSingleObject(hThr_variarPrAcoes, INFINITE);
	WaitForSingleObject(hThr_board, INFINITE);

	// Limpar recursos
	UnmapViewOfFile(lpMapAddress);
	CloseHandle(hMapFile);
	CloseHandle(hEv_atualizaBoard);
	CloseHandle(hEv_avisaThrBoard);
	CloseHandle(hMutex);

	CloseHandle(hThr_variarPrAcoes);
	CloseHandle(hThr_board);

	return 0;
}
