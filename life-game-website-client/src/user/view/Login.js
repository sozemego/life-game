import React, { useReducer } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createReducer, makePayloadActionCreators } from '../../store/utils';
import { isLoggedIn } from '../selectors';
import * as userActions from '../actions';
import styles from './Login.module.css';


const initialState = {
  username: '',
  password: '',
  message: '',
  error: '',
};

const [
  setUsername, setPassword, setMessage,
] = makePayloadActionCreators('username', 'password', 'message');

const reducer = createReducer(initialState, {
  message: (state, action) => ({ ...state, error: action.payload.error, message: action.payload.message }),
});

const Login = ({ isLoggedIn, register, login }) => {

  const [ state, dispatch ] = useReducer(reducer, initialState);
  const { username, password, message, error } = state;

  const onRegisterClick = (_) => {
    register(username, password)
      .then(() => dispatch(setMessage({ message: 'Registered!', error: false })))
      .catch(error => dispatch(setMessage({ message: error.message, error: true })));
  };

  const onLoginClick = (_) => {
    login(username, password)
      .then(() => dispatch(setMessage({ message: 'Logged in!', error: false })))
      .catch(error => dispatch(setMessage({ message: error.message, error: true })));
  };

  return (
    <div className={styles.container}>
      <div className={styles.form}>
        <div className={styles['input-container']}>
          <span>Username</span>
          <input onChange={event => dispatch(setUsername(event.target.value))} value={username}/>
        </div>
        <div className={styles['input-container']}>
          <span>Password</span>
          <input onChange={event => dispatch(setPassword(event.target.value))} value={password}/>
        </div>
        <div className={error ? styles['error-message'] : styles['success-message']}>
          {message}
        </div>
        <div className={styles['button-container']}>
          <button disabled={!username || !password || isLoggedIn} onClick={onRegisterClick}>
            REGISTER
          </button>
          <button disabled={!username || !password} onClick={onLoginClick}>
            LOGIN
          </button>
        </div>
      </div>
    </div>
  );
};

Login.propTypes = {
  loggedIn: PropTypes.bool,
};

const mapStateToProps = (state) => {
  return {
    isLoggedIn: isLoggedIn(state),
  };
};

export default connect(mapStateToProps, userActions)(Login);