import { EntityPhysicsChangeHandler } from './EntityPhysicsChangeHandler';
import { EntityHarvesterChangeHandler } from './EntityHarvesterChangeHandler';
import { EntityStorageChangeHandler } from './EntityStorageChangeHandler';

export type EntityChangeHandler =
  | EntityPhysicsChangeHandler
  | EntityHarvesterChangeHandler
  | EntityStorageChangeHandler;
