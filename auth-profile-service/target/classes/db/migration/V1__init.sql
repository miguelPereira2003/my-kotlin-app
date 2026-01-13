create table if not exists users (
  id uuid primary key,
  email varchar(200) unique not null,
  password_hash varchar(255) not null,
  name varchar(200) not null,
  roles varchar(250) not null,
  status varchar(20) not null,
  created_at timestamp not null
);
