import { EntityPhysicsChangeHandler } from './EntityPhysicsChangeHandler';
import { EntityHarvesterChangeHandler } from './EntityHarvesterChangeHandler';

export type EntityChangeHandler = EntityPhysicsChangeHandler | EntityHarvesterChangeHandler;
