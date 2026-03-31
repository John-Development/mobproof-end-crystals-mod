package net.mobProofCrystals.mixin;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.mobProofCrystals.util.PropertiesCache;

@Mixin(Player.class)
public abstract class NameTagMixin {
  @Inject(
          method = "interactOn",
          at = @At("RETURN"),
          cancellable = true
  )
  public void setCustomNameTag(
          Entity entity,
          InteractionHand hand,
          Vec3 location,
          CallbackInfoReturnable<InteractionResult> cir
  ) {
    ItemStack itemStack = ((Player) (Object) this).getItemInHand(hand);
    PropertiesCache cache = PropertiesCache.getInstance();

    if (itemStack.getItem().getClass().equals(NameTagItem.class)
            && itemStack.get(DataComponents.CUSTOM_NAME) != null
            && entity instanceof EndCrystal
            && !((EndCrystal) entity).showsBottom()
            && cache.getProperty("crystal-name").equals(itemStack.getHoverName().getString())) {
      entity.setCustomName(itemStack.getHoverName());

      itemStack.shrink(1);
      entity.setCustomNameVisible(true);

      cir.setReturnValue(((Player) (Object) this).level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME);
    }
  }
}