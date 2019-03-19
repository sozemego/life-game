import {
  AmbientLight,
  AxesHelper, BasicShadowMap,
  Box3,
  BoxGeometry, CameraHelper,
  Color, DirectionalLight, DirectionalLightHelper,
  FrontSide,
  Geometry,
  GridHelper,
  Group,
  Mesh,
  MeshBasicMaterial, MeshLambertMaterial, MeshPhongMaterial,
  MeshStandardMaterial, PCFShadowMap,
  PerspectiveCamera,
  PlaneGeometry,
  Raycaster, RepeatWrapping,
  Scene, Sprite, SpriteMaterial,
  TextureLoader,
  Vector3,
  WebGLRenderer,
} from 'three';
import Stats from 'stats-js';

/**
 * @param {number} tileSize
 * @returns Engine
 */
export const createEngine = (inputHandler, tileSize) => {

  const scene = new Scene();
  const width = window.innerWidth - 17;
  const height = window.innerHeight - 36 - 25;
  const aspect = width / height;
  const camera = new PerspectiveCamera(70, aspect, 1, 1000);
  camera.up.set(0, 0, 1);
  camera.position.y = -5 * tileSize;
  camera.position.z = 10 * tileSize;

  camera.lookAt(new Vector3(0, 0, 0));
  camera.position.z -= 5;
  camera.position.x += 25;
  camera.position.y += 25;

  const stats = new Stats();
  stats.showPanel(0);

  const axesHelper = new AxesHelper(5);
  scene.add(axesHelper);

  const textureLoader = new TextureLoader();

  const light = new DirectionalLight( 0xffffff, 0.5 );
  light.position.set(0, 0, 10);
  light.castShadow = true;
  light.shadow.camera.up.set(0, 0, 1);
  light.shadow.mapSize.width = 1024 * 2;
  light.shadow.mapSize.height = 1024 * 2;
  light.userData['phase'] = 'UP';
  scene.add(light);
  scene.add(light.target);

  const cameraHelper = new CameraHelper(light.shadow.camera);
  // scene.add(cameraHelper);

  const ambientLight = new AmbientLight(0xffffff, 0.25);
  scene.add(ambientLight);

  const helper = new DirectionalLightHelper(light, 5);
  // scene.add(helper);

  const renderer = new WebGLRenderer();
  renderer.shadowMap.enabled = true;
  renderer.shadowMap.type = PCFShadowMap;

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

  const shadowCubes = [];
  for(let i = 0; i < 50; i++) {
    const shadowBox = new BoxGeometry(Math.random() * tileSize, Math.random() * tileSize, Math.random() * tileSize);
    const material = new MeshPhongMaterial({color: Math.random() * 255 * 255 * 255});
    const shadowCube = new Mesh(shadowBox, material);
    shadowCube.castShadow = true;
    shadowCube.position.set(Math.random() * 50, Math.random() * 50, Math.random() * 5);
    shadowCube.updateMatrix();
    // scene.add(shadowCube);
    shadowCubes.push(shadowCube);
  }

  camera.updateProjectionMatrix();

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

  let intersectedTile = null;
  inputHandler.onMouseMove((mouse) => {
    const rayCaster = new Raycaster();
    camera.updateMatrixWorld();
    rayCaster.setFromCamera(mouse, camera);
    const tileIntersections = rayCaster.intersectObjects(world.group.children);
    const tileIntersection = (tileIntersections.length) > 0 ? tileIntersections[0] : null;

    if (tileIntersection) {
      const name = (intersectedTile && intersectedTile.name) || "";
      if (name !== tileIntersection.object.name) {
        if (intersectedTile) {
          scene.remove(intersectedTile);
        }
        intersectedTile = tileIntersection.object.clone();
        intersectedTile.material.transparent = true;
        intersectedTile.material.opacity = 0.75;
        intersectedTile.position.z = 0.05;
        intersectedTile.userData['highlightPhase'] = 'DOWN';
        intersectedTile.material.color = new Color('red');
        scene.add(intersectedTile);
      }
    } else {
      scene.remove(intersectedTile);
      intersectedTile = null;
    }
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
    entities: new Group()
  };

  const worldGeometry = new Geometry();
  let worldMesh = null;

  engine.setWorld = (newWorld) => {
    worldGeometry.dispose()
    // return;
    console.log(newWorld);
    const texture = textureLoader.load('textures/medievalTile_57.png');
    const tileGeometry = new PlaneGeometry(tileSize, tileSize, 12, 12);
    const worldMaterial = new MeshLambertMaterial({ map: texture, color: 0x00ff00, side: FrontSide });

    newWorld.tiles.forEach(tile => {
      const key = `${tile.x}:${tile.y}`;
      world[key] = tile;

      const tileMaterial = new MeshLambertMaterial({ map: texture, color: 0x00ff00, side: FrontSide });

      const mesh = new Mesh(tileGeometry, tileMaterial);
      mesh.position.x = tile.x * tileSize;
      mesh.position.y = tile.y * tileSize;
      mesh.position.z = 0;
      mesh.up.set(0, 0, 1);
      mesh.updateMatrixWorld();
      mesh.receiveShadow = true;
      // store the mesh so the individual tiles can still be found
      // among the merged geometry.
      tile.mesh = mesh;
      mesh.name = key;
      world.group.add(mesh);
      /**
       * The tile geometries are merged for performance reasons.
       * This is only temporary however, as doing this makes it into 'one' big plane
       * which will not be desirable later.
       */
      worldGeometry.merge(tileGeometry, mesh.matrix);
    });

    worldMesh = new Mesh(worldGeometry, worldMaterial);
    worldMesh.receiveShadow = true;
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
    scene.add(gridHelper);

    light.target.position.set(50 * tileSize / 2, 50 * tileSize / 2, 0);
    light.target.updateMatrixWorld();

    light.position.set(50 * tileSize / 2, 50 * tileSize / 2, 10);
    light.shadow.camera.left = -50 * tileSize / 2;
    light.shadow.camera.right = 50 * tileSize / 2;
    light.shadow.camera.bottom = -50 * tileSize / 2;
    light.shadow.camera.top = 50 * tileSize / 2;
    light.shadow.camera.near = 0;
    light.shadow.camera.far = 15;
  };

  const entities = {};

  engine.addEntity = (entity) => {
    console.log(entity);
    entities[entity.id] = entity;
    const textureName = entity.graphics.texture;
    const { x, y, width, height } = entity.physics;
    const texture = textureLoader.load(`textures/${textureName}.png`);
    texture.wrapS = RepeatWrapping;
    texture.repeat.x = - 1;

    const entityMaterial = new SpriteMaterial({ map: texture, color: 0xffffff, rotation: Math.PI });
    const sprite = new Sprite(entityMaterial);
    sprite.position.x = x * tileSize;
    sprite.position.y = y * tileSize;
    sprite.position.z = 0.3;

    entity.graphics['sprite'] = sprite;
    scene.add(sprite);
  };

  engine.stop = () => {
    console.log('Stopping engine');
    engine.running = false;
    window.removeEventListener('resize', resize);
  };

  engine.update = () => {

  };

  const animate = () => {
    if (!engine.running) {
      return;
    }
    requestAnimationFrame(animate);
    stats.begin();
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
    }

    if (intersectedTile) {
      const highlightPhase = intersectedTile.userData['highlightPhase'];
      const opacity = intersectedTile.material.opacity;
      let nextOpacity = opacity;
      let nextPhase = highlightPhase;
      if (highlightPhase === 'DOWN') {
        nextOpacity -= 0.01;
      } else if (highlightPhase === 'UP') {
        nextOpacity += 0.01;
      }
      if (nextOpacity <= 0.4) {
        nextPhase = 'UP';
      } else if (nextOpacity >= 0.75) {
        nextPhase = 'DOWN';
      }
      intersectedTile.material.opacity = nextOpacity;
      intersectedTile.userData['highlightPhase'] = nextPhase;
    }

    helper.update()
    const lightPhase = light.userData['phase'];
    if (lightPhase === 'UP') {
      // light.intensity += 0.01;
    } else if (lightPhase === 'DOWN') {
      // light.intensity -= 0.01;
    }
    if (light.intensity >= 2) {
      light.userData['phase'] = 'DOWN';
    } else if (light.intensity <= 1) {
      light.userData['phase'] = 'UP';
    }

    light.shadow.camera.updateProjectionMatrix();
    // and now update the camera helper we're using to show the light's shadow camera
    cameraHelper.update();
    helper.update();
    camera.updateProjectionMatrix();
    renderer.render(scene, camera);

    shadowCubes.forEach(shadowCube => {
      shadowCube.rotation.x += 0.01;
      shadowCube.rotation.y += 0.01;
      shadowCube.rotation.z += 0.01;
    })

    stats.end();
  };

  return engine;
};