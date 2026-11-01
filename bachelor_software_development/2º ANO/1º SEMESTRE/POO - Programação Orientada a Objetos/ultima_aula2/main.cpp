#include <iostream>
#include "ponto.h"
#include <vector>

int main() {
    //a)   Ponto * a = new Ponto(1,2);Ponto * b = new Ponto(3,4); delete &a;delete&b;
    //b)   Ponto p[] = {Ponto (1, 2), Ponto(3, 4), Ponto(5, 6)};
    //c)
    int n,i=0;
    vector <Ponto> p;
    cout<<"quantos pontos? : ";
    cin>>n;
    while(i<5){
        int x,y;
        cout << "\nx: ";
        cin >>x;
        cout << "\ny: ";
        cin >>y;
        p.emplace_back(x,y);
        i++;
    }


    cout<<"\nfinal\n";
    return 0;
}
