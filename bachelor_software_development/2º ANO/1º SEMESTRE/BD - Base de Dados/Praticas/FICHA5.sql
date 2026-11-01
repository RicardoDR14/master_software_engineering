--ficha5 pratica----
exec naluno(2022147797);

--ex3
select count(*) as "Num Livros de Informática" from livros
where genero = 'Informática'
and preco_tabela between 20 and 40;
EXEC SQLCHECK('FEKPMBHCGFHKHRG');

--ex4
select count(data_edicao) as "Num Livros Editados",
       sum(unidades_vendidas) as "Total Unidades vendidas",
       avg(preco_tabela) as "Preço de Tabela Médio"
from livros
where genero in ('Romance','Aventura');
EXEC SQLCHECK('FEQZNHADADLXIRE');

--ex5
select genero as "GENERO", count(data_edicao) as "Num. Livros Editados" from livros
group by genero
order by count(data_edicao) desc, genero;
EXEC SQLCHECK('FETUCAREYKNOJEP');

--ex6
select titulo,
       round(min(preco_unitario),2) as "PRECO_MAIS_BAIXO", 
       round(max(preco_unitario),2) as "PRECO_MAIS_ALTO",
       round(avg(preco_unitario),2) as "PRECO_MEDIO"
from livros, vendas
where livros.codigo_livro = vendas.codigo_livro
and genero = 'Informática'
group by titulo
order by max(preco_unitario) desc, titulo;
EXEC SQLCHECK('FEMKYWMFLFBFKTH');

--ex7
select genero,
       count(data_edicao) as "N_LIVROS_EDITADOS",
       max(preco_tabela)-min(preco_tabela) as "DIFERENCA_MAX_MIN"
from livros
group by genero
order by max(preco_tabela)-min(preco_tabela) desc;
EXEC SQLCHECK('FEAUZTGGYJWBLSU');

--ex8
select titulo,
       preco_tabela,
       quantidade as "NUM_VENDIDOS",
       quantidade * preco_tabela as "RECEITA_ESPERADA",
       quantidade * preco_unitario as "RECEITA_EFECTIVA"
from livros, vendas
where livros.codigo_livro=vendas.codigo_livro
and to_number(to_char(data_venda, 'YYYY')) = to_number(to_char(sysdate, 'YYYY')) - 1
order by quantidade;
EXEC SQLCHECK('FELBAFEHXKEYMHB');

--ex9
select genero,
       ceil(avg(preco_tabela)) as "PRECO Médio"
from livros
group by genero
having count(*)>3
order by avg(preco_tabela) desc, genero;
EXEC SQLCHECK('FEMDMOHIGBIPNYO');

--ex10
select nome,
       min(preco_tabela) as "Preco do Mais Barato",
       max(preco_tabela) as "Preco do Mais Caro"
from livros, autores
where livros.codigo_autor=autores.codigo_autor
group by nome
having min(preco_tabela)>10
and max(preco_tabela)<25
and min(preco_tabela)<max(preco_tabela)
order by min(preco_tabela),nome;
EXEC SQLCHECK('FEFWWIEJQMYQOLI');

--ex11
select nome as "NOME",
       sum(quantidade) as "N.Livros Comprados",
       round(avg(preco_unitario),1) as "Preco Medio",
       count(distinct codigo_autor) as "N.Autor Diferent"
from clientes, vendas, livros
where clientes.codigo_cliente=vendas.codigo_cliente
and livros.codigo_livro=vendas.codigo_livro
and morada not like '%Lisboa%'
group by nome
having count(distinct codigo_editora) =1
order by nome;
EXEC SQLCHECK('FEXIBSQKCAWKPBN');

--ex12
select nome,
       replace(count(codigo_livro),'0','Nenhum') as "Num de Livros escritos"
from autores, livros
where autores.codigo_autor = livros.codigo_autor(+)
and idade > 50 
group by nome
order by nome;
EXEC SQLCHECK('FEKXNTJLDGJOQTJ');