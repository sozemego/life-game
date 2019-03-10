import {
  AxesHelper, Box3,
  BoxGeometry,
  CameraHelper, DoubleSide,
  FrontSide, GridHelper,
  Mesh,
  MeshBasicMaterial,
  PerspectiveCamera,
  PlaneGeometry,
  Scene, Vector3,
  WebGLRenderer,
} from 'three';
import { createLogger } from '../utils';

const LOG = createLogger('engine.js');

/**
 * @returns Engine
 */
export const createEngine = () => {

  const scene = new Scene();
  const width = window.innerWidth;
  const height = window.innerHeight;
  const aspect = width / height;
  const camera = new PerspectiveCamera(75, aspect, 1, 1000);
  camera.up.set(0, 0, 1);
  camera.position.x = 30;
  camera.position.y = 0;
  camera.position.z = 5;
  const helper = new CameraHelper(camera);
  scene.add(helper);

  const axesHelper = new AxesHelper(5);
  scene.add(axesHelper);

  const renderer = new WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);

  const resize = () => {
    renderer.setSize(window.innerWidth, window.innerHeight);
    camera.aspect = aspect;
    camera.updateProjectionMatrix();
  };

  window.addEventListener('resize', resize);

  const pressedKeys = new Set();

  const onKeyDown = (event) => {
    const { key } = event;
    pressedKeys.add(key);
  };

  window.addEventListener('keydown', onKeyDown);

  const onKeyUp = (event) => {
    const { key } = event;
    pressedKeys.delete(key);
  }

  window.addEventListener('keyup', onKeyUp);

  const container = document.getElementById('game-container');
  container.append(renderer.domElement);

  const box = new BoxGeometry(4, 4, 4);
  const material = new MeshBasicMaterial({ color: 0xff0000 });
  const cube = new Mesh(box, material);

  camera.updateProjectionMatrix();

  scene.add(cube);

  const engine = {
    running: false,
  };

  engine.start = () => {
    LOG('Starting game loop!');
    engine.running = true;
    animate();
  };

  const world = {
    tileSize: 12,
    tiles: {},
  };

  engine.setWorld = (newWorld) => {
    LOG(newWorld);
    newWorld.tiles.forEach(tile => {
      const key = `${tile.x}:${tile.y}`;
      world[key] = tile;

      const geometry = new PlaneGeometry(world.tileSize, world.tileSize, 12);
      const material = new MeshBasicMaterial({ color: 0x00ff00, side: DoubleSide });
      const plane = new Mesh(geometry, material);
      plane.position.x = tile.x * world.tileSize;
      plane.position.y = tile.y * world.tileSize;
      plane.position.z = 0;
      tile.plane = plane;
      scene.add(plane);

    });

    const gridHelper = new GridHelper(50 * 12, 50);
    gridHelper.position.setZ(0.1);
    gridHelper.up.set(0, 0, 1);
    gridHelper.rotateX(Math.PI / 2);
    const box = new Box3().setFromObject(gridHelper);
    const width = box.max.x - box.min.x;
    const height = box.max.y - box.min.y;
    gridHelper.position.add(new Vector3((width / 2) - 6, 0, 0));
    gridHelper.position.add(new Vector3(0, (height / 2) - 6, 0));
    LOG(box);
    scene.add(gridHelper);
  };

  engine.stop = () => {
    engine.running = false;
    window.removeEventListener('resize', resize);
    window.removeEventListener('keydown', onKeyDown);
    window.removeEventListener('keyup', onKeyUp);
  };

  const animate = () => {
    if (!engine.running) {
      return;
    }
    requestAnimationFrame(animate);

    if (pressedKeys.has('a')) {
      camera.position.x -= 1;
    }
    if (pressedKeys.has('d')) {
      camera.position.x += 1;
    }
    if (pressedKeys.has('s')) {
      camera.position.y -= 1;
    }
    if (pressedKeys.has('w')) {
      camera.position.y += 1;
    }

    if (pressedKeys.has('q')) {
      camera.position.z += 1;
    }
    if (pressedKeys.has('e')) {
      camera.position.z -= 1;
    }

    if (pressedKeys.has('g')) {
      LOG(camera.position);
      LOG(cube.position);
    }

    camera.updateProjectionMatrix();
    helper.update();
    renderer.render(scene, camera);

    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;
    cube.rotation.z += 0.01;

  };

  return engine;
};