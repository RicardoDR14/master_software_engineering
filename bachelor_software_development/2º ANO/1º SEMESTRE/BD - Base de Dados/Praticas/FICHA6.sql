-------ficha6 pratica------
exec naluno(2022147797);

--ex3
with tab as (select min(preco_tabela) menor
             from livros
             where genero = 'Fantástico')
select titulo, menor as "Preco_Mais_Baixo"
from livros, tab
where preco_tabela = menor
and genero = 'Fantástico';
EXEC SQLCHECK('FFKMXQQCCWIZINA');

--ex4
select titulo,
       paginas as "N_Paginas",
       preco_tabela as "Preco_mais_Baixo"
from livros
where genero = 'Fantástico'
and preco_tabela <= all(select preco_tabela
                         from livros
                         where genero = 'Fantástico');
EXEC SQLCHECK('FFDJTIRDJJNSJGQ');

--ex5
select preco_tabela as "Preco_Mais_Baixo",
       titulo
from livros L1
where genero = 'Fantástico'
and not exists(select *
               from livros L2
               where genero = 'Fantástico'
               and L1.preco_tabela > L2.preco_tabela);
EXEC SQLCHECK('FFZMRWEEXQNJKIQ');

--ex6
select titulo as "Livro Mais Barato"
from livros, (select min(preco_tabela) menor
              from livros
              where genero = 'Fantástico')
where genero = 'Fantástico'
and preco_tabela = menor;
EXEC SQLCHECK('FFOIHIAFORVLLSJ');

--ex7
select distinct nome
from autores, livros L1, (select avg(paginas) media_pag
                          from livros)L2
where autores.codigo_autor = L1.codigo_autor
and L1.paginas > media_pag
order by nome;

with L2 as(select avg(paginas) media_pag
                          from livros)
select distinct nome
from autores, livros L1, L2
where autores.codigo_autor = L1.codigo_autor
and L1.paginas > media_pag
order by nome;

select distinct nome
from autores, livros L1
where autores.codigo_autor = L1.codigo_autor
and L1.paginas > (select avg(paginas) media_pag
                          from livros)
order by nome;
EXEC SQLCHECK('FFBKSPCGAZHRMTE');

--ex8
with n1 as (select codigo_autor, count(codigo_livro) media
            from livros
            group by codigo_autor),
     avgnl as (select avg(n1.media) media_total from n1)
  
select distinct nome, idade, nacionalidade
from autores a, livros l, n1, avgnl
where l.codigo_autor=a.codigo_autor
  and a.codigo_autor= n1.codigo_autor 
  and n1.media > avgnl.media_total;

select distinct nome, idade, nacionalidade
from livros, autores
where livros.codigo_autor = autores.codigo_autor
and autores.codigo_autor in (
     select autores.codigo_autor
     from autores, livros
     where livros.codigo_autor = autores.codigo_autor
     group by autores.codigo_autor
     having count(livros.codigo_livro) > (
         select avg(quantlivros)
         from (
             select autores.codigo_autor, count(livros.codigo_livro) as quantlivros
             from autores, livros
             where livros.codigo_autor = autores.codigo_autor
             group by autores.codigo_autor
              ) 
      )
);
EXEC SQLCHECK('FFIIHBGHVJSENIL');

--ex9
select distinct l.codigo_autor, 
                l.titulo,preco_tabela "PRECO",
                x.minimo "PRECO_MINIMO",
                (l.preco_tabela - x.minimo) "DIFERENCA_PRECO"
from livros l, (select codigo_autor, min(preco_tabela) minimo 
                from livros where genero = 'Fantástico'
                group by codigo_autor)x
where genero = 'Fantástico'
and l.codigo_autor = x.codigo_autor
order by 1, 3;
EXEC SQLCHECK('FFHAWDXINTAMOTO');

--ex10
select genero, titulo, unidades_vendidas
from livros
where (genero,unidades_vendidas) in (select genero, min(unidades_vendidas) minimo
                                     from livros
                                     group by genero)
order by unidades_vendidas desc, genero;
EXEC SQLCHECK('FFEFGOFJFOVYPMP');

--ex11
select l.titulo,
       to_char(round(100 * l.unidades_vendidas / b.soma, 1), '990.99') as percentagem
from livros l, editoras e, (select sum(l.unidades_vendidas) as soma
                            from livros l, editoras e
                            where l.codigo_editora = e.codigo_editora
                            and upper(e.nome) like '%GRADIVA%')b
where l.codigo_editora = e.codigo_editora
  and upper(e.nome) like '%GRADIVA%'
order by 2 desc, 1;

with b as (select sum(l.unidades_vendidas) as soma
           from livros l, editoras e
           where l.codigo_editora = e.codigo_editora
            and upper(e.nome) like '%GRADIVA%')
select l.titulo,
       to_char(round(100 * l.unidades_vendidas / b.soma, 1), '990.99') as percentagem
from livros l, editoras e, b
where l.codigo_editora = e.codigo_editora
  and upper(e.nome) like '%GRADIVA%'
order by 2 desc, 1;
EXEC SQLCHECK('FFUSZQRKODGHQZN');

--ex12
select l.titulo,l.genero
from livros l, clientes c, vendas v
where l.codigo_livro = v.codigo_livro
and v.codigo_cliente = c.codigo_cliente
and upper(c.morada) like '%LISBOA%'
and v.quantidade = (select max(quantidade)
                    from clientes c, vendas v
                    where v.codigo_cliente = c.codigo_cliente
                    and upper(c.morada) like '%LISBOA%');
EXEC SQLCHECK('FFEATZOLGSNMRRV');