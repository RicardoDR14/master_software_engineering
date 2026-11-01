--------ficha11 pratica------------
exec naluno(2022147797);

--3
CREATE SEQUENCE exp_sequencia;

SELECT exp_sequencia.CURRVAL FROM dual; --ERRO
SELECT exp_sequencia.NEXTVAL FROM dual; --VALOR 1
SELECT exp_sequencia.NEXTVAL FROM dual; --VALOR 2
SELECT exp_sequencia.NEXTVAL from dual; --VALOR 3
SELECT exp_sequencia.CURRVAL FROM dual; --VALOR 3

DROP SEQUENCE exp_sequencia; --APAGA A SEQUENCIA

--4
CREATE TABLE EDITORAS_BACKUP AS SELECT *FROM EDITORAS;

EXEC SQLCHECK('FKNJNXHDQJLUSPV');

--5
alter table EDITORAS_BACKUP
add(
      primary key(codigo_editora),
      unique(n_contribuinte)
    );
EXEC SQLCHECK('FKYEXNXEUROHELV');

--ex6
create sequence seq_editback
start with 10 
increment by 1
nocycle;
EXEC SQLCHECK('FKYDNPXFPBWPIRC');

--7
INSERT INTO EDITORAS_BACKUP VALUES (seq_editback.nextval,
'D.Quixote', 901111111, 'Rua Cidade de Córdova, n.2 2610-038 Alfragide, Portugal', 
707252252, 707252253);
EXEC SQLCHECK('FKIVZGPGIOZKVZL');

--8
INSERT INTO EDITORAS_BACKUP VALUES (seq_editback.nextval,
'Almedina', 901212121, 'Rua Fernandes Tomás, n.º 76 a 80, 3000-167 Coimbra, Portugal',
239851903, 239851904);
EXEC SQLCHECK('FKAQCEVHKASRDLO');

--9
select seq_editback.CURRVAL FROM dual; 
select seq_editback.NEXTVAL FROM dual; 
select seq_editback.CURRVAL FROM dual; 
EXEC SQLCHECK('FKNNXQTIRKBLACD');

--10
DROP SEQUENCE seq_editback;
EXEC SQLCHECK('FKEPPMCJBROFZHT');


--11
CREATE VIEW livros_informática AS SELECT *FROM LIVROS WHERE UPPER(GENERO) = 'INFORMÁTICA';

SELECT *FROM livros_informática;

EXEC SQLCHECK('FKNAOLLKSLABNKG');

--EXEMPLOS
--a)
select titulo, a.nome
from autores a, livros l, editoras e
where a.codigo_autor = l.codigo_autor and e.codigo_editora = l.codigo_editora
and upper(e.nome) like 'FCA%';

--b)
select titulo, e.nome
from autores a, livros l, editoras e
where a.codigo_autor = l.codigo_autor and e.codigo_editora = l.codigo_editora
and upper(a.nome) like 'SÉRGIO SOUSA%';

--12
INSERT INTO livros_informática (codigo_livro, titulo,isbn, genero) 
VALUES (50,'Uma noite de Verão', 8000000001,'Informática');

INSERT INTO livros_informática (codigo_livro, titulo,isbn, genero) 
VALUES (51,'O céu é azul',8000000002,'Romance');

INSERT INTO livros (codigo_livro, codigo_editora, codigo_autor,titulo,isbn, genero) 
VALUES  (52,2, 2,'Longe de tudo',8000000003,'Informática');
EXEC SQLCHECK('FKPWADILYCFLUJD');

--13
DELETE FROM LIVROS WHERE CODIGO_LIVRO >= 50;
EXEC SQLCHECK('FKAQXPRMLGWNPXE');

--14
DROP VIEW livros_informática;
EXEC SQLCHECK('FKJZDYONFMBBLTS');

--15
CREATE TABLE livros_backup AS SELECT *FROM LIVROS;
EXEC SQLCHECK('FKZBSHOOZEWQOTY');

--16
CREATE VIEW autor_livro AS
    SELECT UPPER(NOME) AS NOME, UPPER(TITULO) AS TITULO FROM AUTORES A, LIVROS_BACKUP L
    WHERE A.CODIGO_AUTOR = L.CODIGO_AUTOR;
EXEC SQLCHECK('FKUAXXVPLXQVHWW');

--17
select *from autor_livro;
EXEC SQLCHECK('FKUZJQEQHXZGJJH');

--18
DELETE LIVROS_BACKUP;
EXEC SQLCHECK('FKDGLKJRGRCADEV');

--19
select *from autor_livro;
EXEC SQLCHECK('FKBMGXFSRZOUQYF');

--20
DROP TABLE LIVROS_BACKUP;
EXEC SQLCHECK('FKASZYETZHOLOEE');

--21
select *from autor_livro;
EXEC SQLCHECK('FKGRFMEUNATDSFN');

--22
create view livros_vendidos as
    select titulo, a.nome,sum(quantidade)as vendas
    from livros l, autores a, vendas
    where a.codigo_autor = l.codigo_autor
    and l.codigo_livro = vendas.codigo_livro
    group by titulo, a.nome
    order by vendas desc;
EXEC SQLCHECK('FKNWSKQVNMPQGVI');

--23
SELECT *FROM user_tables;
EXEC SQLCHECK('FKTDWYWWPTIMSOZ');

--24
SELECT table_name, constraint_name FROM user_constraints
order by 1,2;
EXEC SQLCHECK('FKFEPNHXETKIOZG');