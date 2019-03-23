export const createLogger = name => {
  return (...args) => {
    console.log(name, ...args);
  };
};
