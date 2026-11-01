#ifndef TRABALHOPRATICOCODIGO_APARELHO_H
#define TRABALHOPRATICOCODIGO_APARELHO_H

#include <iostream>

using namespace std;

class Aparelho {
private:
    int aparelho_id;
    bool isligado;
    int temporizador;
public:
    Aparelho(int _id);
    int getId() const;
    virtual string getNome() const = 0;
    bool getEstado() const;
    void setEstado();
    int getTemporizador() const;
    int incTemporizador();
};

class Aquecedor : public Aparelho {
public:
    Aquecedor(int _id);

    string getNome() const override;
};

class Aspersor : public Aparelho {
public:
    Aspersor(int _id);

    string getNome() const override;
};

class Refrigerador : public Aparelho {
public:
    Refrigerador(int _id);

    string getNome() const override;
};

class Lampada : public Aparelho {
public:
    Lampada(int _id);

    string getNome() const override;
};

#endif //TRABALHOPRATICOCODIGO_APARELHO_H


