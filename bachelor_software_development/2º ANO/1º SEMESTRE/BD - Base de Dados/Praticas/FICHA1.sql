--ficha1--
EXEC NALUNO( 2022147797 ); 
--ex3
SELECT * FROM autores;
EXEC SQLCHECK('FAHZSOFACFUDBBX');
--ex4
select TITULO from livros order by TITULO;
EXEC SQLCHECK('FAJOFSKBMIUXCEZ'); 
--ex5
select distinct GENERO from livros order by GENERO;
EXEC SQLCHECK('FAEHGSQCELEEDMQ');
--ex6
select TITULO, GENERO, PRECO_TABELA preco
from livros
where PRECO_TABELA >=30 and PRECO_TABELA<=40
order by PRECO_TABELA;
EXEC SQLCHECK('FAZPHKCDDOXSEUK');