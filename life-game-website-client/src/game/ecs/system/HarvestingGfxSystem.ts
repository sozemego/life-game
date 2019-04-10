import { GameEngine } from '../../game-engine/GameEngine';
import { EntityEngine, EntitySystem } from '../EntityEngine';
import { GfxEngine } from '../../gfx-engine/GfxEngine';
import { TYPES } from '../component/types';
import { getEntities } from './utils';
import { Entity } from '../Entity';
import { Sprite } from 'three';
import { PhysicsComponent, ResourceHarvesterComponent } from "../component/FactoryRegistry";

export const createHarvestingGfxSystem = (
  gameEngine: GameEngine,
  entityEngine: EntityEngine,
  gfxEngine: GfxEngine,
): EntitySystem => {
  const types = [TYPES.PHYSICS, TYPES.HARVESTER];

  const progressBars: ProgressBarMap = {};

  const update = (delta: number) => {
    const entities = getEntities(entityEngine, types);

    entities.forEach(entity => updateEntity(entity, delta));
  };

  const updateEntity = (entity: Entity, delta: number) => {
    const physics = entity.getComponent(TYPES.PHYSICS) as PhysicsComponent;
    const progressBar = progressBars[entity.id];
    const {x, y, width, height} = physics;

    if (progressBar) {
      const harvester = entity.getComponent(TYPES.HARVESTER) as ResourceHarvesterComponent;
      const progress = harvester.harvestingProgress;

      const { bar } = progressBar;
      bar.visible = progress > 0;
      bar.position.x = x - (width * 0.2);
      bar.position.y = y + (height / 2);
      bar.scale.x = progress * width * 0.6;
      bar.scale.y = 0.1;

    } else {
      const [group, removeGroup] = gfxEngine.createGroup(2);
      const bar = gfxEngine.createSprite('progress_bar', { x, y, width, height }, group);
      bar.center.set(1, 1);
      bar.renderOrder = 1;
      bar.material.depthTest = false;
      progressBars[entity.id] = { bar };
    }
  };

  return {
    update,
  };
};

interface ProgressBarMap {
  [entityId: number]: ProgressBar;
}

interface ProgressBar {
  bar: Sprite;
}
