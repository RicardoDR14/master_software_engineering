-----FICHA1 TP----
--ex1
SELECT * FROM EMP;

--ex2
select NOME, FUNCAO, DATA_ENTRADA, NDEP from EMP;

--ex3
select NOME, NDEP, SAL from EMP
where SAL between 1000 and 2000;

--ex4
select NOME from EMP
order by DATA_ENTRADA desc;

--ex4 
select distinct LOCAL from DEP;

-----FICHA2 TP----
--ex1
select NOME, SAL, PREMIOS from EMP
where PREMIOS >= 0;

--ex2
select * from EMP
where NDEP in (20, 30);

--ex3
select NOME from EMP
where (NDEP = 10 or NDEP = 20)
and FUNCAO = 'contabilista';

--ex4
select NOME, FUNCAO from EMP
where (NOME like '%n%' or NOME like '%t%');

--ex5
select NOME, FUNCAO from EMP
where (NOME like 'B%en%' and NOME like 'M%ri%');

--ex6
select NOME, FUNCAO, SAL+PREMIOS REMUNERACAO from EMP;

--ex7
select NOME, (12*SAL*0.15) as "15%_SALARIO_ANUAL" from EMP
where FUNCAO = 'gestor'
and PREMIOS < (12*SAL*0.15)
order by "15%_SALARIO_ANUAL",NOME;

--ex8
select NOME from EMP
where NOME like 'Maria%';

--ex9
select NOME, DATA_ENTRADA from EMP
where FUNCAO = 'presidente';

--ex10
select NOME from EMP
where EMP.NDEP = DEP.NDEP
and DEP.NOME = 'Contabilidade';