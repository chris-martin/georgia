drop schema if exists georgia cascade;
create schema georgia;
set search_path to georgia, public;

create table payments(
  person_name varchar,
  job_title varchar,
  salary numeric(11, 2),
  travel numeric(11, 2),
  org_id varchar,
  year smallint
);

create table orgs(
  id varchar,
  title varchar,
  org_type_id varchar,
  year smallint
);

create table org_types(
  id varchar,
  title varchar
);
