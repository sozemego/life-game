import {
  AdditiveBlending,
  AmbientLight,
  AxesHelper, Blending,
  Box3, BoxGeometry,
  BoxHelper,
  CameraHelper,
  DirectionalLight,
  DirectionalLightHelper, DoubleSide,
  Font,
  FrontSide,
  Geometry,
  GridHelper,
  Group,
  Mesh,
  MeshBasicMaterial,
  MeshLambertMaterial,
  Object3D,
  PCFShadowMap,
  PerspectiveCamera,
  Plane,
  PlaneGeometry,
  Raycaster,
  RepeatWrapping,
  Scene,
  Sprite,
  SpriteMaterial,
  TextGeometry,
  TextureLoader,
  Vector3,
  WebGLRenderer
} from "three/src/Three";
import Stats from 'stats-js';
import { InputHandler, Mouse } from '../InputHandler';
import { World } from '../dto';
import { Cursor, defaultCursor, selectCursor, targetCursor } from './Cursor';

export const createGfxEngine = (inputHandler: InputHandler, tileSize: number): GfxEngine => {
  const scene = new Scene();
  const spriteScene = new Scene();
  const width = window.innerWidth - 17;
  const height = window.innerHeight - 36 - 25;
  const aspect = width / height;
  const camera = new PerspectiveCamera(70, aspect, 0.001, 1000);
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

  const cameraHelper = new CameraHelper(camera);
  scene.add(cameraHelper);

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
  if (container) {
    container.append(renderer.domElement);
    container.appendChild(stats.dom);
  } else {
    throw new Error('Game container does not exist');
  }

  camera.updateProjectionMatrix();

  const boxHelpers = new Group();
  spriteScene.add(boxHelpers);

  const pressedKeys = new Set();

  let mouse: Mouse = { x: 0, y: 0, rawX: 0, rawY: 0, button: 0 };

  let boxHelpersEnabled = true;
  let gridHelperEnabled = true;
  let gridHelper: GridHelper | null = null;

  inputHandler.onKeyUp(key => {
    console.log(`Key up      ${key}`);
    if (key === '0') {
      boxHelpersEnabled = !boxHelpersEnabled;
    }
    if (key === '9') {
      gridHelperEnabled = !gridHelperEnabled;
    }
    pressedKeys.delete(key);
    return false;
  });

  inputHandler.onKeyDown(key => {
    console.log(`Key down    ${key}`);
    pressedKeys.add(key);
    return false;
  });

  inputHandler.onMouseWheel(delta => {
    if (delta > 0) {
      camera.position.z += 1;
    } else {
      camera.position.z -= 1;
    }
    return false;
  });

  let clickedSprite: Sprite | null = null;

  inputHandler.onMouseUp(mouse => {
    if (mouse.button === 0) {
      if (intersectedSprite) {
        clickedSprite = intersectedSprite;
      } else {
        clickedSprite = null;
      }
    }
    if (mouse.button === 2) {
      clickedSprite = null;
    }
    return false;
  });

  let intersectedTile: Mesh | null = null;
  let intersectedSprite: Sprite | null = null;

  inputHandler.onMouseMove(nextMouse => {
    mouse = nextMouse;
    const rayCaster = new Raycaster();
    camera.updateMatrixWorld(true);
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
        intersectedTile = tileIntersection.object.clone() as Mesh;
        intersectedTile.material = tileMaterial;
        intersectedTile.material.transparent = true;
        intersectedTile.material.opacity = 0.75;
        intersectedTile.position.z = 0.05;
        intersectedTile.userData['highlightPhase'] = 'DOWN';
        scene.add(intersectedTile);
      }
    } else {
      if (intersectedTile) {
        scene.remove(intersectedTile);
      }
      intersectedTile = null;
    }

    if (spriteIntersection) {
      const name = (intersectedSprite && intersectedSprite.name) || null;
      if (name !== spriteIntersection.object.name) {
        intersectedSprite = spriteIntersection.object as Sprite;
        intersectedSprite['userData']['selected'] = true;
      }
    } else {
      if (intersectedSprite) {
        intersectedSprite['userData']['selected'] = false;
      }
      intersectedSprite = null;
    }
    return false;
  });

  let running = false;

  const start = () => {
    console.log('Starting game loop!');
    running = true;
    animate();
  };

  const world = {
    tiles: {},
    meshes: new Group(),
    tilesGroup: new Group(),
    sprites: new Group(),
    groups: [],
  };

  const worldGeometry = new Geometry();
  let worldMesh = null;

  const setWorld = (newWorld: World) => {
    const t1 = performance.now();
    worldGeometry.dispose();
    console.log(newWorld);
    const texture = textureLoader.load('textures/medievalTile_57.png');
    const tileGeometry = new PlaneGeometry(tileSize, tileSize, 1, 1);
    const worldMaterial = new MeshLambertMaterial({
      map: texture,
      color: 0x00ff00,
      side: FrontSide,
    });

    newWorld.tiles
      .sort((a, b) => a.x - b.x)
      .sort((a, b) => a.y - b.y)
      .forEach((tile, index) => {
        const key = `${tile.x}:${tile.y}`;
        // @ts-ignore
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
        mesh.matrixAutoUpdate = false;
        mesh.updateMatrix();
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
  scene.add(world.meshes);

  const createSpriteOptions: CreateObjectOptions = {
    x: 0,
    y: 0,
    width: 0,
    height: 0,
  };

  const createSprite = (
    textureName: string,
    options: CreateObjectOptions = createSpriteOptions,
    group: Group | null,
  ): Sprite => {
    options.depthTest = options.depthTest || true;
    options.renderOrder = options.renderOrder || 0;
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
    sprite.position.x = options.x * tileSize;
    sprite.position.y = options.y * tileSize;
    sprite.renderOrder = options.renderOrder;
    sprite.material.depthTest = options.depthTest;
    group = group || world.sprites;
    group.add(sprite);
    const boxHelper = new BoxHelper(sprite);
    boxHelpers.add(boxHelper);

    sprite.addEventListener('removed', event => {
      boxHelpers.remove(boxHelper);
    });

    return sprite;
  };

  const createMesh = (name: string, options: CreateObjectOptions, group: Group | null) => {
    group = group || world.meshes;
    const texture = textureLoader.load(`textures/${name}.png`);
    const material = new MeshBasicMaterial({
      map: texture,
      color: 0xffffff,
      side: FrontSide,
      transparent: true,
      opacity: 1,
      // alphaTest: 0.5
    });
    const { x = 0, y = 0, width = 1, height = 1 } = options;
    const geometry = new PlaneGeometry(width, height, 1, 1);
    const mesh = new Mesh(geometry, material);
    mesh.position.set(x, y, 0.25);
    mesh.rotation.z = Math.PI;
    group.add(mesh);
    return mesh;
  };

  const stop = () => {
    console.log('Stopping engine');
    running = false;
    window.removeEventListener('resize', resize);
  };

  let update = (delta: number) => {};

  const animate = () => {
    requestAnimationFrame(animate);
    if (!running) {
      return;
    }
    stats.begin();
    update(1 / 60);

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
      // @ts-ignore
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
      // @ts-ignore
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
      (helper as BoxHelper).update();
    });

    renderer.autoClear = true;
    renderer.render(scene, camera);
    renderer.clearDepth();
    renderer.autoClear = false;
    renderer.render(spriteScene, camera);

    stats.end();
  };

  const getSpriteUnderMouse = () => {
    return intersectedSprite;
  };

  const getClickedSprite = () => {
    return clickedSprite;
  };

  const createGroup = (layer: number | null) => {
    const newGroup = new Group();
    let sceneToUse: Scene | null = null;
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
      if (sceneToUse) {
        sceneToUse.remove(newGroup);
      }
      newGroup.children.forEach(child => newGroup.remove(child));
    };

    return [newGroup, removeGroup] as [Group, Function];
  };

  const setUpdate = (updateFn: (delta: number) => void) => {
    update = updateFn;
  };

  const cursors = {
    [Cursor.DEFAULT]: defaultCursor,
    [Cursor.SELECT]: selectCursor,
    [Cursor.TARGET]: targetCursor,
  };

  const setCursor = (type: Cursor) => {
    const cursor = cursors[type];
    container.style.cursor = `url(${cursor}), auto`;
    container.getBoundingClientRect();
  };

  const getMouse = () => mouse;

  return {
    start,
    setWorld,
    createSprite,
    createMesh,
    getSpriteUnderMouse,
    getClickedSprite,
    createGroup,
    setUpdate,
    setCursor,
    getMouse,
  };
};

export interface GfxEngine {
  start: Function;
  setWorld: (world: World) => void;
  createSprite: (name: string, options: CreateObjectOptions, group: Group | null) => Sprite;
  createMesh: (name: string, options: CreateObjectOptions, group: Group | null) => Mesh;
  getSpriteUnderMouse: () => Sprite | null;
  getClickedSprite: () => Sprite | null;
  createGroup: (layer: number | null) => [Group, Function];
  setUpdate: (updateFn: (delta: number) => void) => void;
  setCursor: (type: Cursor) => void;
  getMouse: () => Mouse;
}

export interface CreateObjectOptions {
  x: number;
  y: number;
  width?: number;
  height?: number;
  renderOrder?: number;
  depthTest?: boolean;
}
