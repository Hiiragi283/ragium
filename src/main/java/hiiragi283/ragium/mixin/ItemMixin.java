package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.event.HTInventoryTickCallback;
import hiiragi283.ragium.api.recipe.HTItemIngredient;
import hiiragi283.ragium.common.init.RagiumComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "canRepair", at = @At("RETURN"), cancellable = true)
    private void ragium$canRepair(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        HTItemIngredient repairment = stack.get(RagiumComponentTypes.REPAIRMENT);
        if (repairment != null) {
            boolean before = cir.getReturnValue();
            cir.setReturnValue(before || repairment.test(ingredient));
        }
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void ragium$inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        HTInventoryTickCallback.EVENT.invoker().inventoryTick(stack, world, entity, slot, selected);
    }
}
