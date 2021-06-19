package net.mobProofCrystals;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.mobProofCrystals.commands.GameRuleCustomCommand;

@Environment(EnvType.CLIENT)
public class mobProofCrystals implements ModInitializer {
  @Override
  public void onInitialize() {
    // Init commands
    GameRuleCustomCommand.getInstance().init();
  }
}
