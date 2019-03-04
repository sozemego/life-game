import React, { useEffect } from 'react';
import * as gameActions from './actions';
import { isGameStarted } from './selectors';
import { connect } from 'react-redux';

export const Game = ({startGame}) => {

  useEffect(() => {
    startGame();
  }, []);

  return (
    <canvas id='game'/>
  );
};

const mapStateToProps = (state) => {
  return {
    gameStarted: isGameStarted(state),
  };
};

export default connect(mapStateToProps, gameActions)(Game);