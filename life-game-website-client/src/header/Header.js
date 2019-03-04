import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { isLoggedIn } from '../user/selectors';
import { connect } from 'react-redux';
import * as userActions from '../user/actions';
import styles from './Header.module.css';
import LineProgress from '../components/line-progress/LineProgress';
import Divider from '../components/divider/Divider';

const Header = ({ isLoggedIn, logout }) => {

  return (
    <div className={styles.container}>
      <div className={styles['header-content']}>
        <div style={{ flex: 1, visibility: 'hidden' }}>
          what
        </div>
        <div className={styles['game-name']}>
          <Link to={'/'}>
            LIFE GAME
          </Link>
        </div>
        <div className={styles['login-content']}>
          {!isLoggedIn &&
          <Link to={'/login'}>
            LOGIN
          </Link>}
          {isLoggedIn &&
          <span onClick={logout} className={styles.logout}>
            LOGOUT
          </span>
          }
        </div>
      </div>
      <Divider/>
      <LineProgress />
    </div>
  );
};

Header.propTypes = {
  isLoggedIn: PropTypes.bool,
};

const mapStateToProps = (state) => {
  return {
    isLoggedIn: isLoggedIn(state),
  };
};


export default connect(mapStateToProps, userActions)(Header);