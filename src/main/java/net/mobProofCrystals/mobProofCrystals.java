package net.mobProofCrystals;

import java.io.File;
import java.io.IOException;


import net.fabricmc.api.ModInitializer;
import net.mobProofCrystals.util.PropertiesCache;

public class mobProofCrystals implements ModInitializer {

  final public static String DEF_RAD = "32";
  final public static String DEF_LIM_DISTANCE = "1";
  final public static String CONFIG_FILE = "crystal.properties";
  @Override
  public void onInitialize() {
    try {
      File configFile = new File(CONFIG_FILE);

      // If config file does not exists creates a new one with the default values
      if (configFile.createNewFile()) {
        PropertiesCache cache = PropertiesCache.getInstance();
        cache.setProperty("radius", DEF_RAD);
        cache.setProperty("lower-limit-distance", DEF_LIM_DISTANCE);
        
        //Write to the file
        PropertiesCache.getInstance().flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
