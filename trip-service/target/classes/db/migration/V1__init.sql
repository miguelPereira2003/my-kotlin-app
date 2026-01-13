create table if not exists trips (
  id uuid primary key,
  driver_user_id uuid not null,
  vehicle_id uuid,
  origin_text varchar(255) not null,
  destination_text varchar(255) not null,
  origin_lat double precision,
  origin_lng double precision,
  dest_lat double precision,
  dest_lng double precision,
  departure_time timestamp not null,
  available_seats int not null,
  status varchar(20) not null,
  estimated_distance_km double precision,
  estimated_duration_min int,
  toll_cost double precision not null default 0,
  created_at timestamp not null
);
create index if not exists idx_trips_driver on trips(driver_user_id);

create table if not exists bookings (
  id uuid primary key,
  trip_id uuid not null,
  passenger_user_id uuid not null,
  seats_requested int not null,
  status varchar(20) not null,
  created_at timestamp not null
);
create index if not exists idx_bookings_trip on bookings(trip_id);
