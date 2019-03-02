import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import Divider from './components/Divider';
import { isLoggedIn } from './user/selectors';
import { connect } from 'react-redux';
import * as userActions from './user/actions';

const containerStyle = { display: 'flex', justifyContent: 'center', flexDirection: 'column' };
const headerContent = { display: 'flex', justifyContent: 'space-between', flexDirection: 'row' };
const gameNameStyle = {
  display: 'flex',
  justifySelf: 'center',
  height: '2rem',
  fontSize: 24,
  flex: 1,
  justifyContent: 'center',
};
const loginContentStyle = { display: 'flex', justifyContent: 'center', flex: 1, alignSelf: 'center' };
const logoutStyle = { cursor: 'pointer' };

const Header = ({ isLoggedIn, logout }) => {

  return (
    <div style={containerStyle}>
      <div style={headerContent}>
        <div style={{ flex: 1, visibility: 'hidden' }}>
          what
        </div>
        <div style={gameNameStyle}>
          <Link to={'/'}>
            LIFE GAME
          </Link>
        </div>
        <div style={loginContentStyle}>
          {!isLoggedIn &&
          <Link to={'/login'}>
            LOGIN
          </Link>}
          {isLoggedIn &&
          <span onClick={logout} style={logoutStyle}>
            LOGOUT
          </span>
          }
        </div>
      </div>
      <Divider/>
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