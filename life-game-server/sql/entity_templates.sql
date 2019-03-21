DELETE FROM ENTITY WHERE world_id = -1;

INSERT INTO ENTITY (name, world_id, physics, graphics) VALUES
(
 'WAREHOUSE_1', -1,
 '{"type":  "PHYSICS"}', '{"type":  "GRAPHICS", "texture": "WAREHOUSE_1"}'
);

INSERT INTO ENTITY (name, world_id, physics, graphics, resource_provider) VALUES
(
 'FOREST_1', -1,
 '{"type":  "PHYSICS"}', '{"type":  "GRAPHICS", "texture": "FOREST_1"}', '{"type": "RESOURCE_PROVIDER", "resource": "WOOD"}'
);