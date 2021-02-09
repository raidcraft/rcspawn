-- apply changes
create table rcspawn_players (
  id                            uuid not null,
  name                          varchar(255),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_rcspawn_players primary key (id)
);

create table rcspawn_spawns (
  id                            uuid not null,
  name                          varchar(255),
  world                         uuid,
  x                             float not null,
  y                             float not null,
  z                             float not null,
  pitch                         float not null,
  yaw                           float not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_rcspawn_spawns primary key (id)
);

create table rcspawn_history (
  id                            uuid not null,
  player_id                     uuid not null,
  spawn_id                      uuid not null,
  last_execution                timestamptz,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_rcspawn_history primary key (id)
);

create index ix_rcspawn_spawns_name on rcspawn_spawns (name);
create index ix_rcspawn_spawns_world on rcspawn_spawns (world);
create index ix_rcspawn_history_player_id on rcspawn_history (player_id);
alter table rcspawn_history add constraint fk_rcspawn_history_player_id foreign key (player_id) references rcspawn_players (id) on delete restrict on update restrict;

create index ix_rcspawn_history_spawn_id on rcspawn_history (spawn_id);
alter table rcspawn_history add constraint fk_rcspawn_history_spawn_id foreign key (spawn_id) references rcspawn_spawns (id) on delete restrict on update restrict;

