import { Mesh } from "three";

export interface Tile {
  x: number;
  y: number;
  mesh: Mesh | null;
}

export interface World {
  tiles: Tile[];
}
