import React, { useEffect } from 'react';
import * as gameActions from '../actions';
import { getLoadGameMessage, isGameStarted } from '../selectors';
import { connect } from 'react-redux';

export const GameComponent = ({startGame, loadGameMessage}) => {

  useEffect(() => {
    startGame();
  }, []);

  return (
    <div>
      <div style={{width: "100%", textAlign: 'center'}}>{loadGameMessage}</div>
      <div style={{width: "100%", height: "100%"}} id='game-container'/>
    </div>
  );
};

const mapStateToProps = (state) => {
  return {
    gameStarted: isGameStarted(state),
    loadGameMessage: getLoadGameMessage(state),
  };
};

export default connect(mapStateToProps, gameActions)(GameComponent);