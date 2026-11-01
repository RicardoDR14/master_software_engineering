----------ficha7 pratica----------
EXEC NALUNO(2022147797);

--ex3
select titulo, soma_quantidade
from (
    select l.titulo, sum(v.quantidade) as soma_quantidade,
           row_number() over (order by sum(v.quantidade) desc, l.titulo asc) as rn
    from livros l
    join vendas v on l.codigo_livro = v.codigo_livro
    group by l.titulo
) 
where rn = 1;
EXEC SQLCHECK('FGQWRMHCENGCJFH');

--ex4
WITH LivrosVendidos AS (
    SELECT 
        L.GENERO,
        L.TITULO,
        L.PAGINAS,
        COALESCE(SUM(V.QUANTIDADE), 0) AS TOTAL_VENDIDO
    FROM LIVROS L
    LEFT JOIN VENDAS V ON L.CODIGO_LIVRO = V.CODIGO_LIVRO
    WHERE L.PAGINAS > 600
    GROUP BY L.GENERO, L.TITULO, L.PAGINAS
)

SELECT GENERO, TITULO, PAGINAS
FROM (
    SELECT 
        GENERO,
        TITULO,
        PAGINAS,
        TOTAL_VENDIDO,
        ROW_NUMBER() OVER (PARTITION BY GENERO ORDER BY TOTAL_VENDIDO DESC) AS RN
    FROM LivrosVendidos
) 
WHERE RN = 1
ORDER BY GENERO, TITULO;
EXEC SQLCHECK('FGQQTBADOXSZKMA');

--ex5
select l.titulo, a.nome, l.unidades_vendidas
from livros l, autores a,(select codigo_autor, max(unidades_vendidas) m
from livros
group by codigo_autor)aa
where l.codigo_autor = aa.codigo_autor
and a.codigo_autor = l.codigo_autor
and l.unidades_vendidas = aa.m
order by a.nome, l.titulo;
EXEC SQLCHECK('FGJZUXGEZPNYLKV');

--ex6
SELECT 
    e.NOME AS NOME,
    l.TITULO,
    a.NOME AS NOME_AUTOR,
    l.UNIDADES_VENDIDAS
FROM 
    LIVROS l
JOIN 
    EDITORAS e ON e.CODIGO_EDITORA = l.CODIGO_EDITORA
JOIN 
    AUTORES a ON a.CODIGO_AUTOR = l.CODIGO_AUTOR
JOIN 
    (SELECT 
        EDITORAS.NOME AS EN, 
        MAX(UNIDADES_VENDIDAS) AS M
     FROM 
        LIVROS
     JOIN 
        EDITORAS ON LIVROS.CODIGO_EDITORA = EDITORAS.CODIGO_EDITORA
     GROUP BY 
        EDITORAS.NOME) aa ON l.UNIDADES_VENDIDAS = aa.M AND e.NOME = aa.EN
ORDER BY 
    e.NOME, 
    l.TITULO;
EXEC SQLCHECK('FGKMUHRFQTBIMZF');

--ex7
SELECT C.CODIGO_CLIENTE AS "Código do Cliente", C.NOME AS "Nome", SUM(V.QUANTIDADE) AS NUM_LIVROS_COMPRADOS
FROM VENDAS V
JOIN CLIENTES C ON V.CODIGO_CLIENTE = C.CODIGO_CLIENTE
WHERE V.DATA_VENDA BETWEEN TO_DATE('2020-10-01', 'YYYY-MM-DD') AND TO_DATE('2020-12-31', 'YYYY-MM-DD')
GROUP BY C.CODIGO_CLIENTE, C.NOME
HAVING SUM(V.QUANTIDADE) = (
    SELECT MAX(SUM_QUANTIDADE)
    FROM (
        SELECT SUM(QUANTIDADE) AS SUM_QUANTIDADE
        FROM VENDAS
        WHERE DATA_VENDA BETWEEN TO_DATE('2020-10-01', 'YYYY-MM-DD') AND TO_DATE('2020-12-31', 'YYYY-MM-DD')
        GROUP BY CODIGO_CLIENTE
    )
)
ORDER BY C.NOME;
EXEC SQLCHECK('FGOEDODGBGAONUP');

--ex8
WITH MaxVendas AS (
    SELECT 
        CODIGO_EDITORA, 
        MAX(UNIDADES_VENDIDAS) AS Max_Unid_Vendidas
    FROM 
        LIVROS
    GROUP BY 
        CODIGO_EDITORA
)

SELECT 
    a.NOME AS Nome,
    e.NOME AS "Nome Editora",
    l.UNIDADES_VENDIDAS AS UNID_VENDIDAS
FROM 
    LIVROS l
JOIN 
    AUTORES a ON l.CODIGO_AUTOR = a.CODIGO_AUTOR
JOIN 
    EDITORAS e ON l.CODIGO_EDITORA = e.CODIGO_EDITORA
JOIN 
    MaxVendas mv ON l.CODIGO_EDITORA = mv.CODIGO_EDITORA
WHERE 
    l.UNIDADES_VENDIDAS >= 0.7 * mv.Max_Unid_Vendidas
ORDER BY 
    a.NOME, 
    e.NOME;
EXEC SQLCHECK('FGYRVQIHKSBOOLN');

--ex9
WITH VendaMaxima2022 AS (
    SELECT 
        CODIGO_LIVRO, 
        MAX(QUANTIDADE) AS Max_Quantidade
    FROM 
        VENDAS
    WHERE 
        EXTRACT(YEAR FROM DATA_VENDA) = 2022
    GROUP BY 
        CODIGO_LIVRO
    ORDER BY 
        Max_Quantidade DESC
)

, VendaTop AS (
    SELECT CODIGO_LIVRO, Max_Quantidade
    FROM (
        SELECT CODIGO_LIVRO, Max_Quantidade
        FROM VendaMaxima2022
        WHERE ROWNUM = 1
    )
)

SELECT 
    l.TITULO,
    l.GENERO,
    a.NOME
FROM 
    LIVROS l
JOIN 
    AUTORES a ON l.CODIGO_AUTOR = a.CODIGO_AUTOR
JOIN 
    VendaTop vt ON l.CODIGO_LIVRO = vt.CODIGO_LIVRO
ORDER BY 
    a.NOME, 
    l.TITULO;
EXEC SQLCHECK('FGSZIMRISBUYPFF');

--ex10
select clientes.nome, livros.titulo, sum(vendas.quantidade) as "QUANT_COMPRADA"
from livros, clientes, vendas
where livros.codigo_livro = vendas.codigo_livro
and vendas.codigo_cliente = clientes.codigo_cliente
and vendas.codigo_cliente = (select codigo_cliente
                            from vendas
                            where preco_unitario < 30
                            group by codigo_cliente
                            having sum(vendas.quantidade) = (select max(sum(vendas.quantidade))
                                                        from vendas
                                                        where vendas.preco_unitario < 30
                                                        group by vendas.codigo_cliente))
group by livros.titulo, clientes.nome
order by 1,2;
EXEC SQLCHECK('FGKIKZOJTUGEQTK');

--ex11
select a.codigo_autor, a.nome, quantidade, L.TITULO
from autores a, livros l, vendas v
where a.codigo_autor = l.codigo_autor
and l.codigo_livro = v.codigo_livro
and l.codigo_autor in (
    select l.codigo_autor
    from livros l, vendas v,(
    select min(count(v.codigo_venda)) m
    from livros l, vendas v
    where l.codigo_livro = v.codigo_livro
    group by l.codigo_autor
    ) tabl
    where l.codigo_livro = v.codigo_livro
    group by l.codigo_autor, tabl.m
    having count(v.codigo_venda)=tabl.m
    )
    order by 2;
EXEC SQLCHECK('FGHHHQDKNXLPRFD');