-------ficha3 pratica---------
exec naluno(2022147797);
--ex3
select titulo,genero from livros, autores
where preco_tabela <20 
and autores.nome = 'J. K. Rowling'
and livros.codigo_autor = autores.codigo_autor 
order by titulo;
EXEC SQLCHECK('FCINKYPCGVYDGGH');

--ex4
select titulo, genero, nome, morada from livros, autores 
where autores.morada like '%Cascais%'
and not livros.genero = 'Romance' 
and livros.codigo_autor = autores.codigo_autor
order by titulo;
EXEC SQLCHECK('FCEKVCZDJCDXCDF');

--ex5
select distinct nome,idade, genero_preferido from autores, livros 
where genero_preferido = livros.genero 
and livros.codigo_autor = autores.codigo_autor
order by nome;
EXEC SQLCHECK('FCUXGHVEQFTXWVO');

--ex6
select titulo, genero, preco_tabela, paginas from livros, clientes,vendas 
where paginas>300 
and clientes.morada like '%Porto%' 
and livros.codigo_livro = vendas.codigo_livro
and vendas.codigo_cliente = clientes.codigo_cliente
order by titulo;
EXEC SQLCHECK('FCOAPRNFTZUAMIP');

--ex7
select nome, genero_preferido, titulo, genero as "Genero do Livro" from livros, autores
where preco_tabela <40 
and not livros.genero = autores.genero_preferido
and livros.codigo_autor = autores.codigo_autor
order by autores.nome, titulo;
EXEC SQLCHECK('FCTVMKWGHTEVJOA');

--ex8
select titulo, preco_tabela, unidades_vendidas, preco_tabela*0.2*unidades_vendidas as "Rendeu" from livros
where genero = 'Fantástico' 
order by "Rendeu" desc; 
EXEC SQLCHECK('FCGBQWUHPLOLVGV');

--ex9
select distinct autores.nome, idade, autores.morada from autores, clientes,vendas 
where vendas.codigo_cliente = clientes.codigo_cliente
and autores.n_contribuinte = clientes.n_contribuinte
order by nome;
EXEC SQLCHECK('FCHIEOUIJMGUZSG');

--ex10
select titulo, genero, preco_tabela,trunc(unidades_vendidas*preco_tabela*0.2) as "Rendimento"
from livros
where unidades_vendidas*preco_tabela*0.2  > 400000
order by "Rendimento" desc ,preco_tabela desc ;
EXEC SQLCHECK('FCTTXUUJYDKYYCO');

--ex11
select titulo,
       preco_tabela,
       paginas,round(100*(preco_tabela/paginas),2) as "Custo página", 
       ceil((preco_tabela/paginas)*100) as "Custo pág.(sup)", 
       floor((preco_tabela/paginas)*100) as "Custo pág.(inf)" 
from livros
where paginas > 100 
and paginas < 300 
order by titulo;
EXEC SQLCHECK('FCQVTXHKQBHFJBZ');

--ex12
select lower(titulo) as "Titulo (em minusculas)",
       upper(titulo) as "Titulo (em maiusculas)",
       initcap(titulo) as "Titulo (1a letra maiuscula)"
from livros
where genero = 'Informática'
order by titulo;
EXEC SQLCHECK('FCFXAVLLOEBMIEN');

--ex13
select titulo "Titulo",
       preco_tabela "Preco",
       quant_em_stock as "Quantidade em stock"
from livros
where genero = 'Romance'
order by "Quantidade em stock" desc;
EXEC SQLCHECK('FCROYAZMYRXAHEL');

--ex14
select autores.nome "Nome completo",
       substr(autores.nome,1,instr(autores.nome,' ',1,1)-1) as "Primeiro Nome",
       substr(autores.nome,instr(autores.nome,' ',-1)+1) as "Ultimo Nome"
from autores 
where autores.genero_preferido = 'Informática'  
order by autores.idade,autores.nome asc;
EXEC SQLCHECK('FCVMAFTNZJENMQU');

--ex15 
select lower(titulo) as "Titulo (em minusculas)",
       upper(genero) as "genero (maisculas)",
       autores.nome as "Nome Autor (1a letra)"
from livros, autores, editoras
where livros.codigo_autor = autores.codigo_autor
and genero = genero_preferido
and livros.codigo_editora = editoras.codigo_editora
and editoras.nome = 'FCA - EDITORA'
order by titulo desc;
EXEC SQLCHECK('FCKUJPVODLJWHHD');

--ex16
select titulo, genero from livros
where titulo like '%f%'
and length(titulo) > 35
order by titulo;
EXEC SQLCHECK('FCYYGHXPVYZLYEN');

--ex17
select titulo, preco_tabela as preco, round(preco_tabela*1.08,1) as "PRECO_COM_AUMENTO" from livros
where genero = 'Fantástico'
and preco_tabela > 20
order by round(preco_tabela*1.08,1) desc;
EXEC SQLCHECK('FCGQIBVQANPMAAK');