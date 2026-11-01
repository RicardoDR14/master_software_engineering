#include <stdio.h>
#include <stdlib.h>
#include "algoritmo.h"
#include "funcao.h"
#include "utils.h"
#include <math.h>

// Gera um vizinho
// Parametros: solucao actual, vizinho, numero de vertices
//swap two vertices
void gera_vizinho(int a[], int b[], int n)
{
    int i, p1, p2;

    for(i=0; i<n; i++)
        b[i]=a[i];
	// Encontra posicao com valor 0
    do
        p1=random_l_h(0, n-1);
    while(b[p1] != 0);
	// Encontra posicao com valor 0
    do
        p2=random_l_h(0, n-1);
    while(b[p2] != 1);
	// Troca
    b[p1] = 1;
    b[p2] = 0;
    //duplicado
    do
        p1=random_l_h(0, n-1);
    while(b[p1] != 0);
    // Encontra posicao com valor 0
    do
        p2=random_l_h(0, n-1);
    while(b[p2] != 1);
    // Troca
    b[p1] = 1;
    b[p2] = 0;
}

// Trepa colinas first-choice
// TREPA COLINAS PROBABILISTICO (adicionamos o float input_prob nos parametros)
// Parametros: solucao, matriz de adjacencias, numero de vertices e numero de iteracoes
// Devolve o custo da melhor solucao encontrada
int trepa_colinas(int sol[], int *mat, int vert, int num_iter, float input_prob)
{
    int *nova_sol, custo, custo_viz, i;
    float prob;

	nova_sol = malloc(sizeof(int)*vert);
    if(nova_sol == NULL)
    {
        printf("Erro na alocacao de memoria");
        exit(1);
    }
	// Avalia solucao inicial
    custo = calcula_fit(sol, mat, vert);
    for(i=0; i<num_iter; i++)
    {
		// Gera vizinho
		gera_vizinho(sol, nova_sol, vert);
		// Avalia vizinho
		custo_viz = calcula_fit(nova_sol, mat, vert);

        prob = rand_01();//trepacolinasprobabilistico
		// Aceita vizinho se o custo diminuir (problema de minimizacao)
        if(custo_viz <= custo || prob < input_prob)
        {
			substitui(sol, nova_sol, vert);
			custo = custo_viz;
        }
    }
    free(nova_sol);
    return custo;
}

int recristalizacao_simulada(int sol[], int *mat, int vert, int num_iter,int TMax, int TMin, int alpha){

    int *nova_sol, custo, custo_viz, i, num_iter_final;
    double t;
    float prob, delta_c,expression;

    t=TMax;

    nova_sol = malloc(sizeof(int)*vert);
    if(nova_sol == NULL)
    {
        printf("Erro na alocacao de memoria");
        exit(1);
    }
    // Avalia solucao inicial
    custo = calcula_fit(sol, mat, vert);
    for(i=0; i<num_iter; i++) {
        num_iter_final = i;
        // Gera vizinho
        gera_vizinho(sol, nova_sol, vert);
        // Avalia vizinho
        custo_viz = calcula_fit(nova_sol, mat, vert);

        delta_c = custo_viz - custo;

        if (delta_c <= 0) {
            substitui(sol, nova_sol, vert);
            custo = custo_viz;
        } else {
            prob = rand_01();
            expression = exp(-delta_c / t);

            if (prob <= expression) {
                substitui(sol, nova_sol, vert);
                custo = custo_viz;
            }
        }

        t = t * alpha;

        if (t < TMin) break;
    }
    free(nova_sol);

    num_iter_final=num_iter_final+i;
        printf("\nIteracoes = %d\n",num_iter_final);
    return custo;
}