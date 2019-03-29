import {
  AmbientLight,
  AxesHelper,
  Box3,
  BoxGeometry,
  BoxHelper,
  CameraHelper,
  Color,
  DirectionalLight,
  DirectionalLightHelper,
  FrontSide,
  Geometry,
  GridHelper,
  Group,
  Mesh,
  MeshLambertMaterial,
  MeshPhongMaterial,
  OrthographicCamera,
  PCFShadowMap,
  PerspectiveCamera,
  PlaneGeometry,
  Raycaster,
  RepeatWrapping,
  Scene,
  Sprite,
  SpriteMaterial,
  TextureLoader,
  Vector2,
  Vector3,
  WebGLRenderer,
} from 'three';
import Stats from 'stats-js';

export const createEngine = (inputHandler, tileSize) => {
  const scene = new Scene();
  const spriteScene = new Scene();
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

  const light = new DirectionalLight(0xffffff, 0.5);
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

  camera.updateProjectionMatrix();

  const boxHelpers = new Group();
  spriteScene.add(boxHelpers);

  const pressedKeys = new Set();

  let boxHelpersEnabled = true;
  let gridHelperEnabled = true;
  let gridHelper = null;

  inputHandler.onKeyUp(key => {
    console.log(`Key up      ${key}`);
    if (key === '0') {
      boxHelpersEnabled = !boxHelpersEnabled;
    }
    if (key === '9') {
      gridHelperEnabled = !gridHelperEnabled;
    }
    pressedKeys.delete(key);
  });

  inputHandler.onKeyDown(key => {
    console.log(`Key down    ${key}`);
    pressedKeys.add(key);
  });

  inputHandler.onMouseWheel(delta => {
    if (delta > 0) {
      camera.position.z += 1;
    } else {
      camera.position.z -= 1;
    }
  });

  let clickedSprite = null;

  inputHandler.onMouseUp(mouse => {
    if (intersectedSprite) {
      clickedSprite = intersectedSprite;
    } else {
      clickedSprite = null;
    }
  });

  let intersectedTile = null;
  let intersectedSprite = null;
  inputHandler.onMouseMove(mouse => {
    const rayCaster = new Raycaster();
    camera.updateMatrixWorld();
    rayCaster.setFromCamera(mouse, camera);
    const tileIntersections = rayCaster.intersectObjects([]);
    const tileIntersection = tileIntersections.length > 0 ? tileIntersections[0] : null;
    const spriteIntersections = rayCaster.intersectObjects(world.sprites.children);
    // const spriteIntersections = [];
    const spriteIntersection = spriteIntersections[0] || null;

    if (tileIntersection && !spriteIntersection) {
      const name = (intersectedTile && intersectedTile.name) || '';
      if (name !== tileIntersection.object.name) {
        if (intersectedTile) {
          scene.remove(intersectedTile);
        }
        const texture = textureLoader.load('textures/medievalTile_57.png');
        const tileMaterial = new MeshLambertMaterial({
          map: texture,
          color: 0x00ff00,
          side: FrontSide,
          transparent: true,
        });
        intersectedTile = tileIntersection.object.clone();
        intersectedTile.material = tileMaterial;
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

    if (spriteIntersection) {
      const name = (intersectedSprite && intersectedSprite.name) || null;
      if (name !== spriteIntersection.object.name) {
        intersectedSprite = spriteIntersection.object;
        intersectedSprite['userData']['selected'] = true;
      }
    } else {
      if (intersectedSprite) {
        intersectedSprite['userData']['selected'] = false;
      }
      intersectedSprite = null;
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
    tilesGroup: new Group(),
    sprites: new Group(),
    groups: [],
  };

  const worldGeometry = new Geometry();
  let worldMesh = null;

  engine.setWorld = newWorld => {
    const t1 = performance.now();
    worldGeometry.dispose();
    console.log(newWorld);
    const texture = textureLoader.load('textures/medievalTile_57.png');
    const tileGeometry = new PlaneGeometry(tileSize, tileSize, 1, 1);
    const worldMaterial = new MeshLambertMaterial({
      map: texture,
      color: 0x00ff00,
      side: FrontSide,
      opacity: 1,
      transparent: true,
    });

    newWorld.tiles
      .sort((a, b) => a.x - b.x)
      .sort((a, b) => a.y - b.y)
      .forEach((tile, index) => {
        const key = `${tile.x}:${tile.y}`;
        world[key] = tile;

        const tileMaterial = new MeshLambertMaterial({
          map: texture,
          color: 0x00ff00,
          side: FrontSide,
          transparent: true,
          opacity: 1,
        });

        const mesh = new Mesh(tileGeometry, tileMaterial);
        mesh.position.x = tile.x * tileSize;
        mesh.position.y = tile.y * tileSize;
        mesh.position.z = 0;
        mesh.up.set(0, 0, 1);
        mesh.updateMatrixWorld();
        // mesh.receiveShadow = true;
        // store the mesh so the individual tiles can still be found
        // among the merged geometry.
        tile.mesh = mesh;
        mesh.name = key;
        world.tilesGroup.add(mesh);
        /**
         * The tile geometries are merged for performance reasons.
         * This is only temporary however, as doing this makes it into 'one' big plane
         * which will not be desirable later.
         */
        worldGeometry.merge(tileGeometry, mesh.matrix);
      });

    worldMesh = new Mesh(worldGeometry, worldMaterial);
    scene.add(worldMesh);
    // scene.add(world.tilesGroup)
    console.log(`took ${performance.now() - t1} ms to create the world`);

    const lastTile = newWorld.tiles[newWorld.tiles.length - 1];
    const { x: maxWidth, y: maxHeight } = lastTile;
    gridHelper = new GridHelper(maxWidth * tileSize, maxHeight);
    gridHelper.position.setZ(0.1);
    gridHelper.up.set(0, 0, 1);
    gridHelper.rotateX(Math.PI / 2);
    const box = new Box3().setFromObject(gridHelper);
    const width = box.max.x - box.min.x;
    const height = box.max.y - box.min.y;
    gridHelper.position.add(new Vector3(width / 2 - tileSize / 2, 0, 0));
    gridHelper.position.add(new Vector3(0, height / 2 - tileSize / 2, 0));
    scene.add(gridHelper);

    light.target.position.set((maxWidth * tileSize) / 2, (maxHeight * tileSize) / 2, 0);
    light.target.updateMatrixWorld();

    light.position.set((maxWidth * tileSize) / 2, (maxHeight * tileSize) / 2, 10);
    light.shadow.camera.left = (-maxWidth * tileSize) / 2;
    light.shadow.camera.right = (maxWidth * tileSize) / 2;
    light.shadow.camera.bottom = (-maxHeight * tileSize) / 2;
    light.shadow.camera.top = (maxHeight * tileSize) / 2;
    light.shadow.camera.near = 0;
    light.shadow.camera.far = 15;
  };

  spriteScene.add(world.sprites);

  engine.createSprite = (textureName, position = { x: 0, y: 0 }, group) => {
    const texture = textureLoader.load(`textures/${textureName}.png`);
    texture.wrapS = RepeatWrapping;
    texture.repeat.x = -1;

    const entityMaterial = new SpriteMaterial({
      map: texture,
      color: 0xffffff,
      rotation: Math.PI,
      opacity: 1,
      transparent: true,
    });
    const sprite = new Sprite(entityMaterial);
    sprite.position.x = position.x * tileSize;
    sprite.position.y = position.y * tileSize;
    group = group || world.sprites;
    group.add(sprite);
    const boxHelper = new BoxHelper(sprite);
    boxHelpers.add(boxHelper);

    sprite.addEventListener('removed', (event) => {
      boxHelpers.remove(boxHelper)
    });

    return sprite;
  };

  engine.stop = () => {
    console.log('Stopping engine');
    engine.running = false;
    window.removeEventListener('resize', resize);
  };

  engine.update = () => {};

  const animate = () => {
    if (!engine.running) {
      return;
    }
    requestAnimationFrame(animate);
    stats.begin();
    engine.update();

    if (gridHelper) {
      gridHelper.visible = gridHelperEnabled;
    }

    if (pressedKeys.has('a')) {
      camera.position.x -= 0.5;
    }
    if (pressedKeys.has('d')) {
      camera.position.x += 0.5;
    }
    if (pressedKeys.has('s')) {
      camera.position.y -= 0.5;
    }
    if (pressedKeys.has('w')) {
      camera.position.y += 0.5;
    }

    if (pressedKeys.has('q')) {
      camera.position.z += 0.5;
    }
    if (pressedKeys.has('e')) {
      camera.position.z -= 0.5;
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

    helper.update();
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

    boxHelpers.children.forEach(helper => {
      helper.visible = boxHelpersEnabled;
      helper.update();
    });

    renderer.autoClear = true;
    renderer.render(scene, camera);
    renderer.clearDepth();
    renderer.autoClear = false;
    renderer.render(spriteScene, camera);

    stats.end();
  };

  engine.getSpriteUnderMouse = () => {
    return intersectedSprite;
  };

  engine.getClickedSprite = () => {
    return clickedSprite;
  };

  engine.createGroup = (layer = 1) => {
    const newGroup = new Group();
    let sceneToUse = null;
    if (layer === 1) {
      sceneToUse = scene;
    }
    if (layer === 2) {
      sceneToUse = spriteScene;
    }
    if (sceneToUse === null) {
      sceneToUse = scene;
    }
    sceneToUse.add(newGroup);

    const removeGroup = () => {
      sceneToUse.remove(newGroup);
      newGroup.children.forEach(child => newGroup.remove(child));
    };

    return [newGroup, removeGroup];
  };

  return engine;
};
