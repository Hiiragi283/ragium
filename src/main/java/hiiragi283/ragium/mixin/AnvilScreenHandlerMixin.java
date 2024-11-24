package hiiragi283.ragium.mixin;

import hiiragi283.ragium.common.init.RagiumComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;canRepair(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean ragium$canRepair(Item instance, ItemStack stack, ItemStack ingredient) {
        var repairment = stack.get(RagiumComponentTypes.REPAIRMENT);
        if (repairment != null) {
            return repairment.test(ingredient);
        } else {
            return instance.canRepair(stack, ingredient);
        }
    }
}
