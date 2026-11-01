------ficha5 TP ---------

--ex1
select nome, min(sal), max(sal), avg(sal), avg(sal)-min(sal)-max(sal)
from emp
group by nome;

--ex2
select dep.nome, max(sal)-min(sal)
from emp, dep
where emp.ndep=dep.ndep
group by dep.nome
order by dep.nome, max(sal)-min(sal) desc;

--ex3
select funcao, count(funcao), min(sal), max(sal)
from emp
where funcao in (select funcao from emp)
group by funcao
order by funcao;

--ex4
select funcao, count(funcao), min(sal), max(sal)
from emp
where upper(funcao) = 'ANALISTA'
or nome like '%a'
group by funcao
order by funcao;

--ex5
select encar, min(sal), max(sal)
from emp
where encar in (select distinct encar from emp)
group by encar
having min(sal) >= 900
order by max(sal);

--ex6
select ceil(avg(sal)), funcao
from emp
group by funcao
having count(funcao)>2
order by ceil(avg(sal));

--ex7
select ndep, count(funcao), max(sal), min(sal), avg(sal)
from emp
where nome not like 'António Casta'
group by ndep, funcao
having max(sal)>1000
order by ndep, max(sal);

--ex8
select (select count(nemp) from emp),
       (select count(nemp) from emp,dep where emp.ndep=dep.ndep and dep.nome='VENDAS')
from dual;

--ex9
select (select avg(sal) from emp),
       (select count(nemp)from emp)
from dual;

--ex10
select max(sal)
from emp
where funcao not like 'PRESIDENTE';

--ex11 ??????
select dep.nome,
       avg(sal) 
from emp,dep 
where emp.ndep=dep.ndep 
and dep.nome='MARKETING' 
or dep.nome='PUBLICIDADE'
group by dep.nome;

--ex12
select dep.nome, count(emp.nemp)
from emp, dep
where dep.ndep = emp.ndep
group by dep.nome;

--ex13
select sum(sal), sum(premios), dep.nome
from emp, dep
where dep.ndep=emp.ndep
group by dep.nome;

--ex14
select d.nome
from dep d, emp e
where d.ndep = e.ndep
group by d.nome
having sum(e.sal) = (select min(soma)
                     from (select sum(sal) as soma
                           from emp
                           group by ndep)
                    );
                    
--ex15
select nome
from emp
where data_entrada = (select min(data_entrada) from emp);