#include <windows.h>
#include <stdio.h>
#include <conio.h>
#include <tchar.h>
#include <process.h>
#include <fcntl.h> 
#include <io.h>

#define SHM_NAME TEXT("SHM_PC")           // nome da memoria partilhada
#define MUTEX_NAME TEXT("MUTEX")          // nome do mutex   -> em casa devem pensar numa solução para ter mutex´s distintos de forma a não existir perda de performance 
#define SEM_WRITE_NAME TEXT("SEM_WRITE")  // nome do semaforo de escrita
#define SEM_READ_NAME TEXT("SEM_READ")    // nome do semaforo de leitura
#define BUFFER_SIZE 10

typedef struct _BufferCell {
	unsigned int id; //id 
	unsigned  val; // valor 
} BufferCell;

typedef struct _SharedMem {
	unsigned int p;   // contador partilhado com o numero de produtores  
	unsigned int c;   // contador partilhado com o numero de consumidores   
	unsigned int wP;  // posicao do buffer circular para a escrita     
	unsigned int rP;  // posicao do buffer circular para a escrita  
	BufferCell buffer[BUFFER_SIZE]; // buffer circular
} SharedMem;

typedef struct _ControlData {
	unsigned int shutdown;  // flag "continua". 0 = continua, 1 = deve terminar
	unsigned int id;        // id do processo  
	unsigned int count;     // contador do numero de vezes  
	unsigned int sum;       // somatorio de todos os proc consumidores 
	HANDLE hMapFile;        // ficheiro de memoria 
	SharedMem* sharedMem;   // memoria partilhada
	HANDLE hMutex;          // mutex - trabalho de casa -> acrescentar os outros 2 mutexes
	HANDLE hWriteSem;       // sem�foro "aguarda por items escritos"
	HANDLE hReadSem;        // sem�foro "aguarda por posições vazias"
} ControlData;

BOOL initMemAndSync(ControlData* cdata) {
	BOOL firstProcess = FALSE;
	cdata->hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, FALSE, SHM_NAME);
	if (cdata->hMapFile == NULL) {
		firstProcess = TRUE;
		_tprintf(_T("FileMapping does not exist, trying to create one.\n"));
		cdata->hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE, NULL, PAGE_READWRITE, 0, sizeof(SharedMem), SHM_NAME);
		if (cdata->hMapFile == NULL) {
			_tprintf(_T("Error: CreateFileMapping (%d)"), GetLastError());
			return FALSE;
		}
	}
	cdata->sharedMem = (SharedMem*)MapViewOfFile(cdata->hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, BUFFER_SIZE);
	if (cdata->sharedMem == NULL) {
		_tprintf(_T("Error: MapViewOfFile (%d)"), GetLastError());
		CloseHandle(cdata->hMapFile);
		return FALSE;
	}
	if (firstProcess) {
		cdata->sharedMem->c = 0;
		cdata->sharedMem->p = 0;
		cdata->sharedMem->rP = 0;
		cdata->sharedMem->wP = 0;
	}
	cdata->hMutex = CreateMutex(NULL, FALSE, MUTEX_NAME);
	if (cdata->hMutex == NULL) {
		_tprintf(_T("Error: CreateMutex (%d)"), GetLastError());
		UnmapViewOfFile(cdata->sharedMem);
		CloseHandle(cdata->hMapFile);
		return FALSE;
	}
	cdata->hReadSem = CreateSemaphore(NULL, 0, BUFFER_SIZE, SEM_READ_NAME);
	if (cdata->hReadSem == NULL) {
		_tprintf(_T("Error: CreateSemaphore (%d)"), GetLastError());
		UnmapViewOfFile(cdata->sharedMem);
		CloseHandle(cdata->hMapFile);
		CloseHandle(cdata->hMutex);
		return FALSE;
	}
	cdata->hWriteSem = CreateSemaphore(NULL, BUFFER_SIZE, BUFFER_SIZE, SEM_WRITE_NAME);
	if (cdata->hWriteSem == NULL) {
		_tprintf(_T("Error: CreateSemaphore (%d)"), GetLastError());
		UnmapViewOfFile(cdata->sharedMem);
		CloseHandle(cdata->hMapFile);
		CloseHandle(cdata->hMutex);
		CloseHandle(cdata->hReadSem);
		return FALSE;
	}
	return TRUE;
}

//thread que vai estar a consultar o buffer circular
DWORD WINAPI consume(LPVOID p)
{
	ControlData* cdata = (ControlData*)p;
	BufferCell cell;
	int ranTime;
	cell.id = cdata->id;
	while (1)
	{
		if (cdata->shutdown)
			return 0;
		WaitForSingleObject(cdata->hMutex, INFINITE);
		WaitForSingleObject(cdata->hReadSem, INFINITE);

		CopyMemory(&(cdata->sharedMem->buffer[(cdata->sharedMem->rP)++]), &cell, sizeof(BufferCell));
		if (cdata->sharedMem->rP == BUFFER_SIZE)
			cdata->sharedMem->rP = 0;

		ReleaseMutex(cdata->hMutex);
		ReleaseSemaphore(cdata->hWriteSem, 1, NULL);

		_tprintf(_T("C%d consumiu %d do %d.\n"), cdata->id, cell.val, cell.id);
		ranTime = rand() % 2 + 2;
		Sleep(ranTime * 1000);
		cdata->count++;
		cdata->sum += cell.id;
	}

	return 0;

}

int _tmain(int argc, TCHAR* argv[]) {
	ControlData cdata;
	HANDLE hThread;
	TCHAR command[100];
#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);    // *** stdin  ***  
	_setmode(_fileno(stdout), _O_WTEXT);   // *** stdout ***
	_setmode(_fileno(stderr), _O_WTEXT);   // *** stderr ***
#endif

	cdata.shutdown = 0;
	cdata.count = 0;
	cdata.sum = 0;

	//inicializar
	if (!initMemAndSync(&cdata)) {
		_tprintf(TEXT("Error creating/opening shared memory and synchronization mechanisms.\n"));
		exit(1);
	}

	WaitForSingleObject(cdata.hMutex, INFINITE);

	cdata.count++;

	ReleaseMutex(cdata.hMutex);

	hThread = CreateThread(NULL, 0, consume, &cdata, 0, NULL);

	_tprintf(TEXT("Type in 'exit' to leave.\n"));

	do {
		_getts_s(command, 100);
	} while (_tcscmp(command, TEXT("exit")) != 0);

	cdata.shutdown = 1; //flag para terminar a thread

	WaitForSingleObject(hThread, INFINITE); //espera que a thread termine

	_tprintf(TEXT("C%d consumed %d items, summing a total of %d.\n"), cdata.id, cdata.count, cdata.sum);

	CloseHandle(hThread);
	CloseHandle(cdata.hMapFile);
	CloseHandle(cdata.hMutex);
	CloseHandle(cdata.hReadSem);

	return 0;
}