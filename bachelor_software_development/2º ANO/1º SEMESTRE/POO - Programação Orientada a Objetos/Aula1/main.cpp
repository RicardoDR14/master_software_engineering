#include <iostream>
#include <cstdio>
#include <cmath>
using namespace std;

//******* exercicio 2a ********
int main_2a() {
    char nome[20];
    int idade;
    printf("\nNome: ");
    scanf("%s", nome);
    printf("\nIdade: ");
    scanf("%d", & idade);
    printf("\n%s tem %d anos\n", nome, idade);
    return 0;
}

//******* exercicio 2b ********
int main_2b(){
    char nome[30];
    int idade;
    cout << "\nnome: ";
    cin >> nome;
    cout << "\nidade: ";
    cin >> idade;
    cout << "\nNome: " << nome << ", Idade: " << idade;
    return 0;
};

//******* exercicio 2c ********
#include "limits"
int main_2c(){
    char nome[100];
    int idade;
    cout << "\nnome: ";
    cin.getline(nome,100);
    do{
        cout << "\nidade: ";
        cin >> idade;
        if (cin.fail()){
            cout << "*\n";
            idade= -1;
            cin.clear();
            //cin.ignore(); consome e deita fora apenas um caracter
            cin.ignore(numeric_limits<streamsize>::max(),'\n');
        }
    }while (idade<=0 && floor(idade) == idade);
    cout << "\nNome: " << nome << ", Idade: " << idade;
    return 0;
};

int main() {
    //main_2a();
    //main_2b();
    main_2c();
    return 0;
}
