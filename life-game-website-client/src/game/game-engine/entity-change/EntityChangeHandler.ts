import { EntityPhysicsChangeHandler } from './EntityPhysicsChangeHandler';
import { EntityHarvesterChangeHandler } from './EntityHarvesterChangeHandler';

export interface EntityChangeData {}

export type EntityChangeHandler = EntityPhysicsChangeHandler | EntityHarvesterChangeHandler;
