#define _CRT_SECURE_NO_WARNINGS 1

#include <stdio.h>
#include <stdlib.h>
#include "algoritmo.h"
#include "utils.h"

// Preenche uma estrutura com os progenitores da próxima geração, de acordo com o resultados do torneio binario (tamanho de torneio: 2)
// Parâmetros de entrada: população actual (pop), estrutura com parâmetros (d) e população de pais a encher
void tournament(pchrom pop, struct info d, pchrom parents) {
    int i, x1, x2;

    // Realiza popsize torneios
    for (i = 0; i < d.popsize; i++) {
        x1 = random_l_h(0, d.popsize - 1);
        do
            x2 = random_l_h(0, d.popsize - 1);
        while (x1 == x2);
        if (pop[x1].fitness > pop[x2].fitness)        // Problema de maximizacao
            parents[i] = pop[x1];
        else
            parents[i] = pop[x2];
    }
}

void tournament2(pchrom pop, struct info d, pchrom parents) {
    int i, new_x, contador = 0, to_discart = 0;
    int tournament_size = d.tsize;
    int x[tournament_size];
    float this_fitness = 0, best_fitness = 0;

    // Realiza popsize torneios
    for (i = 0; i < d.popsize; i++) {
        x[0] = random_l_h(0, d.popsize - 1);
        best_fitness = pop[x[0]].fitness;

        while (contador < tournament_size) {
            to_discart = 0;
            new_x = random_l_h(0, d.popsize - 1);

            //ver se o ponto já está no torneio
            for (int j = 0; j <= contador; ++j)
                if (x[j] == new_x)
                    to_discart = 1;

            if (to_discart == 0) {
                x[contador-1] = new_x;
                this_fitness = pop[new_x].fitness;

                if (this_fitness > best_fitness) {//problema de maximização
                    parents[i] = pop[new_x];
                    best_fitness = this_fitness;
                }
                contador++;
            }
        }
    }
}

// Operadores geneticos a usar na geração dos filhos
// Parâmetros de entrada: estrutura com os pais (parents), estrutura com parâmetros (d), estrutura que guardará os descendentes (offspring)
void genetic_operators(pchrom parents, struct info d, pchrom offspring) {
    // Recombinação com um ponto de corte
    crossover(parents, d, offspring);
    // Mutação binária
    mutation(offspring, d);
}

// Preenche o vector descendentes com o resultado das operações de recombinação
// Parâmetros de entrada: estrutura com os pais (parents), estrutura com parâmetros (d), estrutura que guardará os descendentes (offspring)
void crossover(pchrom parents, struct info d, pchrom offspring) {
    int i, j, point;

    for (i = 0; i < d.popsize; i += 2) {
        if (rand_01() < d.pr) {
            point = random_l_h(0, d.numGenes - 1);
            for (j = 0; j < point; j++) {
                offspring[i].p[j] = parents[i].p[j];
                offspring[i + 1].p[j] = parents[i + 1].p[j];
            }
            for (j = point; j < d.numGenes; j++) {
                offspring[i].p[j] = parents[i + 1].p[j];
                offspring[i + 1].p[j] = parents[i].p[j];
            }
        } else {
            offspring[i] = parents[i];
            offspring[i + 1] = parents[i + 1];
        }
    }
}

//o mesmo com dois pontos de corte
void crossover2(pchrom parents, struct info d, pchrom offspring) {
    int i, j, point1, point2, dummy;

    for (i = 0; i < d.popsize; i += 2) {
        if (rand_01() < d.pr) {
            point1 = random_l_h(0, d.numGenes - 1);
            point2 = random_l_h(0, d.numGenes - 1);
            while (point1 == point2) {//enquanto forem iguais, gera um novo
                point2 = random_l_h(0, d.numGenes - 1);
            }
            if (point2 < point1) {//ter a certeza que o  1 é primeiro que o 2
                dummy = point1;
                point1 = point2;
                point2 = dummy;
            }
            //inicializa os filhos iguais aos pais
            offspring[i].p[j] = parents[i].p[j];
            offspring[i + 1].p[j] = parents[i + 1].p[j];
            //altera a parte central
            for (j = point1; j < point2; j++) {
                offspring[i].p[j] = parents[i].p[j];
                offspring[i + 1].p[j] = parents[i + 1].p[j];
            }
        } else {
            offspring[i] = parents[i];
            offspring[i + 1] = parents[i + 1];
        }
    }
}

//recombinação uniforme
void crossover3(pchrom parents, struct info d, pchrom offspring) {
    int i, j;

    for (i = 0; i < d.popsize; i += 2) {
        //inicializa os filhos iguais aos pais
        offspring[i].p[j] = parents[i].p[j];
        offspring[i + 1].p[j] = parents[i + 1].p[j];
        //percorre as posições dos pais
        for (j = 0; j < d.numGenes; j++) {
            if (parents[i].p[j] != parents[i + 1].p[j]) {//se os pais forem diferentes
                if (rand_01() < d.pr) {//pr é a probabilidade de recombinação
                    offspring[i + 1].p[j] = parents[i].p[j];
                    offspring[i].p[j] = parents[i + 1].p[j];
                }
            }
        }
    }
}

// Mutação binária com vários pontos de mutação
// Parâmetros de entrada: estrutura com os descendentes (offspring) e estrutura com parâmetros (d)
void mutation(pchrom offspring, struct info d) {
    int i, j;

    for (i = 0; i < d.popsize; i++)
        for (j = 0; j < d.numGenes; j++)
            if (rand_01() < d.pm)
                offspring[i].p[j] = !(offspring[i].p[j]);
}

//mutação por troca
void mutation2(pchrom offspring, struct info d) {
    int i, obj1, obj2;

    for (i = 0; i < d.popsize; i++) {
        if (rand_01() < d.pm) {
            //encontra posição aleatória com o valor 1
            do {
                obj1 = random_l_h(0, d.numGenes - 1);
            } while (offspring[i].p[obj1] != 1);
            //encontra posição aleatória com o valor 0
            do {
                obj2 = random_l_h(0, d.numGenes - 1);
            } while (offspring[i].p[obj2] != 1);
            //troca-os
            offspring[i].p[obj1] = 0;
            offspring[i].p[obj2] = 1;
        }
    }
}

float calcula_peso(int sol[], struct info d, int mat[][2]) {
    int i;
    float sum_weight = 0;

    for (i = 0; i < d.numGenes; i++) {
        // Verifica se o objecto i esta na mochila
        if (sol[i] == 1) {
            // Actualiza o peso total
            sum_weight += mat[i][0];
        }
    }
    return sum_weight;
}

void repair(pchrom pop, struct info d, int mat[][2]) {
    int i, j, object_to_remove;
    float current_w, minimo_v;

    for (i = 0; i < d.popsize; i++) {
        current_w = calcula_peso(pop[i].p, d, mat);

        while (current_w > d.capacity) {
            /*//encontra posição aleatória em b com valor 1
            do {
                //devolve valor inteiro em entre 0 e d.numGenes
                object_to_remove= random_l_h(0,d.numGenes-1);
            }while(pop[i].p[object_to_remove]!=1);*/

            //encontrar posição do menor valor
            object_to_remove = -1;
            minimo_v = 100;
            for (j = 0; j < d.numGenes; j++) {
                if (pop[i].p[j] == 1) {
                    if (mat[j][1] < minimo_v) {
                        minimo_v = mat[j][1];
                        object_to_remove = j;
                    }
                }
            }

            //remove objeto
            pop[i].p[object_to_remove] = 0;
            //recalcula peso
            current_w = calcula_peso(pop[i].p, d, mat);
        }
    }
}

void gera_vizinho(int a[], int b[], int n){
    int i, p1;

    for (i = 0; i<n;i++)
        b[i]=a[i];

    p1= random_l_h(0,n-1);
    b[p1]= !b[p1];
}

void substitui(int a[], int b[], int tam){
    int i;
    for (i = 0;i  < tam; i++)
        a[i]=b[i];
}

float trepa_colinas(int sol[], int mat[][2], struct info d, int num_inter){
    int *nova_sol, i, dummy;
    float custo, custo_viz;

    nova_sol = malloc(sizeof (int)*d.numGenes);
    if(nova_sol == NULL){
        printf("Erro na alocacao de memoria");
        exit(1);
    }
    //avalia a situção inicial do custo
    custo = eval_individual(sol, d, mat, &dummy);

    for(i = 0; i < num_inter; i++){

        //gera vizinho
        gera_vizinho(sol, nova_sol, d.numGenes);
        //avalia vizinho
        custo_viz = eval_individual(nova_sol, d, mat, &dummy);
        //aceita vizinho se custo for melhor (problema de maximização)
        if(custo_viz >= custo){
            substitui(sol , nova_sol, d.numGenes);
            custo = custo_viz;
        }
    }
    free(nova_sol);
    return custo;
}