package net.mobProofCrystals;

import me.x150.renderer.event.Events;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.mobProofCrystals.commands.GameRuleCustomCommand;
import net.mobProofCrystals.util.PropertiesCache;
import net.mobProofCrystals.renderer.Listener;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class mobProofCrystals implements ModInitializer {
  @Override
  public void onInitialize() {
    // Init commands
    GameRuleCustomCommand.getInstance().init();
    PropertiesCache.getDefaultInstance().init();
    Events.manager.registerSubscribers(new Listener());


    KeyBinding renderKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
      "key.fabric-key-binding-api-v1-testmod.test_keybinding_1",
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_F6,
      "key.category.first.test"
    ));

    ClientTickEvents.END_CLIENT_TICK.register(client -> {
      while (renderKey.wasPressed()) {
        // Toggle render
        PropertiesCache cache = PropertiesCache.getInstance();
        cache.setProperty("render", String.valueOf(!cache.getBoolProperty("render")));

        try {
          cache.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
