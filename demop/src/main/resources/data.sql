delete from scientific_area;
delete from user;
delete from role;
delete from privilege;
delete from roles_privileges;
delete from user_roles;
insert into scientific_area (id, name) values (26, 'Radiology');
insert into scientific_area (id, name) values (13, 'Biology');
insert into scientific_area (id, name) values (30, 'Computer Science');
insert into scientific_area (id, name) values (50, 'Automotive engineering');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (2158, 'Admin', 'Admin', 'Nis', 'Srbija', 'admin','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'isa.fakultet@gmail.com','mr', false,true,true, 'admin');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (563, 'Ana', 'Antic', 'Nis', 'Srbija', 'edi1','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'isa.fakultet@gmail.com','mr', false,true,true, 'editor');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (885, 'Ivana', 'Trisic', 'Nis', 'Srbija', 'edi5','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'isa.fakultet@gmail.com','mr', false,true,false, 'editor');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (889, 'Pera', 'Mitic', 'Nis', 'Srbija', 'rev1','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'rev1@gmail.com','mr', false,true,true, 'reviewer');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (5215, 'Olja', 'Vesic', 'Nis', 'Srbija', 'edi2','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'gavric.marinaa@gmail.com','mr', false,true,true, 'editor');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (4566, 'Aca', 'Nikolic', 'Nis', 'Srbija', 'rev2','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'rev2@gmail.com','mr', false,true, true,'reviewer');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (7005, 'Dejan', 'Lazic', 'Nis', 'Srbija', 'edi3','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'editor3@gmail.com','mr', false,true,true, 'editor');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (85656, 'Jeca', 'Nesic', 'Nis', 'Srbija', 'rev3','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'rev3@gmail.com','mr', false,true,true, 'reviewer');
insert into user (id, name, surname, city, country, username, password, mail, title, rev_flag, active, payed,type) values (999, 'Tea', 'Maric', 'Nis', 'Srbija', 'edi4','$2a$10$ifANXgSrjBgprG81FsGmLu5xuE4CkcmLXCi335AM5fDI2faPRz1dO',  'editor4@gmail.com','mr', false,true,true, 'editor');

insert into role values (1, 'ADMIN');
insert into role values (2, 'USER');
insert into role values (3, 'REVIEWER');
insert into role values (4, 'EDITOR');
insert into role values (5, 'AUTHOR');

insert into privilege values (1, 'APPROVE_REVIEWER');
insert into privilege values (2, 'CREATING_MAGAZINE');
insert into privilege values (3, 'CHECKING_MAGAZINE');

insert into roles_privileges values (1, 1);
insert into roles_privileges values (4, 2);
insert into roles_privileges values (1, 3);

insert into user_roles values (2158, 1);
insert into user_roles values (563, 4);
insert into user_roles values (885, 4);
insert into user_roles values (5215, 4);
insert into user_roles values (999, 4);
insert into user_roles values (7005, 4);
insert into user_roles values (889, 3);
insert into user_roles values (4566, 3);

delete from user_areas;
insert into user_areas values (999, 13);
insert into user_areas values (7005, 13);
insert into user_areas values (7005, 30);
insert into user_areas values (7005, 50);
insert into user_areas values (7005, 26);
insert into user_areas values (563, 13);
insert into user_areas values (563, 30);
insert into user_areas values (563, 50);
insert into user_areas values (563, 26);
insert into user_areas values (999, 30);
insert into user_areas values (889, 30);