package com.soze.lifegame.api;

@FunctionalInterface
public interface NetworkRequest<T> {

  T get() throws Exception;

}
