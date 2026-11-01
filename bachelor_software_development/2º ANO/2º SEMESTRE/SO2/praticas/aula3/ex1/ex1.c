#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>

#define TAM 200

typedef struct {
    DWORD soma;
    DWORD min;
    DWORD max;
}TDATA;

BOOL primo(DWORD num) {
    DWORD d;
    for (d = 2; d < num; d++) {
        if (num % d == 0)
            return FALSE;
    }
    return TRUE;
}

DWORD WINAPI somaPares(LPVOID data){
    TDATA *d = (TDATA *)(data);
    int pares_somados = 0;
    for (int i = 1; i <= 1000; i++) {
        if (i % 2 == 0) {
            d->soma += i;
            pares_somados++;
        }
        if (pares_somados % 200 == 0)
            Sleep(1000);
    }
}

DWORD WINAPI somaPrimos(LPVOID data) {
    TDATA* d = (TDATA*)(data);
    int primos_somados = 0;
    for (int i = 1; i <= 1000; i++) {
        if (primo(i)) {
            d->soma += i;
            primos_somados++;
        }
        if (primos_somados % 15 == 0)
            Sleep(1000);
    }
}

int _tmain(int argc, TCHAR* argv[]) {

#ifdef UNICODE 
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif


    HANDLE hThread[2];
    TDATA  tData[2];
    DWORD tId[2];
    DWORD res;
    DWORD num = 5, pos;

    for (int i = 0; i < 5; i++) {
        tData[i].soma = 0; tData[i].min = i * 200; tData[i].max = (i+1) * 200-1;
        hThread[i] = CreateThread(NULL, 0, somaPares, &tData[i], 0, &tId[i]);
    }

    DWORD data[5] = { 0,1,2,3,4 };

    while (num > 0) {
        res = WaitForMultipleObjects(num, hThread, FALSE, INFINITE);
        if (res == WAIT_TIMEOUT)
            _tprintf_s(_T("NINGUEM TERMINOU.....\n"));

        else if (res >= WAIT_OBJECT_0 && res <= (WAIT_OBJECT_0 + num - 1)) {
            pos = res - WAIT_OBJECT_0;
            _tprintf_s(_T("TERMINOU A THREAD %d\n"),pos);
            _tprintf_s(_T("Soma thread %d = %d\n"),pos, tData[pos].soma);
        }
        for (int i = pos; i < num - 1; i++) {
            hThread[i] = hThread[i + 1];
            data[i] = data[i + 1];
        }
        num--;
    }

    return 0;
}