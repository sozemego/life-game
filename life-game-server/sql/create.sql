DROP TABLE IF EXISTS WORLD CASCADE;

CREATE TABLE WORLD (
  id INT GENERATED ALWAYS AS IDENTITY UNIQUE NOT NULL,
  user_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  deleted_at TIMESTAMP
);

DROP TABLE IF EXISTS TILE CASCADE;

CREATE TABLE TILE (
  tile_id INT NOT NULL PRIMARY KEY,
  x INT NOT NULL,
  y INT NOT NULL,
  world_id INT NOT NULL REFERENCES WORLD(id)
);