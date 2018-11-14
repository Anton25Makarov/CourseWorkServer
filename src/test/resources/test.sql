SELECT *
FROM users;

insert into game (sport, date, address_id, team_first_id, team_second_id)
values ('Баскетбол', '13.11.2018', 1, 3, 4);

insert into referee_has_game (referee_id, game_id)
VALUES (2, 1);

insert into referee (name, surname, age, sport, qualification_id, address_id)
VALUES ('Анатолий', 'Купревич', 48, 'Футбол', 1, 1);


select *
from referee_has_game
       inner join game g on referee_has_game.game_id = g.id
       inner join referee r on referee_has_game.referee_id = r.id

insert into users (login, password)
VALUES ('11', '11');

