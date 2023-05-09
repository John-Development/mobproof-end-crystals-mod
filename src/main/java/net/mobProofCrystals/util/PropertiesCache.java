package net.mobProofCrystals.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesCache {
  private final Properties configProp = new Properties();

  private static String levelName = "";

  private final String configRoute;

  Logger logger = Logger.getLogger(PropertiesCache.class.getName());

  /**
   * Default config initialiser
   */
  private PropertiesCache() {
    configRoute = Constants.DEFAULT_CONFIG_FILE;

    try {
      File deprecatedConfigFile = new File(Constants.DEPRECATED_CONFIG_FILE);
      File defaultConfigFile = new File(configRoute);

      if (deprecatedConfigFile.isFile()) {
        if (!deprecatedConfigFile.renameTo(defaultConfigFile)) {
          logger.log(Level.WARNING, "File rename failed.");
        }
      } else if (!defaultConfigFile.createNewFile()) {
        // If config file does not exists creates a new one with the default values
        logger.log(Level.INFO, "File creation failed.");
      }

      InputStream in = new FileInputStream(configRoute);
      configProp.load(in);

      initMissing();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private PropertiesCache(String name) {
    levelName = name;
    configRoute = Constants.CONFIG_DIRECTORY.concat("/").concat(String.format(Constants.CONFIG_FILE, name));

    try {
      File configDirectory = new File(Constants.CONFIG_DIRECTORY);
      if (!configDirectory.isDirectory() && !configDirectory.mkdir()) {
        logger.log(Level.INFO, "Directory creation failed.");
      }

      File defaultConfigFile = new File(Constants.DEFAULT_CONFIG_FILE);
      File configFile = new File(configRoute);

      if (defaultConfigFile.isFile() && !configFile.isFile()) {
        // Creates specific world config file from the old default one
        Path copied = Paths.get(configRoute);
        Path originalPath = defaultConfigFile.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
      }

      InputStream in = new FileInputStream(configRoute);
      configProp.load(in);

      initMissing();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void init() {

  }

  private void initMissing() throws IOException {
    boolean hasChanged = false;

    if (this.getProperty("radius") == null) {
      this.setProperty("radius", Constants.DEF_RAD);
      hasChanged = true;
    }
    if (this.getProperty("lower-limit-distance") == null){
      this.setProperty("lower-limit-distance", Constants.DEF_LIM_DISTANCE);
      hasChanged = true;
    }
    if (this.getProperty("crystal-name") == null){
      this.setProperty("crystal-name", Constants.DEF_CRYSTAL_NAME);
      hasChanged = true;
    }
    if (this.getProperty("render") == null){
      this.setProperty("render", "false");
      hasChanged = true;
    }

    if (hasChanged) {
      //Write to the file
      this.flush();
    }
  }

  public static void setLevelName(String name) {
    if (!Objects.equals(name, levelName)) {
      LazyHolder.resetInstance(name);
    }
  }

  private static class LazyHolder {
    private static final PropertiesCache DEFAULT_INSTANCE = new PropertiesCache();
    private static PropertiesCache INSTANCE;

    protected static void resetInstance(String name) {
      INSTANCE = new PropertiesCache(name);
    }
  }

  public static PropertiesCache getInstance() {
    return LazyHolder.INSTANCE;
  }

  public static PropertiesCache getDefaultInstance() {
    return LazyHolder.DEFAULT_INSTANCE;
  }

  public String getProperty(String key) {
    return configProp.getProperty(key);
  }

  public boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(configProp.getProperty(key));
  }

  public int getIntProperty(String key) {
    return Integer.parseInt(configProp.getProperty(key));
  }

  public void setProperty(String key, String i) {
    configProp.setProperty(key, i);
  }

  public void flush() throws IOException {
    try (final OutputStream outputstream = new FileOutputStream(configRoute)) {
      configProp.store(outputstream, Constants.DEFAULT_CONFIG);
    }
  }
}