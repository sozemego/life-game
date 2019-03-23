import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import styles from './Statistics.module.css';
import { WorldList } from './WorldList';
import { getMessage, getWorlds } from '../selectors';
import * as statActions from '../actions';

const Statistics = props => {
  useEffect(() => {
    props.init();
    return () => props.disconnect();
  }, []);

  const { worlds, message } = props;

  return (
    <div className={styles.container}>
      <div>Here are some statistics about current servers!</div>
      <div>{message}</div>
      <div>Game worlds [{worlds.length}]:</div>
      <WorldList worlds={props.worlds} />
    </div>
  );
};

Statistics.propTypes = {
  worlds: PropTypes.array.isRequired,
  message: PropTypes.string
};

const mapStateToProps = state => {
  return {
    worlds: getWorlds(state),
    message: getMessage(state)
  };
};

export default connect(
  mapStateToProps,
  statActions
)(Statistics);
