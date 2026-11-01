#include <windows.h>
#include <stdio.h>
#include <tchar.h>

#define PIPE_NAME _T("\\\\.\\pipe\\bolsa_pipe")
#define BUFFER_SIZE 1024

int main() {
	HANDLE hPipe;
	TCHAR buffer[BUFFER_SIZE];
	TCHAR response[BUFFER_SIZE];
	DWORD bytesWritten, bytesRead;

	while (TRUE) {
		hPipe = CreateFile(PIPE_NAME, GENERIC_READ | GENERIC_WRITE, 0, NULL, OPEN_EXISTING, 0, NULL);

		if (hPipe != INVALID_HANDLE_VALUE) {
			break; // Sucesso, saiu do loop
		}

		// Verifica se o pipe n�o est� dispon�vel
		DWORD dwError = GetLastError();
		if (dwError != ERROR_PIPE_BUSY) {
			_tprintf(_T("Erro ao abrir o pipe. C�digo do erro: %d\n"), dwError);
			return 1;
		}

		// Aguarda at� que o pipe esteja dispon�vel
		if (!WaitNamedPipe(PIPE_NAME, NMPWAIT_WAIT_FOREVER)) {
			_tprintf(_T("Falha ao aguardar pelo pipe. C�digo do erro: %d\n"), GetLastError());
			return 1;
		}
	}

	_tprintf(_T("Conectado ao servidor.\n"));

	BOOL isRunning = TRUE;
	while (isRunning) {
		// Captura a entrada do usu�rio
		_tprintf(_T("Digite a mensagem a ser enviada (ex: login maria 1234): "));
		_fgetts(buffer, BUFFER_SIZE, stdin);

		// Remove o caractere de nova linha (\n) do final da string, se existir
		size_t len = _tcslen(buffer);
		if (len > 0 && buffer[len - 1] == _T('\n')) {
			buffer[len - 1] = _T('\0');
		}

		// Envia dados para o pipe
		BOOL isSuccess = WriteFile(hPipe, buffer, (DWORD)((_tcslen(buffer) + 1) * sizeof(TCHAR)), &bytesWritten, NULL);

		if (!isSuccess) {
			_tprintf(_T("Erro ao escrever no pipe. C�digo do erro: %d\n"), GetLastError());
			break;
		}
		else {
			_tprintf(_T("Dados enviados: %s\n"), buffer);

			// Aguarda a resposta do servidor
			isSuccess = ReadFile(hPipe, response, BUFFER_SIZE * sizeof(TCHAR), &bytesRead, NULL);

			if (!isSuccess || bytesRead == 0) {
				_tprintf(_T("Erro ao ler do pipe. C�digo do erro: %d\n"), GetLastError());
				break;
			}
			else {
				// Null-termina a string recebida
				response[bytesRead / sizeof(TCHAR)] = _T('\0');
				_tprintf(_T("Resposta recebida: %s\n"), response);

				// Verifica se a mensagem recebida � "fim"
				if (_tcscmp(response, _T("fim")) == 0) {
					_tprintf(_T("Servidor solicitou desconex�o. Fechando o cliente.\n"));
					isRunning = FALSE;
				}
			}
		}
	}

	// Fecha o handle do pipe
	CloseHandle(hPipe);

	return 0;
}