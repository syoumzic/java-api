create table `users` (`id` varchar(45) not null primary key,
 `group` varchar(20) not null, `useIndiv` tinyint not null default 1,
 `time` int not null default 10, `notification` tinyint not null default 0,
 `existDL` tinyint not null default 0,
 `deadline_shift` int not null default 1);