import React from 'react';
import styles from './MainPage.module.css';
import { isLoggedIn } from '../user/selectors';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';

const MainPage = ({ isLoggedIn }) => {
  return (
    <div className={styles.container}>
      HELLO TO THE LIFE GAME
      {isLoggedIn && (
        <Link to={'/game'}>
          <button>PLAY</button>
        </Link>
      )}
      <Link to={'/stats'}>
        <button>Game statistics</button>
      </Link>
    </div>
  );
};

const mapStateToProps = state => {
  return {
    isLoggedIn: isLoggedIn(state),
  };
};

export default connect(
  mapStateToProps,
  null,
)(MainPage);
