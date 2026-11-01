#include <iostream>
#include <string>

using namespace std;
/*ex2
void imprime(string s){
    cout << s << "\n";
}
void imprime(string s, int h){
    cout << s << h<< "\n";
}
void imprime(int h,string s){
    cout << h << s<< "\n";
}

int main() {
    imprime("programação orientada a objetos");
    imprime("horas por aula teórica ", 2);
    imprime(3," horas em cada aula teórica");
    return 0;
}
*/

//ex3a)
/*
int multiplica(){
    return 1;
}
int multiplica(int i){
    return i;
}
int multiplica(int i, int j){
    return i*j;
}
int multiplica(int i, int j, int k){
    return i*j*k;
}*/

//ex3b)
/*
int multiplica(int i=1,int j=1, int k=1){
    return i*j*k;
}

int main(){
    cout << "\n" << multiplica() << "\n"<<multiplica(5);
    cout << endl << multiplica(2,3) << endl<<multiplica(2,3,4);
    return 0;
}*/

//ex4
/*
void troca(int &a, int &b){
    int aux = a;
    a=b;
    b=aux;
}

int main (){
    int a = 5, b = 10;
    troca(a,b);
    cout << "\na = " << a << "\nb = " << b;
}*/

//ex5
/*
int& seleciona(int &a, int &b, char c){
    if (c=='m'){
        if (a<b) return a;
        return b;
    }
    if (c=='M'){
        if (a<b) return b;
        return a;
    }
    if (c=='p'){
        return a;
    }
    if (c=='u'){
        return b;
    }
    return a;//se estiver errado
}

int main(){
    int a = 5, b = 10;
    seleciona(a, b, 'M') -= 3;
    cout << "a = " << a << " b = " << b; // aparece 0 10
}*/

//ex6
const int TAM = 10;
struct Tabela{
    int matriz[TAM][TAM];
};

Tabela preencher_tabela(Tabela t){
    int a;
    cout << "\na -> ";
    cin >> a;

    for (int i=0;i<TAM;i++)
        for (int j=0;j<TAM;j++)

            t->matriz[i][j]=a;


    return t;
}
void lista_tabela(Tabela t){
    for (int i=0;i<TAM;i++){
        for (int j=0;j<TAM;j++){
            cout << t.matriz[i][j] << " ";
        }
        cout << endl;
    }
}

int main (){
    Tabela t;
    preencher_tabela(t);
    lista_tabela(t);
}