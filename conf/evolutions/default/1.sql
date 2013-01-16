# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  account_id                integer auto_increment not null,
  name                      varchar(255),
  download                  varchar(255),
  constraint pk_account primary key (account_id))
;

create table account_contact (
  account_contact_id        integer auto_increment not null,
  account_id                integer,
  username                  varchar(255),
  password                  varchar(255),
  constraint pk_account_contact primary key (account_contact_id))
;

alter table account_contact add constraint fk_account_contact_account_1 foreign key (account_id) references account (account_id);
create index ix_account_contact_account_1 on account_contact (account_id);



# --- !Downs

drop table account;

drop table account_contact;

