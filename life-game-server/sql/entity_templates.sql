DELETE FROM ENTITY WHERE world_id = -1;

INSERT INTO ENTITY (name, world_id, physics, graphics) VALUES
(
  'WAREHOUSE_1', -1,
  '{"type":  "PHYSICS", "width":  3, "height":  3}', '{"type":  "GRAPHICS", "texture": "WAREHOUSE_1"}'
);

INSERT INTO ENTITY (name, world_id, physics, graphics, resource_provider) VALUES
(
  'FOREST_1', -1,
  '{"type": "PHYSICS", "width":  2, "height":  2}',
  '{"type": "GRAPHICS", "texture": "FOREST_1"}',
  '{"type": "RESOURCE_PROVIDER", "resource": "WOOD"}'
);

INSERT INTO ENTITY (name, world_id, physics, graphics, harvester, movement, storage) VALUES
(
  'WORKER_1', -1,
  '{"type": "PHYSICS", "width":  1, "height":  1}',
  '{"type": "GRAPHICS", "texture": "WORKER_1"}',
  '{"type": "HARVESTER", "harvestingTime":  5}', '{"type": "MOVEMENT", "speed": 0.25}',
  '{"type": "STORAGE", "capacity": 1}'
);