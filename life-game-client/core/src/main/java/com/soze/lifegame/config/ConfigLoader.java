package com.soze.lifegame.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);

  private final Map<Config, Properties> configs = new HashMap<>();

  public ConfigLoader() {
    loadConfigs();
  }

  private void loadConfigs() {
    LOG.info("Loading configs");
    String base = "data";

    for (Config value : Config.values()) {
      Properties properties = new Properties();
      try {
        String fileName = String.format("%s/%s.properties", base, value.name().toLowerCase());
        LOG.info("Loading config from {}", fileName);
        InputStream inputStream = new FileInputStream(fileName);
        properties.load(inputStream);
        configs.put(value, properties);
      } catch (FileNotFoundException e) {
        LOG.warn("Configuration not found!", e);
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public Properties getConfig(Config config) {
    if(!configs.containsKey(config)) {
      return new Properties();
    }
    return configs.get(config);
  }

  public enum Config {
    PLAYER_API
  }

}
