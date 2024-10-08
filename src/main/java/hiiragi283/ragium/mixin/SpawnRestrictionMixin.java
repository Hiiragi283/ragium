package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.event.HTAllowSpawnCallback;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnRestriction.class)
public abstract class SpawnRestrictionMixin {

    @Inject(method = "canSpawn", at = @At("TAIL"), cancellable = true)
    private static <T extends Entity> void ragium$canSpawn(EntityType<T> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        TriState result = HTAllowSpawnCallback.EVENT.invoker().canSpawn(type, world, pos, spawnReason);
        if (result == TriState.TRUE) {
            cir.setReturnValue(true);
        }
        if (result == TriState.FALSE) {
            cir.setReturnValue(null);
        }
    }

}
