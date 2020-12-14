package net.mobProofCrystals.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
 
public class PropertiesCache {
  private final Properties configProp = new Properties();
  final public static String CONFIG_FILE = "crystal.properties";
  
  private PropertiesCache() {
    try {
      InputStream in = new FileInputStream(CONFIG_FILE);
      configProp.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Bill Pugh Solution for singleton pattern
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
    try (final OutputStream outputstream = new FileOutputStream(CONFIG_FILE);) {
      configProp.store(outputstream, "File Updated");
      outputstream.close();
    }
  }
}