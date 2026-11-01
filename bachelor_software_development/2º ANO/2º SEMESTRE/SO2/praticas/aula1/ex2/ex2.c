#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

#include <tchar.h>
#include <io.h>
#include <fcntl.h>

#define MAX 256

int _tmain(int argc, TCHAR * argv[]) {
	TCHAR result[MAX] = _TEXT("Olá! Este programa representa UNICODE\n");
	//ouuuuuu
	//TCHAR result[MAX] = _T("Olá! Este programa representa UNICODE\n");

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif // UNICODE

	_tprintf(_T("Frase:%s\n"), result, (int) _tcslen(result));
	_tprintf(_T("Capacidade (letras/bytes/TCHAR) : %d / %d / %d\n"), _countof(result), sizeof(result), sizeof(TCHAR));

	return 0;
}
