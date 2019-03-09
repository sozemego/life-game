import {
  BoxGeometry,
  CameraHelper,
  FrontSide,
  Mesh,
  MeshBasicMaterial, OrthographicCamera,
  PerspectiveCamera,
  PlaneGeometry,
  Scene, Vector3,
  WebGLRenderer,
} from 'three';

export const createGame = (client) => {

  const scene = new Scene();
  const width = window.innerWidth;
  const height = window.innerHeight;
  const aspect = width / height;
  const frustumSize = 100;
  const camera = new PerspectiveCamera(75, aspect, 1, 1000);
  camera.up = new Vector3(0, 0, 1);
  camera.position.x = 30;
  camera.position.y = 0;
  camera.position.z = 5;
  const helper = new CameraHelper(camera);
  scene.add(helper);

  const renderer = new WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);

  const resize = () => {
    renderer.setSize(window.innerWidth, window.innerHeight);
    camera.aspect = aspect;
    camera.updateProjectionMatrix();
  };

  window.addEventListener('resize', resize);

  const pressedKeys = new Set();

  window.addEventListener('keydown', (event) => {
    const { key } = event;
    pressedKeys.add(key);
  });

  window.addEventListener('keyup', (event) => {
    const { key } = event;
    pressedKeys.delete(key);
  });

  const container = document.getElementById('game-container');
  container.append(renderer.domElement);

  const box = new BoxGeometry(4, 4, 4);
  const material = new MeshBasicMaterial({ color: 0xff0000 });
  const cube = new Mesh(box, material);

  camera.updateProjectionMatrix();

  scene.add(cube);

  const game = {
    running: false,
  };

  game.start = () => {
    console.log('Starting game loop!');
    game.running = true;
    animate();
  };

  game.destroy = () => {
    game.running = false;
    window.removeEventListener('resize', resize);
  };

  const animate = () => {
    if (!game.running) {
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

    if(pressedKeys.has('g')) {
      console.log(camera.position);
      console.log(cube.position);
      camera.lookAt(cube.position);
    }

    // camera.lookAt(cube.position);

    camera.updateProjectionMatrix();
    helper.update();
    renderer.render(scene, camera);

    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;
    cube.rotation.z += 0.01;
  };

  const world = {
    tileSize: 12,
    tiles: {},
  };

  game.setWorld = (newWorld) => {
    console.log(newWorld);
    newWorld.tiles.forEach(tile => {
      return;
      const key = `${tile.x}:${tile.y}`;
      world[key] = tile;

      const geometry = new PlaneGeometry(world.tileSize, world.tileSize, 12);
      const material = new MeshBasicMaterial({ color: 0x00ff00, side: FrontSide });
      const plane = new Mesh(geometry, material);
      tile.plane = plane;
      scene.add(plane);

    });
  };

  return game;
};