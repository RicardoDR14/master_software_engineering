#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <windows.h>
#include <tchar.h>
#include <locale.h>

#define _CRT_SECURE_NO_WARNINGS
#define MAX_ACOES 100
#define MAX_NOME 20
#define PIPE_NAME _T("\\\\.\\pipe\\bolsa_pipe")

typedef struct {
    TCHAR nome[MAX_NOME];
    int preco;
    int quantidade;
} Acao;

Acao acoes[MAX_ACOES];
int numAcoes = 0;

HANDLE hMutex;
DWORD dwThreadId;


void adicionarAcao(const TCHAR* nome, int preco, int quantidade) {
    if (numAcoes < MAX_ACOES) {
        _tcscpy_s(acoes[numAcoes].nome, MAX_NOME, nome);
        acoes[numAcoes].preco = preco;
        acoes[numAcoes].quantidade = quantidade;
        numAcoes++;
    }
    else {
        _tprintf(_T("Número máximo de ações atingido.\n"));
    }
}

void listarAcoes(TCHAR* buffer, size_t bufferSize) {
    size_t offset = 0;
    for (int i = 0; i < numAcoes; i++) {
        offset += _tprintf(buffer + offset, bufferSize - offset, _T("%d. %s - Preço: %d - Quantidade disponível: %d\n"), i + 1, acoes[i].nome, acoes[i].preco, acoes[i].quantidade);
        if (offset >= bufferSize) break;
    }
}

void redefinirPreco(const TCHAR* nome, int novoPreco) {
    for (int i = 0; i < numAcoes; i++) {
        if (_tcscmp(acoes[i].nome, nome) == 0) {
            acoes[i].preco = novoPreco;
            return;
        }
    }
    _tprintf(_T("Ação não encontrada.\n"));
}

void lerAcoesDoFicheiro(const TCHAR* nomeFicheiro) {
    FILE* file;
    _tfopen_s(&file, nomeFicheiro, _T("r"));
    if (file == NULL) {
        _tprintf(_T("Erro ao abrir o ficheiro!\n"));
        exit(1);
    }

    while (_ftscanf_s(file, _T("%19s %d %d"), acoes[numAcoes].nome, (unsigned)_countof(acoes[numAcoes].nome), &acoes[numAcoes].preco, &acoes[numAcoes].quantidade) != EOF) {
        numAcoes++;
        if (numAcoes >= MAX_ACOES) {
            _tprintf(_T("Número máximo de ações excedido!\n"));
            break;
        }
    }

    fclose(file);
}

DWORD WINAPI atenderCliente(LPVOID lpParam) {
    HANDLE hPipe = (HANDLE)lpParam;
    TCHAR buffer[1024];
    DWORD bytesRead;

    while (ReadFile(hPipe, buffer, sizeof(buffer) - 1, &bytesRead, NULL)) {
        buffer[bytesRead / sizeof(TCHAR)] = '\0';
        _tprintf(_T("Comando recebido: %s\n"), buffer);

        TCHAR comando[10];
        _tscanf_s(buffer, _T("%s"), comando, (unsigned)_countof(comando));

        if (_tcscmp(comando, _T("addc")) == 0) {
            TCHAR nome[MAX_NOME];
            int preco, quantidade;
            _tscanf_s(buffer, _T("addc %s %d %d"), nome, &preco, &quantidade);
            WaitForSingleObject(hMutex, INFINITE);
            adicionarAcao(nome, preco, quantidade);
            ReleaseMutex(hMutex);
        }
        else if (_tcscmp(comando, _T("listc")) == 0) {
            TCHAR output[1024];
            WaitForSingleObject(hMutex, INFINITE);
            listarAcoes(output, sizeof(output) / sizeof(TCHAR));
            ReleaseMutex(hMutex);
            WriteFile(hPipe, output, (DWORD)(_tcslen(output) * sizeof(TCHAR)), NULL, NULL);
        }
        else if (_tcscmp(comando, _T("stock")) == 0) {
            TCHAR nome[MAX_NOME];
            int novoPreco;
            _tscanf_s(buffer, _T("stock %s %d"), nome, &novoPreco);
            WaitForSingleObject(hMutex, INFINITE);
            redefinirPreco(nome, novoPreco);
            ReleaseMutex(hMutex);
        }
        else if (_tcscmp(comando, _T("pause")) == 0) {
            int segundos;
            _tscanf_s(buffer, _T("pause %d"), &segundos);
            Sleep(segundos * 1000);
        }
        else if (_tcscmp_(comando, _T("close")) == 0) {
            DisconnectNamedPipe(hPipe);
            CloseHandle(hPipe);
            CloseHandle(hMutex);
            exit(0);
        }
        else {
            _tprintf(_T("Comando desconhecido.\n"));
        }
    }

    DisconnectNamedPipe(hPipe);
    CloseHandle(hPipe);
    return 0;
}
/*
void exibirMenu() {
    _tprintf(_T("===== Bolsa de Valores =====\n"));
    _tprintf(_T("Ações disponíveis:\n"));
    for (int i = 0; i < numAcoes; i++) {
        _tprintf(_T("%d. %s - Preço: %d - Quantidade disponível: %d\n"), i + 1, acoes[i].nome, acoes[i].preco, acoes[i].quantidade);
    }
    _tprintf(_T("\n"));
    _tprintf(_T("Escolha uma opção:\n"));
    _tprintf(_T("1. Comprar\n"));
    _tprintf(_T("2. Vender\n"));
    _tprintf(_T("3. Sair\n"));
    _tprintf(_T("-----------------------\n"));
}

// Função para realizar a compra de ações
void comprarAcao(int indice) {
    int quantidade;
    _tprintf(_T("Quantas ações de %s você deseja comprar? "), acoes[indice].nome);
    _tscanf_s(_T("%d"), &quantidade);

    if (quantidade <= 0) {
        _tprintf(_T("Quantidade inválida!\n"));
        return;
    }

    if (quantidade * acoes[indice].preco > 1000000) {
        _tprintf(_T("Operação excede o limite de 1 milhão!\n"));
        return;
    }

    if (quantidade > acoes[indice].quantidade) {
        _tprintf(_T("Quantidade indisponível!\n"));
        return;
    }

    WaitForSingleObject(hMutex, INFINITE);
    acoes[indice].quantidade -= quantidade;
    ReleaseMutex(hMutex);

    _tprintf(_T("Compra de %d ações de %s realizada com sucesso!\n"), quantidade, acoes[indice].nome);
    _tprintf(_T("Total gasto: %d\n"), quantidade * acoes[indice].preco);
}

// Função para realizar a venda de ações
void venderAcao(int indice) {
    int quantidade;
    _tprintf(_T("Quantas ações de %s você deseja vender? "), acoes[indice].nome);
    _tscanf_s(_T("%d"), &quantidade);

    if (quantidade <= 0) {
        _tprintf(_T("Quantidade inválida!\n"));
        return;
    }

    WaitForSingleObject(hMutex, INFINITE);
    acoes[indice].quantidade += quantidade;
    ReleaseMutex(hMutex);

    _tprintf(_T("Venda de %d ações de %s realizada com sucesso!\n"), quantidade, acoes[indice].nome);
    _tprintf(_T("Total ganho: %d\n"), quantidade * acoes[indice].preco);
}
*/
// Função para simular a variação dos preços das ações
DWORD WINAPI simularVariacaoPrecos(LPVOID lpParam) {
    while (true) {
        for (int i = 0; i < numAcoes; i++) {
            int variacao = rand() % 21 - 10;
            WaitForSingleObject(hMutex, INFINITE);
            acoes[i].preco += variacao;
            if (acoes[i].preco <= 0)
                acoes[i].preco = 1;
            ReleaseMutex(hMutex);
        }
        Sleep(3000);
    }
}
/*
// Função para ler ações de um ficheiro
void lerAcoesDoFicheiro(const TCHAR* nomeFicheiro) {
    FILE* file;
    _tfopen_s(&file, nomeFicheiro, _T("r"));
    if (file == NULL) {
        _tprintf(_T("Erro ao abrir o ficheiro!\n"));
        exit(1);
    }

    while (_ftscanf_s(file, _T("%19s %d %d"), acoes[numAcoes].nome, (unsigned)_countof(acoes[numAcoes].nome), &acoes[numAcoes].preco, &acoes[numAcoes].quantidade) != EOF) {
        numAcoes++;
        if (numAcoes >= MAX_ACOES) {
            _tprintf(_T("Número máximo de ações excedido!\n"));
            break;
        }
    }

    fclose(file);
}
*/
int _tmain() {
    srand((unsigned int)time(NULL));

    hMutex = CreateMutex(NULL, FALSE, NULL);
    if (hMutex == NULL) {
        _tprintf(_T("Erro ao criar o mutex!\n"));
        return 1;
    }

    lerAcoesDoFicheiro(_T("Acoes.txt"));

    HANDLE hThread;
    HANDLE hThreadSimulacao;
    DWORD dwThreadIdSimulacao;
    HANDLE hPipe;
    hThreadSimulacao = CreateThread(NULL, 0, simularVariacaoPrecos, NULL, 0, &dwThreadIdSimulacao);

    if (hThreadSimulacao == NULL) {
        _tprintf(_T("Erro ao criar a thread de simulação!\n"));
        CloseHandle(hMutex);
        return 1;
    }

    // Configuração do named pipe
    while (true) {
        _tprintf(_T("Aguardando conexão de cliente...\n"));

        hPipe = CreateNamedPipe(PIPE_NAME, PIPE_ACCESS_DUPLEX, PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE |
            PIPE_WAIT, PIPE_UNLIMITED_INSTANCES, 1024 * sizeof(TCHAR), 1024 * sizeof(TCHAR), 0, NULL);

        if (hPipe == INVALID_HANDLE_VALUE) {
            _tprintf(_T("Erro ao criar o named pipe.\n"));
            return 1;
        }

        BOOL fConnected = ConnectNamedPipe(hPipe, NULL) ?
            TRUE : (GetLastError() == ERROR_PIPE_CONNECTED);

        if (fConnected) {
            _tprintf(_T("Cliente conectado.\n"));

            hThread = CreateThread(NULL, 0, atenderCliente, (LPVOID)hPipe, 0, &dwThreadId);

            if (hThread == NULL) {
                _tprintf(_T("Erro ao criar a thread de atendimento ao cliente.\n"));
                CloseHandle(hPipe);
            }
            else {
                CloseHandle(hThread);
            }
        }
        else {
            CloseHandle(hPipe);
        }
    }

    // Fechando handles
    CloseHandle(hThreadSimulacao);
    CloseHandle(hMutex);

    return 0;
}