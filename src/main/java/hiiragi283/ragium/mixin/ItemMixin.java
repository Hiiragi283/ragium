package hiiragi283.ragium.mixin;

import hiiragi283.ragium.setup.RagiumDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    
    @Inject(method = "isValidRepairItem", at = @At("RETURN"), cancellable = true)
    private void ragium$isValidRepairItem(ItemStack stack, ItemStack repairCandidate, CallbackInfoReturnable<Boolean> cir) {
        var repairable = stack.get(RagiumDataComponents.REPAIRABLE);
        if (repairable != null) {
            cir.setReturnValue(repairable.getItems().contains(repairCandidate.getItemHolder()));
        }
    }
}
