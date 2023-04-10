MERGE INTO genres
USING VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик')
AS inc(name)
ON genres.name  = inc.name
WHEN NOT MATCHED THEN INSERT (name) VALUES (inc.name);

MERGE INTO rating_mpa
USING VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17')
AS inc(name)
ON rating_mpa.name  = inc.name
WHEN NOT MATCHED THEN INSERT (name) VALUES (inc.name)