import React from 'react';
import { World } from './World';
import PropTypes from 'prop-types';

export const WorldList = props => {
  const { worlds } = props;

  return (
    <div>
      {worlds.map((world, index) => {
        return <World world={world} key={world.username} />;
      })}
    </div>
  );
};

WorldList.propTypes = {
  worlds: PropTypes.array.isRequired,
};
