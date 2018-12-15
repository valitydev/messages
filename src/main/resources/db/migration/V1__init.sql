create schema if not exists msgs;

create table if not exists msgs.author
(
id        varchar(255) not null
    constraint author_pkey
    primary key,
  email     varchar(255),
  full_name varchar(255)
);

alter table msgs.author
  owner to postgres;

create table if not exists msgs.conversation
(
  id     varchar(255) not null
    constraint conversation_pkey
    primary key,
  status varchar(255) not null
);

alter table msgs.conversation
  owner to postgres;

create table if not exists msgs.message
(
  id              varchar(255) not null
    constraint message_pkey
    primary key,
  text            varchar(255),
  created_date       timestamp,
  user_id       varchar(255) not null
    constraint fk7103wf07agwpt9cyo8bxktjy3
    references msgs.author,
  conversation_id varchar(255) not null
    constraint fk6yskk3hxw5sklwgi25y6d5u1l
    references msgs.conversation
);

alter table msgs.message
  owner to postgres;