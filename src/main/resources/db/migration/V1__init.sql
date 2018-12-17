create schema if not exists msgs;

create table if not exists msgs.author
(
  id        varchar(255) not null
    constraint author_pkey
    primary key,
  email     varchar      not null,
  full_name varchar(255) not null
);

create table if not exists msgs.conversation
(
  id     varchar(255) not null
    constraint conversation_pkey
    primary key,
  status varchar(255) not null
);

create table if not exists msgs.message
(
  id              varchar(255)  not null
    constraint message_pkey
    primary key,
  text            varchar       not null,
  created_date    timestamp     not null,
  user_id         varchar(255)  not null
    constraint author_fk
    references msgs.author,
  conversation_id varchar(255)  not null
    constraint conversation_fk
    references msgs.conversation
);