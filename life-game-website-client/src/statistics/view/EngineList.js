import React from 'react';
import PropTypes from 'prop-types';
import { Engine } from './Engine';

export const EngineList = props => {
  const { engines } = props;

  return (
    <div>
      {engines.map((engine, index) => {
        return <Engine engine={engine} key={engine.username} />;
      })}
    </div>
  );
};

EngineList.propTypes = {
  engines: PropTypes.array.isRequired,
};
