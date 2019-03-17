DELETE FROM ENTITY WHERE world_id = -1;

INSERT INTO ENTITY (name, world_id, physics, graphics) VALUES
(
 'WAREHOUSE_1', -1,
 '{"type":  "PHYSICS"}', '{"type":  "GRAPHICS"}'
);
