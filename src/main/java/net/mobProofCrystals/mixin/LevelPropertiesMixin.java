package net.mobProofCrystals.mixin;

import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.mobProofCrystals.util.PropertiesCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimaryLevelData.class)
public class LevelPropertiesMixin {
    @Shadow
    private LevelSettings settings;

    @Inject(method = "getLevelName", at = @At("HEAD"))
    void preUpdate(CallbackInfoReturnable<GameRules> cir) {
        PropertiesCache.setLevelName(settings.levelName());
    }
}
