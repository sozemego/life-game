import React from 'react';
import Header from '../../Header/Header';
import { Route } from 'react-router';
import Login from '../../user/view/Login';
import MainPage from '../../main/MainPage';

const App = (props) => {

  return (
    <div style={{ height: '100%' }}>
      <Header/>
      <Route path={'/'} component={MainPage} exact/>
      <Route path={'/login'} component={Login} exact/>
    </div>
  );
};

export default App;
