#include <windows.h>
#include <tchar.h>
#include <math.h>

#include <stdio.h>
#include <fcntl.h> 
#include <io.h>

// definir bloco
#define BLOCO 10000


// funcionalidade relacionada com temporização

static double PerfCounterFreq; // n ticks por seg.

void initClock() {
	LARGE_INTEGER aux;
	if (!QueryPerformanceFrequency(&aux))
		_tprintf_s(TEXT("\nSorry - No can do em QueryPerfFreq\n"));
	PerfCounterFreq = (double)(aux.QuadPart); // / 1000.0;
	_tprintf_s(TEXT("\nTicks por sec.%f\n"), PerfCounterFreq);
}

__int64 startClock() {
	LARGE_INTEGER aux;
	QueryPerformanceCounter(&aux);
	return aux.QuadPart;
}

double stopClock(__int64 from) {
	LARGE_INTEGER aux;
	QueryPerformanceCounter(&aux);
	_tprintf_s(_T("Ticks - %lld\n"), aux.QuadPart - from);
	return (double)(aux.QuadPart - from) / PerfCounterFreq;
}

// estrutura de dados para controlar as threads

typedef struct {
	unsigned int* numBloco;
	unsigned int* soma;
	BOOL* continua;
	CRITICAL_SECTION* cs;
	HANDLE* hEv;
} TDados;

// função da(s) thread(s)
DWORD WINAPI threadContar(LPVOID data) {
	TDados* d = (TDados*)(data);
	DWORD i;
	unsigned int bloco, inicio, fim;


	while (d->continua)
	{
		EnterCriticalSection(d->cs);
		bloco = *(d->numBloco);
		(*(d->numBloco))++;
		LeaveCriticalSection(d->cs);

		inicio = bloco * BLOCO + 1;
		fim = (bloco + 1) * BLOCO;

		_tprintf_s(_T("Vou contar de %d a %d\n"), inicio, fim);
		for (int i = inicio; i <= fim; i++) {
			if (i % 3 == 0) {
				EnterCriticalSection(d->cs);
				(*(d->soma))++;
				LeaveCriticalSection(d->cs);
			}
		}
		
		SetEvent(d->hEv);
	}
}

DWORD WINAPI threadTeclado() {

}

// número * máximo * de threads
// podem (e devem) ser menos
#define MAX_THREADS 20



int _tmain(int argc, TCHAR* argv[]) {

	// criação de uma critical section
	CRITICAL_SECTION cs;
	InitializeCriticalSectionAndSpinCount(&cs, 100);

	// criação de um evento
	HANDLE hEv = CreateEvent(NULL, FALSE, FALSE, NULL);

	// matriz de handles das threads
	HANDLE hThreads[MAX_THREADS];

	// Matriz de dados para as threads;
	TDados tdados[MAX_THREADS];

	// número efectivo de threads
	int numthreads;

	// limite superior
	unsigned int limsup;

	// variáveis para cronómetro
	__int64 clockticks;
	double duracao;

	unsigned int range;
	unsigned int inter;
	unsigned int soma = 0;//guarda o nº de multiplos de 3

	unsigned int numBloco=0;
	BOOL continua = TRUE;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif 

	initClock();
	//_tprintf_s(TEXT("\nLimite sup. -> "));
	//_tscanf_s(TEXT("%u"), &limsup);
	_tprintf_s(TEXT("\nNum threads -> "));
	_tscanf_s(TEXT("%u"), &numthreads);
	if (numthreads > MAX_THREADS)
		numthreads = MAX_THREADS;



	for (int i = 0; i < numthreads; i++) {
		tdados[i].soma = &soma;
		tdados[i].numBloco = &numBloco;
		tdados[i].continua = &continua;
		tdados[i].cs = &cs;
		tdados[i].hEv = &hEv;
		hThreads[i] = CreateThread(NULL, 0, threadContar, &tdados[i], CREATE_SUSPENDED, NULL);
	}

	// Aguardar por uma tecla
	_tprintf_s(TEXT("\nCarregue numa tecla para começar!!!\n"));
	_gettch();

	for (int i = 0; i < numthreads; i++)
		ResumeThread(hThreads[i]);



	// FAZER prepara e cria threads
	//       manda as threads começar

	clockticks = startClock();

	while (continua)
	{
		WaitForSingleObject(hEv,INFINITE);
		_tprintf_s(_T("* "));
	}

	// FAZER aguarda / controla as threads 
	//       manda as threads parar

	DWORD res;
	res = WaitForMultipleObjects(numthreads, hThreads, TRUE, INFINITE);

	if (res == WAIT_OBJECT_0) {
		// FAZER apresenta resultados
		duracao = stopClock(clockticks);
		_tprintf_s(TEXT("\nSegundos=%f\n"), duracao);
	}

	_tprintf_s(_T("Soma : %d\n"), soma);

	CloseHandle(hEv);
	DeleteCriticalSection(&cs);

	return 0;
}
