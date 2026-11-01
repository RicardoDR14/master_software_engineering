#include <iostream>
#include <string>

using namespace std;

/*int main() {

    char pausa;
    while (true){
        string palavra, inversa;
        cout << "\npalavra -> ";
        cin >> palavra;
        if (palavra=="fim")
            break;
        inversa = palavra;
        for (int i = 0; i < palavra.length();i++)
            inversa[palavra.length()-i-1] = palavra[i];
        cout << "\ninversa -> " << inversa;
        if (inversa == palavra)
            cout << "\nPalindromo";
        cin >> pausa;
    }

    return 0;
}
*/

int main(){

    string palavras[11] = {"zero", "um", "dois", "tres", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez"};
    string numeros[11] = {"0","1","2","3","4","5","6","7","8","9","10"};
    string n;

    cout << "\nintroduza um numeros:";
    cin >> n;

    for (int i = 0; i < 11; i++){
        if (n==numeros[i])
            cout << palavras[i];
    }

    for (int i = 0; i < 11; i++){
        if (n==palavras[i])
            cout << numeros[i];
    }

    return 0;
}