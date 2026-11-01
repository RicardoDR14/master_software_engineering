cmake_minimum_required(VERSION 3.26)
project(ultima_aula4)

set(CMAKE_CXX_STANDARD 23)

add_executable(ultima_aula4 main.cpp
        CD.cpp
        CD.h
        Classica.cpp
        Classica.h
        BandaSonora.cpp
        BandaSonora.h
        ColecaoCD.cpp
        ColecaoCD.h)
