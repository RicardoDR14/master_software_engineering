#include <iostream>
#include <string>

using namespace std;

class Automovel{
    string mat, marca;
    int cil;
public:
    Automovel(string m, string mr, int c):mat(m),marca(mr),cil(c){}

    const Automovel & operator=(const Automovel & outro){
        if(this == &outro)
            return *this;
        marca=outro.marca;
        mat = outro.mat;
        return *this;
    }
};

int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}
