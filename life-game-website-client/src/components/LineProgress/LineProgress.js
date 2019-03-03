import React from 'react';
import { connect } from 'react-redux';

import styles from './LineProgress.module.css';

export const LineProgress = (props) => {

  const { isFetching } = props;

  const classNames = [
    styles.body, isFetching ? styles.fetching : null
  ].filter(Boolean).join(' ');

  return (
    <div className={styles.container}>
      <div className={classNames}>

      </div>
    </div>
  );
};

const mapStateToProps = (state) => {
  return {
    isFetching: true,
  };
};

export default connect(mapStateToProps, null)(LineProgress);