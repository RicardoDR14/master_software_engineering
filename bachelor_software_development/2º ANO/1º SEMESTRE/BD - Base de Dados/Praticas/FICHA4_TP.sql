-------ficha4 TP------
--ex1
select E1.nome  AS Encarregado, E2.nome AS Empregado
from Empregados E1
inner join  Empregados E2 ON E1.NDep = E2.NDep
where E2.nemp <> E1.nemp
order by Encarregado, Empregado;

----------ex2
select ndep, nome
from dep
MINUS
select distinct emp.ndep, dep.nome
from emp
inner join emp on emp.ndep = dep.ndep;

-----------ex3
select upper( emp.nome), lower(emp.funcao), initcap(dep.nome)
from emp
inner join dep on emp.ndep = dep.ndep
where emp.funcao =  'gestor'
order by dep.nome, emp.nome;

-------------ex4
select count(*) as "Numero de empregados"
from emp
where sal > 900;

----------------ex5
select count(*) as "Empregados sem direito a premio"
from emp 
where premios = 0;

-----------ex6
select count(*) as "Numero de empregados", avg(sal), sum(sal * 12)
from emp
where sal >=800;

-------------ex7
select nome, sal, round( sal * 1.015 , 2)
from emp
where funcao = 'gestor';

-------------ex8
select nome, sal, round( sal * 1.015)
from emp
where funcao = 'gestor';