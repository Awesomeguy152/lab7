CREATE TABLE MusicBand
(
    id                     serial PRIMARY KEY,
    name                   VARCHAR(255)     NOT NULL,
    coordinates_x          double precision NOT NULL,
    coordinates_y          double precision NOT NULL,
    creation_date          date             NOT NULL,
    number_of_participants INT              NOT NULL,
    genre                  VARCHAR(255)     NOT NULL,
    front_man_name         VARCHAR(255)     NOT NULL,
    front_man_birthday     DATE             NOT NULL,
    front_man_weight       INT              NOT NULL,
    front_man_eye_color    VARCHAR(255),
    owner_id               int REFERENCES users (id)
);

CREATE TABLE users
(
    id       serial PRIMARY KEY,
    login    varchar(70) UNIQUE,
    password text
);