import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import styles from './Statistics.module.css';
import { EngineList } from './EngineList';
import { getEngines, getMessage, getWorlds } from '../selectors';
import * as statActions from '../actions';

const Statistics = props => {
  useEffect(() => {
    props.init();
    return () => props.disconnect();
  }, []);

  const { engines, message } = props;

  return (
    <div className={styles.container}>
      <div>Here are some statistics about current players!</div>
      <div>{message}</div>
      <div>Game worlds [{engines.length}]:</div>
      <EngineList engines={engines} />
    </div>
  );
};

Statistics.propTypes = {
  engines: PropTypes.array.isRequired,
  message: PropTypes.string,
};

const mapStateToProps = state => {
  return {
    engines: getEngines(state),
    message: getMessage(state),
  };
};

export default connect(
  mapStateToProps,
  statActions,
)(Statistics);
