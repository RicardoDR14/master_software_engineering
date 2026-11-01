

#ifndef ULRIMA_AULA6_MYSTRING_H
#define ULRIMA_AULA6_MYSTRING_H

#include <iostream>
#include <memory>
#include <string>
using namespace std;

class MyString {
public:
    MyString();
    MyString(const char*p);
    int length() const;
    const char * c_str() const;
    void concat(const MyString &);
    void clear();
    ~MyString();
    MyString & operator=(const MyString &);
private:
    unique_ptr<char[]> letras;
    int len;
};


#endif //ULRIMA_AULA6_MYSTRING_H
