create table comments
(
    id       int8         not null,
    created  timestamp(6),
    message  text         not null,
    username varchar(255) not null,
    user_id  int8         not null,
    post_id  int8,
    primary key (id)
);
create table images
(
    id      int8         not null,
    image   bytea,
    name    varchar(255) not null,
    post_id int8,
    user_id int8,
    primary key (id)
);
create table post_liked_users
(
    post_id     int8 not null,
    liked_users varchar(255)
);
create table posts
(
    id       int8 not null,
    created  timestamp(6),
    likes    integer,
    location varchar(255),
    text     varchar(255),
    title    varchar(255),
    user_id  int8,
    primary key (id)
);
create table users
(
    id        int8     not null,
    bio       text,
    created   timestamp(6),
    email     varchar(255),
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    username  varchar(255),
    password  varchar(255),
    role      varchar(255),
    primary key (id)
);
alter table if exists users
    add constraint email_unique unique (email);
alter table if exists users
    add constraint username_unique unique (username);
alter table if exists comments
    add constraint comments_fk_post foreign key (post_id) references posts;
alter table if exists post_liked_users
    add constraint post_liked_users_fk_post foreign key (post_id) references posts;
alter table if exists posts
    add constraint posts_fk_user foreign key (user_id) references users;