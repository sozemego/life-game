import React from 'react';
import Header from '../../header/Header';
import { Route } from 'react-router';
import Login from '../../user/view/Login';
import MainPage from '../../main/MainPage';
import GameComponent from '../../game/view/GameComponent';
import Statistics from '../../statistics/view/Statistics';

const App = props => {
  return (
    <div style={{ height: '100%' }}>
      <Header />
      <Route path={'/'} component={MainPage} exact />
      <Route path={'/login'} component={Login} exact />
      <Route path={'/game'} component={GameComponent} exact />
      <Route path={'/stats'} component={Statistics} exact />
    </div>
  );
};

export default App;
