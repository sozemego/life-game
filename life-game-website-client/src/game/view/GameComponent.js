import React, { useEffect } from 'react';
import * as gameActions from '../actions';
import { isGameStarted } from '../selectors';
import { connect } from 'react-redux';

export const GameComponent = ({startGame}) => {

  useEffect(() => {
    startGame();
  }, []);

  return (
    <div style={{width: "100%", height: "100%"}} id='game-container'/>
  );
};

const mapStateToProps = (state) => {
  return {
    gameStarted: isGameStarted(state),
  };
};

export default connect(mapStateToProps, gameActions)(GameComponent);