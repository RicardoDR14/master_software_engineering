#include "utils.h"

#define TAM 100
#define NOME_SHM _T("memória")
#define NOME_EVENT _T("evento")
#define NOME_MUTEX _T("trinco")



typedef struct {
	HANDLE hMutex, hEv;
	TCHAR* str;
	BOOL continua;
} TDATA;

DWORD WINAPI mostra(LPVOID dados) {

	TDATA* td = (TDATA*)dados;
	TCHAR str[TAM];
	//esperar evento, esperar mutex, ler SHM, libertar mutex, mostrar, sleep
	do
	{
		WaitForSingleObject(td->hEv, INFINITE);
		WaitForSingleObject(td->hMutex, INFINITE);
		CopyMemory(str, td->str, (_tcslen(td->str) + 1) * sizeof(TCHAR));
		ReleaseMutex(td->hMutex);
		_tprintf_s(_T("Li: %s\n"), str);
		Sleep(1000);
	} while (td->continua);
	return 0;
}

int _tmain(int argc, TCHAR* argv[]) {

	HANDLE hMap, hThread;
	TCHAR str[TAM];
	TDATA td;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif


	//criar mutex - trinco
	td.hMutex = CreateMutex(NULL, FALSE, NOME_MUTEX);
	//criar evento - evento
	td.hEv = CreateEvent(NULL, TRUE, FALSE,	NOME_EVENT);

	hMap = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, TAM * sizeof(TCHAR), NOME_SHM);
	td.str = (TCHAR*)MapViewOfFile(hMap, FILE_MAP_READ | FILE_MAP_WRITE, 0, 0, 0);

	td.continua = TRUE;
	//criar thread
	hThread = CreateThread(NULL, 0, mostra, (LPVOID)&td, 0, NULL);

	//ler texto, esperar mutex, escrever SHM, libertar mutex, set evento, sleep 0.5s, reset evento
	do
	{
		_tscanf_s(_T("%s"), str, TAM - 1);
		WaitForSingleObject(td.hMutex, INFINITE);
		CopyMemory(td.str, str, (_tcslen(str) + 1) * sizeof(TCHAR));
		ReleaseMutex(td.hMutex);
		SetEvent(td.hEv);
		Sleep(500);
		ResetEvent(td.hEv);
	} while (_tcscmp(str, _T("fim")) != 0);

	td.continua = FALSE;
	WaitForSingleObject(hThread, INFINITE);
	UnmapViewOfFile(td.str);
	CloseHandle(hMap);
	CloseHandle(td.hEv);
	CloseHandle(td.hMutex);


	return 0;
}