package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.component.HTConsumableData;
import hiiragi283.ragium.setup.RagiumComponentTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    
    @Unique
    private ItemStack ragium$self() {
        return (ItemStack) (Object) this;
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void ragium$getUseAnimation(CallbackInfoReturnable<UseAnim> cir) {
        HTConsumableData consumable = ragium$self().get(RagiumComponentTypes.CONSUMABLE);
        if (consumable != null) {
            cir.setReturnValue(consumable.getAnimation());
        }
    }

    @Inject(method = "getDrinkingSound", at = @At("HEAD"), cancellable = true)
    private void ragium$getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        HTConsumableData consumable = ragium$self().get(RagiumComponentTypes.CONSUMABLE);
        if (consumable != null) {
            cir.setReturnValue(consumable.getSound());
        }
    }

    @Inject(method = "getEatingSound", at = @At("HEAD"), cancellable = true)
    private void ragium$getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        HTConsumableData consumable = ragium$self().get(RagiumComponentTypes.CONSUMABLE);
        if (consumable != null) {
            cir.setReturnValue(consumable.getSound());
        }
    }

    @Inject(method = "getBreakingSound", at = @At("HEAD"), cancellable = true)
    private void getBreakingSound(CallbackInfoReturnable<SoundEvent> cir) {
        HTConsumableData consumable = ragium$self().get(RagiumComponentTypes.CONSUMABLE);
        if (consumable != null) {
            cir.setReturnValue(consumable.getSound());
        }
    }
}
