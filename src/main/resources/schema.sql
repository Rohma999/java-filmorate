CREATE TABLE IF NOT EXISTS films (
id bigint   GENERATED BY DEFAULT AS IDENTITY,
name varchar   NOT NULL,
description varchar(200)   NOT NULL,
release_date date   NOT NULL,
duration int   NOT NULL,
rating_id int   NOT NULL,
CONSTRAINT pk_films PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rating_MPA (
id int   GENERATED BY DEFAULT AS IDENTITY,
name varchar(5) unique  NOT NULL,
CONSTRAINT pk_rating_MPA PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genres (
id int   GENERATED BY DEFAULT AS IDENTITY,
name varchar   NOT NULL,
CONSTRAINT pk_genre PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id bigint   NOT NULL,
genre_id int   NOT NULL ,
CONSTRAINT pk_film_genre PRIMARY KEY (film_id,genre_id)
);

CREATE TABLE IF NOT EXISTS users (
id bigint   GENERATED BY DEFAULT AS IDENTITY,
email varchar   NOT NULL,
login varchar   NOT NULL,
name varchar   NOT NULL,
birthday date   NOT NULL,
CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS likes (
film_id bigint   NOT NULL ,
user_id bigint   NOT NULL,
CONSTRAINT pk_like PRIMARY KEY (film_id,user_id)
);

CREATE TABLE IF NOT EXISTS friends (
user_id bigint   NOT NULL ,
friend_id bigint   NOT NULL ,
CONSTRAINT pk_friend PRIMARY KEY (user_id,friend_id)
);


ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_user_id FOREIGN KEY(user_id)
REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_friend_id FOREIGN KEY(friend_id)
REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_film_id FOREIGN KEY(film_id)
REFERENCES films (id) ON DELETE CASCADE;

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_user_id FOREIGN KEY(user_id)
REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE films ADD CONSTRAINT IF NOT EXISTS fk_film_rating_id FOREIGN KEY(rating_id)
REFERENCES rating_mpa (id);

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS fk_film_genre_film_id FOREIGN KEY(film_id)
REFERENCES films (id) ON DELETE CASCADE;

ALTER TABLE film_genre ADD CONSTRAINT IF NOT EXISTS fk_film_genre_genre_id FOREIGN KEY(genre_id)
REFERENCES genres (id) ON DELETE CASCADE;