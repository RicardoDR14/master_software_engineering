#include <windows.h>
#include <tchar.h>
#include <math.h>

#include <stdio.h>
#include <fcntl.h> 
#include <io.h>


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
	unsigned int inicio;
	unsigned int fim;
	unsigned int* n_multi_tres;
	CRITICAL_SECTION* cs;
} TDados;

// função da(s) thread(s)
DWORD WINAPI encontraMultiTres(LPVOID data) {
	TDados* d = (TDados*)(data);
	_tprintf_s(_T("Vou contar de %d a %d\n"), d->inicio, d->fim);
	for (int i = d->inicio; i <= d->fim; i++) {
		if (i % 3 == 0) {
			EnterCriticalSection(d->cs);
			(*(d->n_multi_tres))++;
			LeaveCriticalSection(d->cs);
		}
	}
}

// número * máximo * de threads
// podem (e devem) ser menos
#define MAX_THREADS 20





int _tmain(int argc, TCHAR* argv[]) {

	// criação de um critical section
	CRITICAL_SECTION cs;
	InitializeCriticalSectionAndSpinCount(&cs,0);

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

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif 

	initClock();
	_tprintf_s(TEXT("\nLimite sup. -> "));
	_tscanf_s(TEXT("%u"), &limsup);
	_tprintf_s(TEXT("\nNum threads -> "));
	_tscanf_s(TEXT("%u"), &numthreads);
	if (numthreads > MAX_THREADS)
		numthreads = MAX_THREADS;

	

	for (int i = 0; i < numthreads; i++) {
		tdados[i].n_multi_tres = &soma;
		tdados[i].inicio = i * (limsup / numthreads);
		tdados[i].fim = (i + 1) * (limsup / numthreads) - 1;
		tdados[i].cs = &cs;
		hThreads[i] = CreateThread(NULL, 0, encontraMultiTres, &tdados[i], CREATE_SUSPENDED, NULL);
	}

	// Aguardar por uma tecla
	_tprintf_s(TEXT("\nCarregue numa tecla para começar!!!\n"));
	_gettch();

	for (int i = 0; i < numthreads; i++)
		ResumeThread(hThreads[i]);


	// FAZER prepara e cria threads
	//       manda as threads começar

	clockticks = startClock();

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

	DeleteCriticalSection(&cs);

	return 0;
}
