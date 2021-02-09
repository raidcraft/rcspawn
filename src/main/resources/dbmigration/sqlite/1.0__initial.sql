-- apply changes
create table rcspawn_players (
  id                            varchar(40) not null,
  name                          varchar(255),
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rcspawn_players primary key (id)
);

create table rcspawn_spawns (
  id                            varchar(40) not null,
  name                          varchar(255),
  world                         varchar(40),
  x                             double not null,
  y                             double not null,
  z                             double not null,
  pitch                         float not null,
  yaw                           float not null,
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rcspawn_spawns primary key (id)
);

create table rcspawn_history (
  id                            varchar(40) not null,
  player_id                     varchar(40) not null,
  spawn_id                      varchar(40) not null,
  last_execution                timestamp,
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rcspawn_history primary key (id),
  foreign key (player_id) references rcspawn_players (id) on delete restrict on update restrict,
  foreign key (spawn_id) references rcspawn_spawns (id) on delete restrict on update restrict
);

create index ix_rcspawn_spawns_name on rcspawn_spawns (name);
create index ix_rcspawn_spawns_world on rcspawn_spawns (world);
