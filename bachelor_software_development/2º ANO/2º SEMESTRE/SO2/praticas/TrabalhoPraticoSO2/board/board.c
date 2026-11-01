#include <stdio.h>
#include <windows.h>

#define FILE_MAP_NAME "SharedMemory"
#define EVENT_BOARD "AvisaThrBoard"
#define BUFFER_SIZE 2048

int main() {
	// Abrir o mapeamento de arquivo existente
	HANDLE hMapFile = OpenFileMapping(FILE_MAP_READ, FALSE, FILE_MAP_NAME);

	if (hMapFile == NULL) {
		printf("Erro ao abrir o mapeamento de arquivo: %d\n", GetLastError());
		return 1;
	}

	// Mapear a mem�ria compartilhada para um ponteiro
	LPVOID lpMapAddress = MapViewOfFile(hMapFile, FILE_MAP_READ, 0, 0, BUFFER_SIZE);

	if (lpMapAddress == NULL) {
		printf("Erro ao mapear a mem�ria compartilhada: %d\n", GetLastError());
		CloseHandle(hMapFile);
		return 1;
	}

	// Criar ou abrir o evento de sincroniza��o
	HANDLE hEvent = OpenEvent(SYNCHRONIZE, FALSE, EVENT_BOARD);

	if (hEvent == NULL) {
		printf("Erro ao abrir o evento: %d\n", GetLastError());
		CloseHandle(hMapFile);
		return 1;
	}

	// Loop para ler apenas quando o escritor sinalizar que os dados mudaram
	while (1) {
		// Aguardar o evento ser sinalizado pelo escritor
		WaitForSingleObject(hEvent, INFINITE);

		// Ler da mem�ria compartilhada
		char* pData = (char*)lpMapAddress;
		printf("Dados lidos da mem�ria compartilhada: %s\n", pData);

		// Limpar o evento
		ResetEvent(hEvent);
	}

	// Limpar recursos (o loop infinito nunca chegar� aqui)
	UnmapViewOfFile(lpMapAddress);
	CloseHandle(hMapFile);
	CloseHandle(hEvent);

	return 0;
}