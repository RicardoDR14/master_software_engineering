exec naluno (2022147797);

--ficha2
--ex3
select TITULO, GENERO ,PRECO_TABELA from LIVROS
where PRECO_TABELA > 20
and (GENERO = 'Aventura' or GENERO = 'Policial')
order by TITULO;
EXEC SQLCHECK('FBZFTYYCWMJOENW');

--ex4
select NOME, SEXO
from AUTORES
where IDADE>30
and GENERO_PREFERIDO = 'Romance'
order by NOME asc;
EXEC SQLCHECK('FBHYDWADZAUJFAT');

--ex5
select distinct GENERO from LIVROS
where PAGINAS>400
or PRECO_TABELA>25
order by GENERO;
EXEC SQLCHECK('FBJQNSWEVIFUGWV');

--EX6
select TITULO, PRECO_TABELA PRECO from LIVROS
where GENERO = 'Informática'
order by PRECO_TABELA asc;
EXEC SQLCHECK('FBHOWKTFDMPWHAI');

--ex7
select TITULO, ISBN, QUANT_EM_STOCK from LIVROS
where GENERO = 'Informática'
and QUANT_EM_STOCK >10
order by PRECO_TABELA asc, QUANT_EM_STOCK desc;
EXEC SQLCHECK('FBLPCFWGZYXCITX');

--ex8
select distinct CODIGO_AUTOR from LIVROS
where QUANT_EM_STOCK>0
order by CODIGO_AUTOR desc;
EXEC SQLCHECK('FBCLDRYHKXKMJXJ');

--ex9
select TITULO from LIVROS
where GENERO not in ('Aventura');
EXEC SQLCHECK('FBELDMQIWUXFKNT');

--ex10
select TITULO, PRECO_TABELA from LIVROS
where GENERO not in ('Aventura');
EXEC SQLCHECK('FBIZEEEJGLOKLVI');

--ex11
select NOME, SEXO from AUTORES
where IDADE > 30 
and GENERO_PREFERIDO = 'Romance'
order by NOME asc;
EXEC SQLCHECK('FBTNEIDKPDPIMYQ');

--ex12
select NOME, IDADE from AUTORES
where NOME like '%o%' and NOME like '%u%'
order by IDADE;
EXEC SQLCHECK('FBCVUBULKHUONEV');

--ex13
select TITULO, GENERO from LIVROS
where TITULO like 'O%st%' or TITULO like 'W%NT%'
order by PRECO_TABELA;
EXEC SQLCHECK('FBLTKPUMCPJWOJT');

--ex14
select TITULO, GENERO, PRECO_TABELA from LIVROS
where GENERO in ('Policial', 'Romance', 'Economia')
order by TITULO;
EXEC SQLCHECK('FBWUSOSNTSQGPPG');

--ex15 
select TITULO, PAGINAS as "Num de Paginas" from LIVROS
where GENERO = 'Romance'
and PAGINAS < 520
order by PAGINAS;
EXEC SQLCHECK('FBHNFMWOMZTHQNJ');

--ex16 
select CODIGO_LIVRO, TITULO, PRECO_TABELA, QUANT_EM_STOCK as "Quantidade em stock" from LIVROS
where GENERO = 'Policial'
order by QUANT_EM_STOCK;
EXEC SQLCHECK('FBONDSEPBRRKRIO');

--EX17
select 'O livro "'||TITULO||'" tem '||PAGINAS||' paginas.' "Listagem paginas dos Livros"
from LIVROS
where GENERO in ('Aventura')
order by TITULO asc;
EXEC SQLCHECK('FBTFIETQMZONSSJ');