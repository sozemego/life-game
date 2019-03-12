import {
  AxesHelper,
  Box3,
  BoxGeometry,
  FrontSide,
  Geometry,
  GridHelper, Group,
  Mesh,
  MeshBasicMaterial,
  PerspectiveCamera,
  PlaneGeometry,
  Raycaster,
  Scene,
  Vector3,
  WebGLRenderer,
} from 'three';
import { createInputHandler } from './view/input-handler';
import Stats from 'stats-js';

/**
 * @param {number} tileSize
 * @returns Engine
 */
export const createEngine = (tileSize) => {

  const scene = new Scene();
  const width = window.innerWidth - 17;
  const height = window.innerHeight - 36 - 25;
  const aspect = width / height;
  const camera = new PerspectiveCamera(70, aspect, 1, 1000);
  camera.up.set(0, 0, 1);
  camera.position.y = -10;
  camera.position.z = 30;

  camera.lookAt(new Vector3(0, 0, 0));
  // const helper = new CameraHelper(camera);
  // scene.add(helper);

  const stats = new Stats();
  stats.showPanel(0);

  const axesHelper = new AxesHelper(5);
  scene.add(axesHelper);

  const renderer = new WebGLRenderer();
  renderer.setSize(window.innerWidth - 17, window.innerHeight - 36 - 25);

  const resize = () => {
    renderer.setSize(window.innerWidth - 17, window.innerHeight - 36 - 25);
    camera.aspect = (window.innerWidth - 17) / (window.innerHeight - 36 - 25);
    camera.updateProjectionMatrix();
  };

  window.addEventListener('resize', resize);


  const container = document.getElementById('game-container');
  container.append(renderer.domElement);
  container.appendChild(stats.dom);

  const box = new BoxGeometry(4, 4, 4);
  const material = new MeshBasicMaterial({ color: 0xff0000 });
  const cube = new Mesh(box, material);

  camera.updateProjectionMatrix();

  scene.add(cube);

  const inputHandler = createInputHandler();

  const pressedKeys = new Set();

  inputHandler.onKeyUp((key) => {
    pressedKeys.delete(key);
  });

  inputHandler.onKeyDown((key) => {
    pressedKeys.add(key);
  });

  inputHandler.onMouseWheel((delta) => {
    if (delta > 0) {
      camera.position.z += 1;
    } else {
      camera.position.z -= 1;
    }
  });

  inputHandler.onMouseMove((mouse) => {
    const rayCaster = new Raycaster();

    rayCaster.setFromCamera(mouse, camera);
    const intersections = rayCaster.intersectObjects(world.group.children);
    const intersection = (intersections.length) > 0 ? intersections[0] : null;
    console.log(intersection);
  });

  const engine = {
    running: false,
  };

  engine.start = () => {
    console.log('Starting game loop!');
    engine.running = true;
    animate();
  };

  const world = {
    tiles: {},
    group: new Group(),
  };

  const worldGeometry = new Geometry();
  let worldMesh = null;

  engine.setWorld = (newWorld) => {
    worldGeometry.dispose()

    console.log(newWorld);
    const tileGeometry = new PlaneGeometry(tileSize, tileSize, 1);
    const tileMaterial = new MeshBasicMaterial({ color: 0x00ff00, side: FrontSide });
    newWorld.tiles.forEach(tile => {
      const key = `${tile.x}:${tile.y}`;
      world[key] = tile;

      const mesh = new Mesh(tileGeometry, tileMaterial);
      mesh.position.x = tile.x * tileSize;
      mesh.position.y = tile.y * tileSize;
      mesh.position.z = 0;
      mesh.updateMatrix();
      // store the mesh so the individual tiles can still be found
      // among the merged geometry.
      tile.mesh = mesh;
      world.group.add(mesh);
      /**
       * The tile geometries are merged for performance reasons.
       * This is only temporary however, as doing this makes it into 'one' big plane
       * which will not be desirable later.
       */
      worldGeometry.merge(tileGeometry, mesh.matrix);
    });

    worldMesh = new Mesh(worldGeometry, tileMaterial);
    scene.add(worldMesh);

    const gridHelper = new GridHelper(50 * tileSize, 50);
    gridHelper.position.setZ(0.1);
    gridHelper.up.set(0, 0, 1);
    gridHelper.rotateX(Math.PI / 2);
    const box = new Box3().setFromObject(gridHelper);
    const width = box.max.x - box.min.x;
    const height = box.max.y - box.min.y;
    gridHelper.position.add(new Vector3((width / 2) - (tileSize / 2), 0, 0));
    gridHelper.position.add(new Vector3(0, (height / 2) - (tileSize / 2), 0));
    console.log(box);
    scene.add(gridHelper);
  };

  engine.stop = () => {
    console.log('Stopping engine');
    engine.running = false;
    window.removeEventListener('resize', resize);
    inputHandler.destroy();
  };

  engine.update = () => {

  };

  const animate = () => {
    if (!engine.running) {
      return;
    }
    requestAnimationFrame(animate);
    // stats.begin();
    engine.update();

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
      console.log(camera.position);
      console.log(cube.position);
    }

    camera.updateProjectionMatrix();
    // helper.update();
    renderer.render(scene, camera);

    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;
    cube.rotation.z += 0.01;

    // stats.end();
  };

  return engine;
};