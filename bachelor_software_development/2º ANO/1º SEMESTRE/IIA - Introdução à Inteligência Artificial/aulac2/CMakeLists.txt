cmake_minimum_required(VERSION 3.26)
project(aulac2 C)

set(CMAKE_C_STANDARD 99)

add_executable(aulac2 main.c
        algoritmo.c
        funcao.c
        utils.c
)
