#include <iostream>
#include <memory>
#include "Ponto.h"

using namespace std;

int main() {

    //a)
    /*  // zona inicial
    weak_ptr<Ponto> spw;
    { // bloco B1
        unique_ptr<Ponto> ponto = make_unique<Ponto> (1,2);
        ponto->mostra();
    }
    { // bloco B2
        shared_ptr<Ponto> sps1 = make_shared<Ponto> (3,4);
        sps1->mostra();
        spw = sps1;

        shared_ptr<Ponto> sps2 = sps1;
        sps2->mostra();
    }
    { // bloco B3
        shared_ptr<Ponto> sps_tmp = spw.lock();
        if (sps_tmp){
            sps_tmp->mostra();
        }
    }
*/
    //b)
    /*int i=0;

    unique_ptr<Ponto> u[10];
    for (auto & p:u)
        p= make_unique<Ponto>(1,i++);

    i=0;
    shared_ptr<Ponto> s[10];
    for (auto & p:s)
        p= make_shared<Ponto>(2,i++);

    i=0;
    weak_ptr<Ponto> w[10];
    for (auto & p:w)
        p= s[i++];*/

    //c)
    unique_ptr<Ponto> u1,u2;
    shared_ptr<Ponto> s1,s2;
    weak_ptr<Ponto> w1,w2;

    u1 = make_unique<Ponto>(1,1);
    u2 = make_unique<Ponto>(1,2);
    s1 = make_shared<Ponto>(2,1);
    s2 = make_shared<Ponto>(2,2);
    w1 = s1;
    w2 = s2;

    u1=move(u2);

    s1=s2;

    w1=w2;
}