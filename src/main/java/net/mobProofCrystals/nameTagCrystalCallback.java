package net.mobProofCrystals;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface nameTagCrystalCallback {
  Event<nameTagCrystalCallback> EVENT = EventFactory.createArrayBacked(nameTagCrystalCallback.class,
      (listeners) -> (player, crystal) -> {
          for (nameTagCrystalCallback listener : listeners) {
              ActionResult result = listener.interact(player, crystal);

              if(result != ActionResult.PASS) {
                  return result;
              }
          }

      return ActionResult.PASS;
  });

  ActionResult interact(PlayerEntity player, Entity crystal);
}