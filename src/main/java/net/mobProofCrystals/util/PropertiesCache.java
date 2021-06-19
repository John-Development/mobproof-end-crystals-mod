package net.mobProofCrystals.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Set;
 
public class PropertiesCache {
  private final Properties configProp = new Properties();
  
  final public static String DEF_RAD = "32";
  final public static String DEF_LIM_DISTANCE = "1";
  final public static String DEF_CRYSTAL_NAME = "";

  final public static String CONFIG_DIRECTORY = "config/crystal";
  final public static String CONFIG_FILE = "%s.properties";
  final public static String DEPRECATED_CONFIG_FILE = "crystal.properties";
  final public static String DEFAULT_CONFIG_FILE = "config/crystal.properties";
  private static final String defaultConfig = "radius specifies the radius of the cube\nlower-limit-distance specifies the distance between the crystal and the lower border of the area\nset crystal-name to a value to make only renamed end crystals (with a nametag) to be be spawn proofing";

  private static String levelName = "";
  private String worldFileRoute = DEFAULT_CONFIG_FILE;

  private PropertiesCache(String name) {
    worldFileRoute = CONFIG_DIRECTORY.concat("/").concat(String.format(CONFIG_FILE, name));
    try {
      File deprecatedConfigFile = new File(DEPRECATED_CONFIG_FILE);
      if (deprecatedConfigFile.isFile()) {
        renameDeprecatedConfig(deprecatedConfigFile);
      }

      File defaultConfigFile = new File(DEFAULT_CONFIG_FILE);
      File configFile = new File(worldFileRoute);
      File configDirectory = new File(CONFIG_DIRECTORY);

      if (defaultConfigFile.isFile() && !configFile.isFile()) {
        if (!configDirectory.isDirectory()) {
          configDirectory.mkdir();
        }
        Path copied = Paths.get(worldFileRoute);
        Path originalPath = defaultConfigFile.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
      } else {
        // If config file does not exists creates a new one with the default values
        configFile.createNewFile();
      }

      InputStream in = new FileInputStream(worldFileRoute);
      configProp.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private PropertiesCache() {
    worldFileRoute = CONFIG_DIRECTORY.concat("/").concat(String.format(CONFIG_FILE, levelName));
    try {
      File deprecatedConfigFile = new File(DEPRECATED_CONFIG_FILE);
      if (deprecatedConfigFile.isFile()) {
        renameDeprecatedConfig(deprecatedConfigFile);
      }
      
      File configDirectory = new File(CONFIG_DIRECTORY);
      if (!configDirectory.isDirectory()) {
        configDirectory.mkdir();
      }

      File defaultConfigFile = new File(DEFAULT_CONFIG_FILE);
      File configFile = new File(worldFileRoute);

      if (defaultConfigFile.isFile() && !configFile.isFile()) {
        Path copied = Paths.get(worldFileRoute);
        Path originalPath = defaultConfigFile.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
      } else {
        // If config file does not exists creates a new one with the default values
        configFile.createNewFile();
      }
      
      InputStream in = new FileInputStream(worldFileRoute);
      configProp.load(in);
      
      initMissing();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void renameDeprecatedConfig(File deprecatedFile) {
    deprecatedFile.renameTo(new File(DEFAULT_CONFIG_FILE));
  }

  private void initMissing() throws FileNotFoundException, IOException {
    boolean hasChanged = false;

    if (this.getProperty("radius") == null) {
      this.setProperty("radius", DEF_RAD);
      hasChanged = true;
    }
    if (this.getProperty("lower-limit-distance") == null){
      this.setProperty("lower-limit-distance", DEF_LIM_DISTANCE);
      hasChanged = true;
    }
    if (this.getProperty("crystal-name") == null){
      this.setProperty("crystal-name", DEF_CRYSTAL_NAME);
      hasChanged = true;
    }
    
    if (hasChanged) {
      //Write to the file
      this.flush();
    }
  }

  public static void setLevelName(String name) {
    levelName = name;
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
  
  public Set<String> getAllPropertyNames() {
    return configProp.stringPropertyNames();
  }
  
  public boolean containsKey(String key) {
    return configProp.containsKey(key);
  }

  public void setProperty(String key, String i) {
    configProp.setProperty(key, i);
  }
   
  public void flush() throws FileNotFoundException, IOException {
    try (final OutputStream outputstream = new FileOutputStream(worldFileRoute);) {
      configProp.store(outputstream, defaultConfig);
      outputstream.close();
    }
  }
}