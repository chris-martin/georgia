drop schema if exists georgia cascade;
create schema georgia;
set search_path to georgia, public;

create table people(
  person_name text,
  job_title text,
  salary numeric(11, 2),
  travel numeric(11, 2)
);

create table org(
  id text,
  title text
);

create table org_type(
  id text,
  title text
);

