create schema hw_person;

create table hw_person.person
(
    id  bigserial
        constraint user_pk
            primary key,
    name varchar,
    age int,
    uuid UUID
);

create unique index user_id_uindex
    on hw_person.person (id);

