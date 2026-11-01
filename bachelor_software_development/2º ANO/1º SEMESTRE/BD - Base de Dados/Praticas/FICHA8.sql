----------ficha 8 pratica-----------
EXEC NALUNO( 2022147797 );

--Pergunta 3
SELECT A.NOME AS Nome,
       L.TITULO,
       L.LIVROS_PORTO_EDITORA,
       TL.TOTAL_DE_LIVROS
FROM Autores A
JOIN (
    SELECT L.CODIGO_AUTOR, L.TITULO, COUNT(*) AS LIVROS_PORTO_EDITORA
    FROM Livros L
    JOIN Editoras E ON L.CODIGO_EDITORA = E.CODIGO_EDITORA
    WHERE E.NOME = 'PORTO EDITORA'
    AND L.PRECO_TABELA = (
        SELECT MIN(PRECO_TABELA)
        FROM Livros
        WHERE CODIGO_EDITORA = E.CODIGO_EDITORA
    )
    GROUP BY L.CODIGO_AUTOR, L.TITULO
) L ON A.CODIGO_AUTOR = L.CODIGO_AUTOR
JOIN (
    SELECT CODIGO_AUTOR, COUNT(*) AS TOTAL_DE_LIVROS
    FROM Livros
    GROUP BY CODIGO_AUTOR
) TL ON A.CODIGO_AUTOR = TL.CODIGO_AUTOR
WHERE ROWNUM = 1
ORDER BY TL.TOTAL_DE_LIVROS DESC;

EXEC SQLCHECK('FHPIETTCGQOCKNR');


--Pergunta 4
select a.nome as NOME, 
    case when a.genero_preferido is not null then 
            (select count(*) from livros l 
             where l.codigo_autor = a.codigo_autor
             and l.genero = a.genero_preferido)
         else 
             0 
    end as "Genero Preferido",
    (select count(*) from livros l
     where l.codigo_autor = a.codigo_autor) as "Total de Livros"
from autores a
order by NOME;

EXEC SQLCHECK('FHFMLMIDVHAALUN');

--Pergunta 5 
WITH EditoraMaisVendida AS (
  SELECT LIVROS.CODIGO_EDITORA
  FROM LIVROS
  GROUP BY LIVROS.CODIGO_EDITORA
  HAVING SUM(LIVROS.UNIDADES_VENDIDAS) = (
    SELECT MAX(VENDAS_TOTAL)
    FROM (
      SELECT CODIGO_EDITORA, SUM(UNIDADES_VENDIDAS) AS VENDAS_TOTAL
      FROM LIVROS
      GROUP BY CODIGO_EDITORA
    )
  )
)

SELECT 'O autor ' || AUTORES.NOME || ' escreveu ' || COUNT(LIVROS.CODIGO_AUTOR) || ' de ' || 
       (SELECT COUNT(*) FROM LIVROS WHERE CODIGO_AUTOR = AUTORES.CODIGO_AUTOR) || 
       ' livros para a editora FCA - EDITORA.' AS RESULTADO
FROM AUTORES, LIVROS
WHERE AUTORES.CODIGO_AUTOR = LIVROS.CODIGO_AUTOR
AND LIVROS.CODIGO_EDITORA IN (SELECT CODIGO_EDITORA FROM EditoraMaisVendida)
GROUP BY AUTORES.NOME, AUTORES.CODIGO_AUTOR
ORDER BY AUTORES.NOME ASC; 

EXEC SQLCHECK('FHQVHSKECGWIMOH');


--Pergunta 6
SELECT 
    'top ' || sub.ranking AS top,
    sub.titulo,
    sub.total_vendido AS soma
FROM (SELECT l.titulo,
             SUM(v.quantidade) as total_vendido,
             RANK() OVER (ORDER BY SUM(v.quantidade) DESC) as ranking
      FROM livros l, vendas v
      WHERE l.codigo_livro = v.codigo_livro
      AND EXTRACT(YEAR FROM v.data_venda) = EXTRACT(YEAR FROM SYSDATE) - 1
      GROUP BY l.titulo
      )  sub
WHERE sub.ranking <= 3;

EXEC SQLCHECK('FHFMHGHFGBZWNSR');

--Pergunta 7
WITH DATACOMPRASMAX AS (
  SELECT CLIENTES.CODIGO_CLIENTE,
         MAX(VENDAS.DATA_VENDA) AS DATAMAX
  FROM CLIENTES,VENDAS
  WHERE CLIENTES.CODIGO_CLIENTE = VENDAS.CODIGO_CLIENTE
  GROUP BY CLIENTES.CODIGO_CLIENTE
)

SELECT C.NOME, L.TITULO,
       TO_CHAR(DCM.DATAMAX, 'DD-MM-YYYY') AS DATA_DA_VENDA
FROM DATACOMPRASMAX DCM, CLIENTES C, VENDAS V, LIVROS L
WHERE DCM.CODIGO_CLIENTE = C.CODIGO_CLIENTE
AND C.CODIGO_CLIENTE = V.CODIGO_CLIENTE 
AND DCM.DATAMAX = V.DATA_VENDA
AND V.CODIGO_LIVRO = L.CODIGO_LIVRO
ORDER BY C.NOME ASC;

EXEC SQLCHECK('FHQSWMFGQHNBOVG');

--Pergunta 8
WITH VENDASMENSAIS1 AS (
    SELECT TO_CHAR(Data_Venda, 'YYYY/MM') AS ANOMES,
           SUM(Quantidade) AS TOTAL
    FROM VENDAS
    GROUP BY TO_CHAR(Data_Venda, 'YYYY/MM')
    ),
    VENDASMENSAIS2 as (
    SELECT TO_CHAR(add_months (Data_Venda,1), 'YYYY/MM') AS AnoMes,
        SUM(Quantidade) AS QuantidadeTotal
    FROM VENDAS
    GROUP BY TO_CHAR(add_months (Data_Venda,1), 'YYYY/MM')
    )
    
SELECT VM1.ANOMES AS MES,
       VM1.TOTAL AS Vendas_Do_Mes,
       NVL(VM2.QuantidadeTotal, 0) AS Vendas_Do_Mes_Anterior,
       CASE
          WHEN VM2.QuantidadeTotal IS NOT NULL THEN
            ROUND((VM1.TOTAL - VM2.QuantidadeTotal) / VM2.QuantidadeTotal * 100, 2)
          ELSE
            NULL
        END AS Variacao_Percent
FROM VENDASMENSAIS1 VM1, VENDASMENSAIS2 VM2
WHERE VM1.ANOMES = VM2.AnoMes
AND VM2.QuantidadeTotal IS NOT NULL
AND (VM1.TOTAL - VM2.QuantidadeTotal) / VM2.QuantidadeTotal * 100 < -50
ORDER BY VM1.ANOMES;
    
EXEC SQLCHECK('FHNFTPUHLPYZPFU');
