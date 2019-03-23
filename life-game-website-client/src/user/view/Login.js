import React, { useReducer, useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createHookReducer, makePayloadActionCreators } from '../../store/utils';
import { isLoggedIn } from '../selectors';
import * as userActions from '../actions';
import styles from './Login.module.css';
import { Button } from '../../components/button/Button';

const initialState = {
  username: 'asd',
  password: 'asd',
  message: '',
  error: false,
};

const [setUsername, setPassword, setMessage] = makePayloadActionCreators(
  'username',
  'password',
  'message',
);

const reducer = createHookReducer(initialState, {
  message: (state, action) => ({
    ...state,
    error: action.payload.error,
    message: action.payload.message,
  }),
});

const Login = ({ isLoggedIn, register, login }) => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const { username, password, message, error } = state;
  const [fetching, setFetching] = useState(false);

  const onRegisterClick = _ => {
    setFetching(true);
    return register(username, password)
      .then(() => dispatch(setMessage({ message: 'Registered!', error: false })))
      .catch(error => dispatch(setMessage({ message: error.message, error: true })))
      .then(() => setFetching(false));
  };

  const onLoginClick = _ => {
    setFetching(true);
    return login(username, password)
      .then(() => dispatch(setMessage({ message: 'Logged in!', error: false })))
      .catch(error => dispatch(setMessage({ message: error.message, error: true })))
      .then(() => setFetching(false));
  };

  useEffect(() => {
    onLoginClick();
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.form}>
        <div className={styles['input-container']}>
          <span>Username</span>
          <input onChange={event => dispatch(setUsername(event.target.value))} value={username} />
        </div>
        <div className={styles['input-container']}>
          <span>Password</span>
          <input onChange={event => dispatch(setPassword(event.target.value))} value={password} />
        </div>
        <div className={error ? styles['error-message'] : styles['success-message']}>{message}</div>
        <div className={styles['button-container']}>
          <Button
            disabled={!username || !password || isLoggedIn || fetching}
            onClick={onRegisterClick}
          >
            REGISTER
          </Button>
          <Button disabled={!username || !password || fetching} onClick={onLoginClick}>
            LOGIN
          </Button>
        </div>
      </div>
    </div>
  );
};

Login.propTypes = {
  loggedIn: PropTypes.bool,
};

const mapStateToProps = state => {
  return {
    isLoggedIn: isLoggedIn(state),
  };
};

export default connect(
  mapStateToProps,
  userActions,
)(Login);
