DELETE FROM ENTITY WHERE world_id = -1;

INSERT INTO ENTITY (id, world_id, physics, graphics) VALUES
(
 'WAREHOUSE_1', -1,
 '{"type":  "PHYSICS"}', '{"type":  "GRAPHICS"}'
);
