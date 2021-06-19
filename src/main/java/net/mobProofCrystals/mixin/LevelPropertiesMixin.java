package net.mobProofCrystals.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.GameRules;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.mobProofCrystals.util.PropertiesCache;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {
    @Shadow
    @Final
    private LevelInfo levelInfo;

    @Inject(method = "getLevelName", at = @At("HEAD"))
    void preUpdate(CallbackInfoReturnable<GameRules> cir) {
      PropertiesCache.setLevelName(levelInfo.getLevelName());
    }
}
