

#include "MyString.h"
MyString::MyString() :len(0){}

MyString::MyString(const char *p) {
    if(p== nullptr)
        return;
    len = strlen(p);
    if(len==0)
        return;
    letras= make_unique<char[]>(len);
    strcpy(letras.get(),p);
}

int MyString::length() const {return len;}

const char *MyString::c_str() const {return letras.get();}

