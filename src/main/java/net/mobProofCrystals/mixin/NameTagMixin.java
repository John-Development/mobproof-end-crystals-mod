package net.mobProofCrystals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.mobProofCrystals.util.PropertiesCache;

@Mixin(PlayerEntity.class)
public abstract class NameTagMixin {

  @Inject(
    method = "interact",
    at = @At("RETURN"),
    cancellable = true
  )
  public void setCustomNameTag(
    Entity entity,
    Hand hand,
    CallbackInfoReturnable<ActionResult> cir
  ) {
    ItemStack itemStack = ((PlayerEntity) (Object) this).getStackInHand(hand);
    PropertiesCache cache = PropertiesCache.getInstance();

    if (itemStack.getItem().getClass().equals(NameTagItem.class)
    && itemStack.hasCustomName()
    && entity instanceof EndCrystalEntity
    && !((EndCrystalEntity) entity).shouldShowBottom()
    && cache.getProperty("crystal-name").equals(itemStack.getName().getString())) {
      entity.setCustomName(itemStack.getName());

      itemStack.decrement(1);
      entity.setCustomNameVisible(true);

      cir.setReturnValue(ActionResult.success(((PlayerEntity) (Object) this).world.isClient));
    }
  }
}