package net.mobProofCrystals.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.AABB;
import net.mobProofCrystals.util.PropertiesCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(NaturalSpawner.class)
public abstract class SpawnHelperMixin {
  @Inject(
          method = "canSpawnMobAt",
          at = @At("HEAD"),
          cancellable = true
  )
  private static void handleCustomSpawnRestriction(
          ServerLevel level,
          StructureManager structureManager,
          ChunkGenerator generator,
          MobCategory mobCategory,
          MobSpawnSettings.SpawnerData spawnerData,
          BlockPos pos,
          CallbackInfoReturnable<Boolean> cir
  ) {
    if (mobCategory != null) {
      PropertiesCache cache = PropertiesCache.getInstance();

      List<String> mobs = Arrays.asList(cache.getProperty("mobs").split("\\s*,\\s*"));

      if (mobs.contains("all") || mobs.contains(mobCategory.getName()) || mobs.contains(spawnerData.type().toString().split("entity.minecraft.")[1])) {
        double monsterX, monsterY, monsterZ;
        monsterX = pos.getX();
        monsterY = pos.getY();
        monsterZ = pos.getZ();

        int radius = cache.getIntProperty("radius");
        int lowerLimitDistance = cache.getIntProperty("lower-limit-distance");

        AABB box = new AABB(
                monsterX + radius - 0.49,
                monsterY + lowerLimitDistance,
                monsterZ + radius - 0.49,
                monsterX - radius + 0.5,
                monsterY - 2 * radius - lowerLimitDistance + 3,
                monsterZ - radius + 0.5
        );

        List<EndCrystal> crystals = level.getEntitiesOfClass(
                EndCrystal.class,
                box,
                crystal -> cache.getProperty("crystal-name").trim().isEmpty()
                        || cache.getProperty("crystal-name").equals(crystal.getName().getString())
        );

        if (!crystals.isEmpty()) {
          cir.setReturnValue(false);
        }
      }
    }
  }
}
