import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import styles from './LineProgress.module.css';
import { isFetching } from '../../app/selectors';

export const LineProgress = props => {
  const { isFetching } = props;

  const classNames = [styles.body, isFetching ? styles.fetching : null].filter(Boolean).join(' ');

  return (
    <div className={styles.container}>
      <div className={classNames} />
    </div>
  );
};

LineProgress.props = {
  isFetching: PropTypes.bool,
};

LineProgress.defaultProps = {
  isFetching: false,
};

const mapStateToProps = state => {
  return {
    isFetching: isFetching(state),
  };
};

export default connect(
  mapStateToProps,
  null,
)(LineProgress);
