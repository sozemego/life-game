import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import styles from './Statistics.module.css';
import { WorldList } from './WorldList';
import { getWorlds } from '../selectors';


const Statistics = (props) => {

  useEffect(() => {

  }, []);

  const { worlds } = props;

  return (
    <div className={styles.container}>
      <div>Here are some statistics about current servers!</div>
      <div>Game worlds [{worlds.length}]:</div>
      <WorldList worlds={props.worlds}/>
    </div>
  );
};

Statistics.propTypes = {
  worlds: PropTypes.array.isRequired,
};

const mapStateToProps = (state) => {
  return {
    worlds: getWorlds(state),
  };
};

export default connect(mapStateToProps, null)(Statistics);