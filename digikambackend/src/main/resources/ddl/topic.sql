create Table Topic (
    id int not null AUTO_INCREMENT,
    tagId int not null,
    year int not null,
    title varchar(255) not null,
    content longtext not null,
    search longtext not null,
    primary key (id),
    foreign key (tagId) references Tags (id)
)