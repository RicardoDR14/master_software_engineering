#include <windows.h>
#include <tchar.h>
#include <stdio.h>
#include <io.h>
#include <fcntl.h>

#define PIPE_NAME TEXT("\\\\.\\pipe\\teste")
#define MAX 5

typedef struct {
	HANDLE trinco;
	HANDLE leitor[MAX];
	BOOL continua;
}TDATA;

DWORD WINAPI escreveTudo(LPVOID data) {
	TDATA* d = (TDATA*)data;
	HANDLE hPipe[MAX];
	TCHAR buf[256];
	DWORD n;

	do {
		_tprintf(TEXT("[ESCRITOR] Frase: "));
		_fgetts(buf, 256, stdin);
		buf[_tcslen(buf) - 1] = '\0';
		//pedir mutex
		WaitForSingleObject(d->trinco, INFINITE);
		//copiar a memoria para o array
		CopyMemory(hPipe, d->leitor, INFINITE);
		//libertar mutex
		ReleaseMutex(d->trinco);
		//enviar para todos os leitores (handle do array diferente de NULL) (ciclo)
		for (int i = 0; i < MAX; i++)
		{
			if (!WriteFile(hPipe[i], buf, (DWORD)_tcslen(buf) * sizeof(TCHAR), &n, NULL)) {
				_tprintf(TEXT("[ERRO] Escrever no pipe! (WriteFile)\n"));
				exit(-1);
			}
			_tprintf(_T("[ESCRITOR] Enviei %d bytes ao leitor...' ' (WriteFile)\n"), n);
		}

	} while (_tcscmp(buf, TEXT("fim")));

	//desligar todos os leitores(!=NULL) (ciclo)
	for (int i = 0; i < MAX; i++)
	{
		if (hPipe[i] != NULL) {
			FlushFileBuffers(hPipe[i]);
			_tprintf(TEXT("[ESCRITOR] Desligar o pipe (DisconnectNamedPipe)\n"));

			if (!DisconnectNamedPipe(hPipe[i])) {
				_tprintf(TEXT("[ERRO] Desligar o pipe! (DisconnectNamedPipe)"));
				exit(-1);
			}
			CloseHandle(hPipe);
		}
	}
	d->continua = FALSE;
	//HANDLE hPipe = CreateFile(PIPE_NAME, GENERIC_READ, 0, NULL, );
	CloseHandle(hPipe);


	return 0;
}

int _tmain(int argc, LPTSTR argv[]) {
	DWORD n;
	HANDLE hPipe;
	TCHAR buf[256];

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	//criar array com valores a null
	TDATA data;
	data.continua = TRUE;
	for (int i = 0; i < MAX; i++)
		data.leitor[i] = NULL;

	//cria mutex
	data.trinco = CreateMutex(NULL, FALSE, NULL);

	//criar thread para broadcast
	HANDLE hThread = CreateThread(NULL, 0, escreveTudo, &data, 0, NULL);

	OVERLAPPED ov;
	HANDLE hEv = CreateEvent(NULL, TRUE, FALSE, NULL);

	while (1) {
		_tprintf(TEXT("[ESCRITOR] Criar uma c�pia do pipe '%s' ... (CreateNamedPipe)\n"), PIPE_NAME);
		hPipe = CreateNamedPipe(PIPE_NAME, PIPE_ACCESS_OUTBOUND, PIPE_WAIT |
			PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE, PIPE_UNLIMITED_INSTANCES,
			sizeof(buf), sizeof(buf), 1000, NULL);
		if (hPipe == INVALID_HANDLE_VALUE) {
			_tprintf(TEXT("[ERRO] Criar Named Pipe! (CreateNamedPipe)"));
			exit(-1);
		}
		_tprintf(TEXT("[ESCRITOR] Esperar liga��o de um leitor... (ConnectNamedPipe)\n"));

		ZeroMemory(&ov, sizeof(OVERLAPPED));
		ov.hEvent = hEv;
		BOOL res = ConnectNamedPipe(hPipe, &ov);
		if (res)
		{
			_tprintf(TEXT("Leitor ligado de imediato\n"));
		}
		else {
			if (GetLastError() == ERROR_IO_PENDING) {
				_tprintf(TEXT("FIQUEI À ESPERAD DE UM LEITOR (connect)\n"));
				WaitForSingleObject(hEv, INFINITE);
				GetOverlappedResult(hPipe, &ov, NULL, FALSE);
			}
			else
			{
				_tprintf(TEXT("[ERRO] Liga��o ao leitor! (ConnectNamedPipe\n"));
				exit(-1);

			}
		}

		if (!ConnectNamedPipe(hPipe, &ov)) {
			_tprintf(TEXT("[ERRO] Liga��o ao leitor! (ConnectNamedPipe\n"));
			exit(-1);
		}
		//pedir mutex
		WaitForSingleObject(data.trinco, INFINITE);
		//colocar handle numa posicao vazia
		for (int i = 0; i < MAX; i++)
		{
			if (data.leitor[i] == NULL) {
				data.leitor[i] = hPipe;
				break;
			}
		}
		//libertar mutex
		ReleaseMutex(data.trinco);
		// se nao houver vagas, desliga cliente e remove instancia

	}

	WaitForSingleObject(hThread, INFINITE);
	//eliminar mutex
	//eliminar thread
	CloseHandle(data.trinco);
	CloseHandle(hThread);

	return 0;
}
