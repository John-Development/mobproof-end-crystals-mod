package net.mobProofCrystals;

import net.fabricmc.api.ModInitializer;
import net.mobProofCrystals.commands.GameRuleCustomCommand;
import net.mobProofCrystals.util.PropertiesCache;

public class mobProofCrystals implements ModInitializer {
  @Override
  public void onInitialize() {
    // Init commands
    GameRuleCustomCommand.getInstance().init();
    PropertiesCache.getDefaultInstance().init();
  }
}
