package net.mobProofCrystals;

import java.io.File;
import java.io.IOException;


import net.fabricmc.api.ModInitializer;
import net.mobProofCrystals.util.PropertiesCache;

import static net.mobProofCrystals.util.PropertiesCache.CONFIG_FILE;

public class mobProofCrystals implements ModInitializer {

  final public static String DEF_RAD = "32";
  final public static String DEF_LIM_DISTANCE = "1";
  final public static String DEF_CRYSTAL_NAME = "";
  final public static String DEPRECATED_CONFIG_FILE = "crystal.properties";
  @Override
  public void onInitialize() {
    try {
      File deprecatedConfigFile = new File(DEPRECATED_CONFIG_FILE);
      if (deprecatedConfigFile.isFile()) {
        deprecatedConfigFile.renameTo(new File(CONFIG_FILE));
      }
      File configFile = new File(CONFIG_FILE);
      // If config file does not exists creates a new one with the default values
      configFile.createNewFile();

      PropertiesCache cache = PropertiesCache.getInstance();
      if (cache.getProperty("radius") == null) {
        cache.setProperty("radius", DEF_RAD);
      }
      if (cache.getProperty("lower-limit-distance") == null){
        cache.setProperty("lower-limit-distance", DEF_LIM_DISTANCE);
      }
      if (cache.getProperty("crystal-name") == null){
        cache.setProperty("crystal-name", DEF_CRYSTAL_NAME);
      }
      
      //Write to the file
      PropertiesCache.getInstance().flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
