cmake_minimum_required(VERSION 3.26)
project(TrabalhoPraticoCodigo)

set(CMAKE_CXX_STANDARD 20)

add_executable(TrabalhoPraticoCodigo
        aparelho.h
        aparelho.cpp
        sensor.h
        sensor.cpp
        Processador.h
        Processador.cpp
        Regra.h
        Regra.cpp
        zona.h
        zona.cpp
        curses.h
        Terminal.cpp
        Terminal.h
        main.cpp
        comandos.cpp
        comandos.h
        modelo.cpp
        modelo.h
        iteracao.cpp
        iteracao.h
)
target_link_libraries(${PROJECT_NAME} ${PROJECT_SOURCE_DIR}/pdcurses.a)