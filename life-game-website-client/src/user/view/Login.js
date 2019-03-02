import React, { useReducer } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createReducer, makePayloadActionCreators } from '../../store/utils';
import { isLoggedIn } from '../selectors';
import * as userActions from '../actions';

const containerStyle = {
  display: 'flex',
  width: '100%',
  height: '100%',
  justifyContent: 'center',
  alignItems: 'center',
};
const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  border: '1px solid gray',
  borderRadius: '4px',
  padding: '8px',
  boxShadow: '0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)',
  minWidth: '250px',
};

const inputContainerStyle = {
  display: 'flex',
  justifyContent: 'space-between',
};

const buttonContainer = {
  display: 'flex',
  justifyContent: 'center',
};

const successMessageStyle = {
  color: 'green',
};

const errorMessageStyle = {
  color: 'red',
};

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

  const [state, dispatch] = useReducer(reducer, initialState);
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
    <div style={containerStyle}>
      <div style={formStyle}>
        <div style={inputContainerStyle}>
          <span>Username</span>
          <input onChange={event => dispatch(setUsername(event.target.value))} value={username}/>
        </div>
        <div style={inputContainerStyle}>
          <span>Password</span>
          <input onChange={event => dispatch(setPassword(event.target.value))} value={password}/>
        </div>
        <div style={error ? errorMessageStyle : successMessageStyle}>
          {message}
        </div>
        <div style={buttonContainer}>
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