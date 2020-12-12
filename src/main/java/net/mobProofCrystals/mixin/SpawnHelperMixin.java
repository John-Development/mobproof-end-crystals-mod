package net.mobProofCrystals.mixin;

import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import net.mobProofCrystals.util.PropertiesCache;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {

  @Inject(
    method = "canSpawn",
    at = @At("HEAD"),
    cancellable = true
  )
  private static void handleCustomSpawnRestriction(
    ServerWorld world,
    SpawnGroup group,
    StructureAccessor pAccessor,
    ChunkGenerator chunkGenerator,
    SpawnSettings.SpawnEntry spawnEntry,
    BlockPos.Mutable blockPos,
    double squaredDistance,
    CallbackInfoReturnable<Boolean> cir
  ) {
    if (group != null && group.compareTo(SpawnGroup.MONSTER) == 0) {
      double monsterX, monsterY, monsterZ = 0;
      monsterX = blockPos.getX();
      monsterY = blockPos.getY();
      monsterZ = blockPos.getZ();

      PropertiesCache cache = PropertiesCache.getInstance();
      int radius = Integer.parseInt(cache.getProperty("radius"));
      int lowerLimitDistance = Integer.parseInt(cache.getProperty("lower-limit-distance"));

      Box box = new Box(
        monsterX + radius,
        monsterY + lowerLimitDistance,
        monsterZ + radius,
        monsterX - radius,
        monsterY - 2 * radius - lowerLimitDistance,
        monsterZ - radius
      );

      List<EndCrystalEntity> crystals = world.getEntitiesByClass(EndCrystalEntity.class, box, new Predicate<EndCrystalEntity>() {
        @Override
          public boolean test(EndCrystalEntity crystal) {
            return (cache.getProperty("crystal-name").isBlank())
              ? cache.getProperty("crystal-name").equals(crystal.getName().asString())
              : true;
          }
        }
      );
      
      if (!crystals.isEmpty()) {
        cir.setReturnValue(false);
        return;
      }
    }
    return;
  }
  
}
