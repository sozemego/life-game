import React from 'react';

export const Engine = ({ engine }) => {
  return (
    <div>
      Player [{engine.username}] started playing at [{new Date(engine.createdAt).toLocaleString()}].
      Currently running: {engine.running ? 'true' : 'false'}
    </div>
  );
};
