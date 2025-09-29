package hiiragi283.ragium.mixin;

import hiiragi283.ragium.setup.RagiumDataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
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
    
    @Inject(method = "getDrinkingSound", at = @At("RETURN"), cancellable = true)
    public void ragium$getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        var sound = ragium$self().get(RagiumDataComponents.DRINK_SOUND);
        if (sound != null) {
            cir.setReturnValue(sound.getSound());
        }
    }

    @Inject(method = "getEatingSound", at = @At("RETURN"), cancellable = true)
    public void ragium$getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        var sound = ragium$self().get(RagiumDataComponents.EAT_SOUND);
        if (sound != null) {
            cir.setReturnValue(sound.getSound());
        }
    }
}
