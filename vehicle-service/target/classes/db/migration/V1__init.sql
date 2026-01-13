create table if not exists vehicles (
  id uuid primary key,
  owner_user_id uuid not null,
  brand varchar(100) not null,
  model varchar(100) not null,
  plate varchar(30) not null,
  fuel_type varchar(20) not null,
  consumption_per_100 double precision not null,
  seats int not null,
  created_at timestamp not null
);
create index if not exists idx_vehicles_owner on vehicles(owner_user_id);
