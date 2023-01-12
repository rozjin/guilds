CREATE TABLE IF NOT EXISTS lands (
                       section_id integer PRIMARY KEY AUTOINCREMENT,
                       guild_id integer,
                       user_id varchar,
                       region_id integer,
                       x_pos integer,
                       z_pos integer
);