package hiiragi283.ragium.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import hiiragi283.ragium.common.advancement.HTAdvancementRewardCallback;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class AdvancementRewardsMixin {

    @Shadow
    private ServerPlayerEntity owner;
    
    @Inject(method = "rewardEmptyAdvancements", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
    private void ragium$rewardEmptyAdvancements(ServerAdvancementLoader advancementLoader, CallbackInfo ci, @Local AdvancementEntry entry) {
        HTAdvancementRewardCallback.EVENT.invoker().onRewards(owner, entry);
    }
    
    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
    private void ragium$grantCriterion(AdvancementEntry entry, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        HTAdvancementRewardCallback.EVENT.invoker().onRewards(owner, entry);
    }

}
