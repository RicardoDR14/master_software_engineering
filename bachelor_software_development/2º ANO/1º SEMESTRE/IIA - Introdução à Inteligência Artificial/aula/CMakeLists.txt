cmake_minimum_required(VERSION 3.26)
project(aula C)

set(CMAKE_C_STANDARD 23)

add_executable(aula main.c
        algoritmo.c
        funcao.c
        utils.c)
