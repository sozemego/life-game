import React from 'react';

export const World = ({ world }) => {
  return (
    <div>
      Player [{world.username}] started playing at [{new Date(world.createdAt).toLocaleString()}]
    </div>
  );
};
