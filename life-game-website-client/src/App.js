import React from 'react';
import Header from './Header/Header';
import { Route } from 'react-router';
import Login from './user/view/Login';
import MainPage from './main/MainPage';

const App = (props) => {

  return (
    <div style={{ height: '100%' }}>
      <Header/>
      <Route path={'/'} component={MainPage}/>
      <Route path={'/login'} component={Login}/>
    </div>
  );
};

export default App;
