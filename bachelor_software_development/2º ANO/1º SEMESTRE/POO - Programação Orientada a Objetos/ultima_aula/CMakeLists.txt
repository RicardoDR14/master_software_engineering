cmake_minimum_required(VERSION 3.26)
project(ultima_aula)

set(CMAKE_CXX_STANDARD 23)

add_executable(ultima_aula main.cpp
        pessoa.h
        pessoa.cpp
        RegCivil_v.cpp
        RegCivil_v.h
        RegCivil_s.cpp
        RegCivil_s.h
        RegCivil_m.cpp
        RegCivil_m.h
)
