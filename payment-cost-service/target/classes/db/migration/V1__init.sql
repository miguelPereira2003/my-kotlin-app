create table if not exists cost_breakdowns (
  id uuid primary key,
  trip_id uuid unique not null,
  fuel_cost double precision not null,
  toll_cost double precision not null,
  total_cost double precision not null,
  cost_per_person double precision not null,
  currency varchar(10) not null,
  created_at timestamp not null
);

create table if not exists payments (
  id uuid primary key,
  trip_id uuid not null,
  payer_user_id uuid not null,
  amount double precision not null,
  status varchar(20) not null,
  created_at timestamp not null
);

create index if not exists idx_payments_trip on payments(trip_id);
create index if not exists idx_payments_payer on payments(payer_user_id);
