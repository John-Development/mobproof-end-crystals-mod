package net.mobProofCrystals.util;

import java.io.*;
import java.util.Properties;
 
public class PropertiesCache {
  private final Properties configProp = new Properties();
  final public static String CONFIG_FILE = "config/crystal.properties";
  final public static String DEPRECATED_CONFIG_FILE = "crystal.properties";
  private final String defaultConfig = "radius specifies the radius of the cube\nlower-limit-distance specifies the distance between the crystal and the lower border of the area\nset crystal-name to a value to make only renamed end crystals (with a nametag) to be be spawn proofing";

  private PropertiesCache() {
    try {
      File deprecatedConfigFile = new File(DEPRECATED_CONFIG_FILE);
      if (deprecatedConfigFile.isFile()) {
        deprecatedConfigFile.renameTo(new File(CONFIG_FILE));
      }
      File configFile = new File(CONFIG_FILE);
      // If config file does not exists creates a new one with the default values
      configFile.createNewFile();

      InputStream in = new FileInputStream(CONFIG_FILE);
      configProp.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class LazyHolder {
    private static final PropertiesCache INSTANCE = new PropertiesCache();
  }

  public static PropertiesCache getInstance() {
    return LazyHolder.INSTANCE;
  }
  
  public String getProperty(String key) {
    return configProp.getProperty(key);
  }

  public int getIntProperty(String key) {
    return Integer.parseInt(configProp.getProperty(key));
  }

  public void setProperty(String key, String i) {
    configProp.setProperty(key, i);
  }
   
  public void flush() throws IOException {
    try (final OutputStream outputstream = new FileOutputStream(CONFIG_FILE)) {
      configProp.store(outputstream, defaultConfig);
    }
  }
}