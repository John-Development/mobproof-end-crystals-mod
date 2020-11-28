package net.mobProofCrystals.mixin;

import java.util.List;

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

@Mixin(SpawnHelper.class)
public abstract class EndCrystalMixin {

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

      Box box = new Box(monsterX + 32, monsterY + 1, monsterZ + 32, monsterX - 32, monsterY - 63, monsterZ - 32);

      List<EndCrystalEntity> crystals = world.getEntitiesByClass(EndCrystalEntity.class, box, null);

      if (crystals != null && !crystals.isEmpty()) {
        cir.setReturnValue(false);
          return;
      }
    }
      return;
  }
  
}
