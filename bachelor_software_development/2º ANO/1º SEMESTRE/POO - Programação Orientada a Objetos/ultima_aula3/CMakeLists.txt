cmake_minimum_required(VERSION 3.26)
project(ultima_aula3)

set(CMAKE_CXX_STANDARD 23)

add_executable(ultima_aula3 main.cpp
        imovel.cpp
        imovel.h
        apartamento.cpp
        apartamento.h
        loja.cpp
        loja.h
        imobiliaria.cpp
        imobiliaria.h)
