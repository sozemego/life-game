import { BoxGeometry, Mesh, MeshBasicMaterial, PerspectiveCamera, Scene, WebGLRenderer } from 'three';

export const createGame = () => {

  const scene = new Scene();
  const camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 2000);

  const renderer = new WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);
  const container = document.getElementById('game-container');
  container.append(renderer.domElement);

  const box = new BoxGeometry(1, 1, 1);
  const material = new MeshBasicMaterial({color: 0x00ff00});
  const cube = new Mesh(box, material);

  scene.add(cube)

  camera.position.z = 5;

  const animate = () => {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);

    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;
    cube.rotation.z += 0.01;
  };

  const game = {

  };

  game.start = () => {
    console.log('Starting game loop!');
    animate();
  };

  return game;
};