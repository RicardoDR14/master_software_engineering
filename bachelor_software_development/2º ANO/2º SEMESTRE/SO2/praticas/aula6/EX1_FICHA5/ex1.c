#include "utils.h"

#define TAM 26

int _tmain(int argc, TCHAR* argv[]) {

	HANDLE hFich, hMap;
	char* pstr;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif

	hFich = CreateFile(_T("exemplo.txt"), GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);

	hMap = CreateFileMapping(hFich, NULL, PAGE_READWRITE, 0, (TAM + 1)* sizeof(char), NULL);

	pstr = (char*)MapViewOfFile(hMap, FILE_MAP_READ|FILE_MAP_WRITE, 0, 0, (TAM + 1)*sizeof(char));

	for (DWORD i = 0; i < TAM/2; i++)
	{
		char aux = pstr[i];
		pstr[i] = pstr[TAM - i - 1];
		pstr[TAM - i - 1]= aux;
	}


	UnmapViewOfFile(pstr);
	CloseHandle(hFich);
	CloseHandle(hMap);
}