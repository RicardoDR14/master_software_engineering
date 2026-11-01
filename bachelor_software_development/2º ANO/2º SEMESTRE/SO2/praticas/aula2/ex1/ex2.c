#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>

#define TAM 200

int _tmain(int argc, TCHAR* argv[]) {
    HKEY chave = 0;
    TCHAR chave_nome[TAM], par_nome[TAM], par_valor[TAM];

    /* ... Mais variáveis ... */
    BOOL continua = TRUE;
    DWORD opc, estado;
    TCHAR caminho[TAM] = _T("Software\\Aula\\");
    LSTATUS res;

#ifdef UNICODE 
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif

    /* ... as várias alíneas ...*/

    while (continua)
    {
        //MENÚ
        _tprintf_s(_T(":::::MENÚ:::::\n1 - Adicionar Key\n2 - Apagar Key\n3 - Adicionar Valor\n4 - Procurar Valor\n5 - Apagar Valor\n....\n9 - Sair;\nOpção: "));
        _tscanf_s(_T("%u"), &opc);

        switch (opc)
        {
        case 1:
            //CRIAR UMA KEY
            //PEDIR AO UTILIZADOR UMA CHAVE
            //ACRESCENTAR A CHAVE AO CAMINHO...
            _tprintf_s(_T("Nome da chave: "));
            _tscanf_s(_T("%s"), &chave_nome,TAM);
            _tcscat_s(caminho, TAM, chave_nome);
            res = RegCreateKeyEx(HKEY_CURRENT_USER, caminho,0,NULL, REG_OPTION_NON_VOLATILE,KEY_ALL_ACCESS,NULL,&chave,&estado);
            if (res == ERROR_SUCCESS) {
                if (estado == REG_OPENED_EXISTING_KEY)
                    _tprintf(_T("Abri a chave '%s' \n"), caminho);
                else if (estado == REG_CREATED_NEW_KEY)
                    _tprintf(_T("Criei a chave '%s' \n"), caminho);
            }
            break;
        case 2:
            //APAGAR UMA KEY
            //PEDIR AO UTILIZADOR UMA CHAVE
            //ACRESCENTAR A CHAVE AO CAMINHO...
            _tccpy_s(caminho, TAM, _T("Software\\Aula\\"));
            _tprintf_s(_T("Nome da chave: "));
            _tscanf_s(_T("%s"), &chave_nome, TAM);
            _tcscat_s(caminho, TAM, chave_nome);
            res = RegDeleteKeyEx(HKEY_CURRENT_USER,caminho,KEY_ALL_ACCESS,0);
            //if (res == ERROR_SUCCESS) {
            //    if (estado == REG_)
            //        _tprintf(_T("Abri a chave '%s' \n"), caminho);
            //    else if (estado == REG_CREATED_NEW_KEY)
            //        _tprintf(_T("Criei a chave '%s' \n"), caminho);
            //}
            break;
        case 3:
            //ADICIONAR VALOR
            //PEDIR AO UTILIZADOR NOME E VALOR DO PAR
            if (chave != 0) {
                _tprintf_s(_T("Nome PAR: "));
                _tscanf_s(_T("%s"), &par_nome, TAM);
                _tprintf_s(_T("Valor PAR: "));
                _tscanf_s(_T("%s"), &par_valor, TAM);
                res = RegSetValueEx(HKEY_CURRENT_USER,par_nome,0,REG_SZ,par_valor,(_tcslen(par_valor)+1)*sizeof(TCHAR));
            }
            break;
        case 4:
            //PROCURAR VALOR
            //PEDIR AO UTILIZADOR NOME DO PAR
            if (chave != 0) {
                _tprintf_s(_T("Nome PAR: "));
                _tscanf_s(_T("%s"), &par_nome, TAM);
                DWORD par_tipo, tam =  sizeof(par_valor);
                res = RegQueryValueEx(HKEY_CURRENT_USER, par_nome, NULL, &par_tipo,(LPBYTE) par_valor, &tam);
                if (res == ERROR_SUCCESS)
                    _tprintf_s(_T("\nPAR: %s - %s (%u)\n"), par_nome, par_valor, par_tipo);
            }
            break;
        case 5:
            break;
        case 9:
            if (chave != 0)
                RegCloseKey(chave);
            continua = FALSE;

        default:
            break;
        }
    }

    return 0;
}