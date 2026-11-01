#define _CRT_SECURE_NO_WARNINGS 1

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "algoritmo.h"
#include "funcao.h"
#include "utils.h"

#define DEFAULT_RUNS    10

int main(int argc, char *argv[]) {
    char nome_fich[100];
    struct info EA_param;
    pchrom pop = NULL, parents = NULL;
    chrom best_run, best_ever;
    int gen_actual, r, runs, i, inv, mat[MAX_OBJ][2];
    int hibrido_flag;//seletor pode ter os valores de 1-ANTES,2-DEPOIS ou 3-DURANTE
    float mbf = 0.0;


    EA_param.popsize = 100;
    EA_param.pm = 0.1;
    EA_param.pr = 0.7;
    EA_param.tsize = 2;
    EA_param.numGenerations = 2500;
    EA_param.numGenes = 0;
    EA_param.ro = 0;




    // Lê os argumentos de entrada
    if (argc == 3) {
        runs = atoi(argv[2]);
        strcpy(nome_fich, argv[1]);
    } else
        // Se o número de execuções do processo não for colocado nos argumentos de entrada, define-o com um valor por defeito
    if (argc == 2) {
        runs = DEFAULT_RUNS;
        strcpy(nome_fich, argv[1]);
    }
        // Se o nome do ficheiro de informações não for colocado nos argumentos de entrada, pede-o novamente
    else {
        runs = DEFAULT_RUNS;
        printf("Nome do Ficheiro: ");
        gets(nome_fich);
    }
    // Se o número de execuções do processo for menor ou igual a 0, termina o programa
    if (runs <= 0)
        return 0;
    //Inicializa a geração dos números aleatórios
    init_rand();
    // Preenche a matriz com dados dos objectos (peso e valor) e a estrutura EA_param que foram definidos no ficheiro de input
    EA_param = init_data(nome_fich, mat);
    // Faz um ciclo com o número de execuções definidas
    for (r = 0; r < runs; r++) {
        printf("Repeticao %d\n", r + 1);
        // Geração da população inicial
        pop = init_pop(EA_param);
        // Avalia a população inicial
        evaluate(pop, EA_param, mat);

        //ANTES
        if(hibrido_flag==1){
            for (i = 0; i < EA_param.popsize; i++) {
                pop[i].fitness = trepa_colinas(pop[i].p,mat,EA_param,1000);
            }
        }

        gen_actual = 1;
        // Como ainda não existe, escolhe-se como melhor solução a primeira da população (poderia ser outra qualquer)
        best_run = pop[0];
        // Encontra-se a melhor solução dentro de toda a população
        best_run = get_best(pop, EA_param, best_run);
        // Reserva espaço para os pais da população seguinte
        parents = malloc(sizeof(chrom) * EA_param.popsize);
        // Caso não consiga fazer a alocação, envia aviso e termina o programa
        if (parents == NULL) {
            printf("Erro na alocacao de memoria\n");
            exit(1);
        }
        // Ciclo de optimização
        while (gen_actual <= EA_param.numGenerations) {
            // Torneio binário para encontrar os progenitores (ficam armazenados no vector parents)
            tournament(pop, EA_param, parents);
            // Aplica os operadores genéticos aos pais (os descendentes ficam armazenados na estrutura pop)
            genetic_operators(parents, EA_param, pop);
            // Avalia a nova população (a dos filhos)
            evaluate(pop, EA_param, mat);

            //DURANTE
            if(hibrido_flag==3){
                for (i = 0; i < EA_param.popsize; i++) {
                    pop[i].fitness = trepa_colinas(pop[i].p,mat,EA_param,100);
                }
            }

            // Actualiza a melhor solução encontrada
            best_run = get_best(pop, EA_param, best_run);
            gen_actual++;
        }

        //DEPOIS
        if(hibrido_flag==2){
            for (i = 0; i < EA_param.popsize; i++) {
                pop[i].fitness = trepa_colinas(pop[i].p,mat,EA_param,1000);
            }
        }

        // Contagem das soluções inválidas
        for (inv = 0, i = 0; i < EA_param.popsize; i++)
            if (pop[i].valido == 0)
                inv++;
        // Escreve resultados da repetição que terminou
        printf("\nRepeticao %d:", r);
        write_best(best_run, EA_param);
        printf("\nPercentagem Invalidos: %f\n", 100 * (float) inv / EA_param.popsize);
        mbf += best_run.fitness;
        if (r == 0 || best_run.fitness > best_ever.fitness)
            best_ever = best_run;
        // Liberta a memória usada
        free(parents);
        free(pop);
    }
    // Escreve resultados globais
    printf("\n\nMBF: %f\n", mbf / r);
    printf("\nMelhor solucao encontrada");
    write_best(best_ever, EA_param);
    return 0;
}
