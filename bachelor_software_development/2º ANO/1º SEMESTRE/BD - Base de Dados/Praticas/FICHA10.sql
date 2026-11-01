---------ficha10 Pratica--------
EXEC NALUNO(2022147797);

--ex3
create table autores2(
  codigo_autor number(4,0),
  nome varchar2(30),
  n_contribuinte number(9,0) not null, 
  morada varchar2(50),
  idade number(4,0),
  sexo char(1),
  nacionalidade varchar2(20),
  genero_preferido varchar2(20),
  constraint pk_au2_coda primary key(codigo_autor),
  constraint uk_au2_nc unique (n_contribuinte),
  constraint ck_au2_nome check (nome=upper(nome)),
  constraint ck_au2_idade check (idade between 0 and 199)
);

drop table autores2;
EXEC SQLCHECK('FJURGEYCRPWBCQL');

--ex4
create table avaliacoes(
  codigo_livro number(4),
  codigo_cliente number(4),
  nota number(1),
  constraint pk_av_cl_cc primary key(codigo_livro,codigo_cliente),
  constraint fk_av_cl foreign key(codigo_livro) references livros(codigo_livro),
  constraint fk_av_cc foreign key(codigo_cliente) references clientes(codigo_cliente)
);
drop table avaliacoes;
EXEC SQLCHECK('FJPJGYEDBOGCGUG');

--ex5
create table livros_backup as
select CODIGO_LIVRO,
       CODIGO_EDITORA,
       CODIGO_AUTOR,
       TITULO,
       ISBN,
       upper(GENERO)as genero,
       PRECO_TABELA,
       PAGINAS,
       QUANT_EM_STOCK,
       UNIDADES_VENDIDAS,
       DATA_EDICAO
from livros;
drop table livros_backup;

--ex6
alter table livros_backup
add (constraint pk_livrosbackup_codigolivro primary key (codigo_livro),
    constraint fk_livrosbackup_codigoautor foreign key (codigo_autor) references autores(codigo_autor),
    constraint fk_livrosbackup_codigoeditora foreign key (codigo_editora)references editoras(codigo_editora)
);

--ex7
insert into livros_backup
values (51,(select codigo_editora from editoras where nome like '%FCA%'),
    (select codigo_autor from autores where nome='Sérgio Sousa'), 'Informática para todos',132434,
    'Informática',24,430,null,0,sysdate);

--ex8
insert into autores2
values(45,'José de Magalhães',77665544,'Porto',50,m,'Portuguesa','Aventura');

--ex9
delete from livros_backup where codigo_livro in (51);

--ex10
update livros_backup
set preco_tabela = preco_tabela*1.1
where genero = 'AVENTURA';

--ex11
delete from livros_backup
where INITCAP(genero)='Informática'
and preco_tabela < (select avg(preco_tabela)from livros_backup
                    where INITCAP(genero) = 'Informática');

--ex12
create table autores_backup as
select 
      codigo_autor,
      nome,
      n_contribuinte,
      morada,
      idade,
      sexo,
      nacionalidade,
      genero_preferido
from autores;

--ex13
alter table autores_backup
add(nlivro NUMBER
    default 0 constraint ck_nlivros check (nlivro >= 0 and nlivro <= 150));

--ex14
update autores_backup a
set nlivro = (
    select count(*) 
    from livros l
    where l.codigo_autor = a.codigo_autor
);

--ex15
update AUTORES_BACKUP
set GENERO_PREFERIDO = (
    select max(GENERO)
    from (
        select GENERO, count(*) as cnt
        from LIVROS_BACKUP L, AUTORES_BACKUP A
        where L.CODIGO_AUTOR = A.CODIGO_AUTOR
        and A.IDADE < 50
        group by GENERO
        order by cnt desc
    ) where rownum = 1
)
where IDADE < 50;

--ex16
create table editoras_backup as
select 
      codigo_editora,
      nome,
      n_contribuinte,
      morada,
      telefone,
      fax
from editoras;

--ex17
drop table editoras_backup;

--ex18
alter table livros_backup
add(EDICAO NUMBER(7));

--ex19
update livros_backup
set unidades_vendidas = (select unidades_vendidas from livros 
                           where livros.codigo_livro = livros_backup.codigo_livro);

--ex20
update vendas_backup
set total_venda = quantidade * preco_unitario;

--ex21
create table vendas_backup as
select
      codigo_venda,
      data_venda,
      codigo_cliente,
      codigo_livro,
      preco_unitario,
      quantidade,
      total_venda
from vendas;

--ex22
delete from vendas_backup where to_char(data_venda, 'MM') = '01';

--ex23
delete from vendas_backup
where (codigo_livro, data_venda) in (
    select codigo_livro, data_venda
    from (
        select vendas.codigo_livro, vendas.data_venda
        from vendas_backup vendas, livros_backup livros, autores_backup autores
        where vendas.codigo_livro = livros.codigo_livro
        and livros.codigo_autor = autores.codigo_autor
        and autores.nome = 'Sérgio Sousa'
        order by livros.preco_tabela desc
    ) where rownum = 1
);