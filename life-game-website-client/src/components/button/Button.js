import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { LineProgress } from '../line-progress/LineProgress';

export const Button = ({ children, onClick, disabled = false, ...props }) => {
  const [fetching, setFetching] = useState(false);

  const onClickDelegate = () => {
    const result = onClick();
    if (result instanceof Promise) {
      setFetching(true);
      result.then(() => setFetching(false));
    }
  };

  disabled = disabled || fetching;

  return (
    <button {...props} onClick={onClickDelegate} disabled={disabled}>
      {children}
      <LineProgress isFetching={fetching} />
    </button>
  );
};

Button.propTypes = {
  children: PropTypes.any,
  onClick: PropTypes.func,
};

Button.defaultProps = {
  content: null,
  onClick: () => {},
};
