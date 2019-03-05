// import React from 'react';
// import { act, cleanup, fireEvent, render, wait } from 'react-testing-library';
//
// import 'jest-dom/extend-expect';
// import Login from './Login';
// import { Provider } from 'react-redux';
// import realStore from '../../store/store';
// import { createStore } from 'redux';
// import { isLoggedIn } from '../selectors';
// import axiosMock from 'axios';
//
// jest.useFakeTimers();
//
// afterEach(cleanup);
//
// // this is a handy function that I normally make available for all my tests
// // that deal with connected components.
// // you can provide initialState or the entire store that the ui is rendered with
// function renderWithRedux(
//   ui,
//   { initialState, store = createStore((s, a) => s, initialState) } = {},
// ) {
//   return {
//     ...render(<Provider store={store}>{ui}</Provider>),
//     // adding `store` to the returned utilities to allow us
//     // to reference it in our tests (just try to avoid using
//     // this to test implementation details).
//     store,
//   };
// }
//
// test('can render with redux with defaults', async () => {
//   const { getByTestId, getByText, getByLabelText, store } = renderWithRedux(<Login/>, { store: realStore });
//   axiosMock.post.mockResolvedValueOnce({data: {greeting: 'hello there'}})
//   act(() => {
//     const usernameInput = getByLabelText('username');
//     fireEvent.change(usernameInput, { target: { value: 'asd' } });
//     const passwordInput = getByLabelText('password');
//     fireEvent.change(passwordInput, { target: { value: 'asd' } });
//   })
//   act(() => {
//     fireEvent.click(getByText('LOGIN'));
//     // store.dispatch({type: 'SET_USER_NAME', payload: 'lol'});
//     jest.runAllTimers();
//   });
//   expect(axiosMock.post).toHaveBeenCalledTimes(1);
//   console.log(store.getState())
//   await wait(() => {
//     return false
//   });
//   console.log(store.getState())
//
// });