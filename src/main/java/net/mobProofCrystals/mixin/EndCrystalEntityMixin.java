package net.mobProofCrystals.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static net.mobProofCrystals.util.CrystalPositions.isThereACrystal;
import static net.mobProofCrystals.util.CrystalPositions.positions;


@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {
  @Shadow @Final private static TrackedData<Optional<BlockPos>> BEAM_TARGET;
  @Shadow @Final private static TrackedData<Boolean> SHOW_BOTTOM;

  public EndCrystalEntityMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @Inject(
    method = "<init>(Lnet/minecraft/world/World;DDD)V",
    at = @At("RETURN")
  )
  void initianizer(World world, double x, double y, double z, CallbackInfo ci) {
    Vec3d pos = new Vec3d(x,y,z);

    if (!isThereACrystal(pos)) {
      positions.add(pos);
    }
  }

  @Inject(
    method = "crystalDestroyed",
    at= @At("HEAD")
  )
  void removeCrystal(DamageSource source, CallbackInfo ci) {
    positions.remove(this.getPos());
  }
}
