drop schema if exists georgia cascade;
create schema georgia;
set search_path to georgia, public;

create table people(
  person_name text,
  job_title text,
  salary numeric(11, 2),
  travel numeric(11, 2),
  org_id text
);

create table orgs(
  id text,
  title text,
  org_type_id text,
  year text
);

create table org_types(
  id text,
  title text
);

