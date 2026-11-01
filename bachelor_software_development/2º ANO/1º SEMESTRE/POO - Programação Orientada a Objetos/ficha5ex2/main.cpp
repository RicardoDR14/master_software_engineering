#include <iostream>

using namespace std;

class Vetor{
    double dCx, dCy;
public:
    Vetor() : dCx(0.0), dCy(0.0) {}

    Vetor(double Cx, double Cy) : dCx(Cx), dCy(Cy) {}
    Vetor(double c) : dCx(c), dCy(c) {}

    double getX() const {return dCx;}
    double getY() const {return dCy;}

    double setX(double nx) {return dCx = nx;}
    double setY(double ny) {return dCy = ny;}

    string descricao() const {
        return "(" + to_string(dCx) + "," + to_string(dCy) + ")";
    };

    Vetor & operator+=( const Vetor &v){
        dCx+=v.dCx;
        dCy+=v.dCy;
        return *this;
    }
};

Vetor operator+(const Vetor&a, const Vetor&b){
    return Vetor(a.getX() + a.getY() + b.getX() + b.getY());
}

Vetor operator-(const Vetor&a, const Vetor&b){
    return Vetor(a.getX() + a.getY() + b.getX() + b.getY());
}

bool operator==(const Vetor&a, const Vetor&b){
    return (a.getX() == b.getX()) && (a.getY() == b.getY());
}

bool operator!=(const Vetor&a, const Vetor&b){
    return !(a==b);
}

ostream&operator << (ostream&saida, Vetor v){
    saida << v.descricao();
    return saida;
}

int main() {
    Vetor v1(2.0, 1.0), v2(1.0, 3.0), v3(2.2), z;
    z = v1 + v2 + v3;
    cout << v1 << "+" << v2 << "+" << v3 << "=" << z << endl; // obs: "(x,y)"
    z = v1 + 10.0;
    cout << v1 << " + " << " 10 = " << z << endl;
    z = 20.0 + v1;
    cout << "20 + " << v1 << " = " << z << endl;
    z = v1 - v2;
    cout << v1 << " - " << v2 << " = " << z << endl;
    Vetor a(1.0, 1.0), b(2.0, 4.0);
    cout << " a= " << a << " b= " << b << endl;
    a += b += v1;
    a += b;
    a += 10.0;
    cout << " a= " << a << endl;
    cout << "(a == b)? " << (a == b) << endl;
    cout << "(a != b)? " << (a != b) << endl;
}