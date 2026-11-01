--FICHA3 TP----
--ex1
select NOME from EMP
where SAL > 2000;

--ex2
select NEMP as n_chefe, NOME as nome_chefe from EMP e1
where FUNCAO = 'chefe'
union
select NEMP as n_funcionario, NOME as nome_funcionario from EMP e2
join EMP e1 on e2.ENCAR = e1.n_chefe;

--ex3
select NOME, FUNCAO, DEP.NOME from EMP
left join DEP on EMP.NDEP = DEP.NDEP
order by DEP.NOME, NOME desc;

--ex4
select NOME, SAL, NDEP from EMP
where NOME like 'C%' 
and NOME like '% M%'
and SAL < 1000;

--ex5
select NOME, DATA_ENTRADA, DEP.NOME, DEP.LOCAL from EMP
left join DEP on EMP.NDEP = DEP.NDEP
where SAL > 1500;

--ex6
select ESCALAO from DESCONTOS
join EMP on EMP.SAL between DESCONTOS.SALINF and DESCONTOS.SALSUP
order by ESCALAO, EMP.FUNCAO;

--ex7
select NOME, FUNCAO, SAL, DESCONTOS.SALINF, DESCONTOS.SALSUP from EMP
join DESCONTOS on EMP.SAL between DESCONTOS.SALINF and DESCONTOS.SALSUP
where ESCALAO = 4 or ESCALAO = 5
order by NOME;

--ex8
select NOME, FUNCAO, SAL, DESCONTOS.SALINF, DESCONTOS.SALSUP, DEP.NOME from EMP
join DEP on EMP.NDEP = DEP.NDEP
join DESCONTOS on EMP.SAL between DESCONTOS.SALINF and DESCONTOS.SALSUP
where ESCALAO = 4 or ESCALAO = 5
order by NOME;

--ex9
select NOME, FUNCAO, SAL, DEP.LOCAL from EMP
join DEP on EMP.NDEP = DEP.NDEP
where (DEP.LOCAL = 'Coimbra' or DEP.LOCAL = 'Porto')
and SAL > 5000;

--ex10
select NOME, FUNCAO, DESCONTOS.ESCALAO, DEP.NOME from EMP
join DEP on EMP.NDEP = DEP.NDEP
join DESCONTOS on EMP.SAL between DESCONTOS.SALINF and DESCONTOS.SALSUP
where FUNCAO in ('vendedor','gestor')
order by DESCONTOS.ESCALAO desc;

--ex11
select NOME, FUNCAO, DEP.NOME from EMP
join DEP on EMP.NDEP = DEP.NDEP
union all
select NULL, NULL, DEP.NOME from DEP
where DEP.NDEP not in (select NDEP from EMP)
order by DEP.NOME, NOME desc;


