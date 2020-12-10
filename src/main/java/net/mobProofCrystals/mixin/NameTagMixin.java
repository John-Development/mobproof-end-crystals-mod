package net.mobProofCrystals.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

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
    ActionResult actionResult = entity.interact((PlayerEntity) (Object) this, hand);
    ItemStack itemStack = ((PlayerEntity) (Object) this).getStackInHand(hand);
    // ActionResult actionResult2 = itemStack.useOnEntity((PlayerEntity) (Object) this, (LivingEntity)entity, hand);

    // if (itemStack.getClass().isInstance(NameTagItem.class))

    // System.out.println("patata" + itemStack.getItem().getClass() + itemStack.getItem().getClass().equals(NameTagItem.class));
    if (itemStack.getItem().getClass().equals(NameTagItem.class) && itemStack.hasCustomName() && entity.getType() == EntityType.END_CRYSTAL) {
      System.out.println("patata" + ((NameTagItem)itemStack.getItem()).);
      entity.setCustomNameVisible(true);
      entity.setCustomName(itemStack.getItem().asItem().);

      itemStack.setCount(itemStack.getCount() - 1);
      
      cir.setReturnValue(ActionResult.SUCCESS);
    }
    return;
  }
}
