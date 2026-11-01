-- s2
select * from clientes_backup;

update CLIENTES_BACKUP
set N_CONTRIBUINTE = 999999999
where CODIGO_CLIENTE=1;

rollback;

update CLIENTES_BACKUP
set nome = 'Manuel Moreira'
where nome = 'João Moreira' and CODIGO_CLIENTE=11;

update CLIENTES_BACKUP
set morada = 'Rua Ferreira Borges, 3000-179, Coimbra, Portugal'
where CODIGO_CLIENTE=8;

create table FUNCIONARIO( 
  N_CONTRIBUINTE number primary key,
  nome VARCHAR2(40),
  telefone number);
  
select * from funcionario;
  
insert into funcionario
values (987654321,'Joaquim Almeida',239534123);

commit;

drop table funcionario;