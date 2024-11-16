package hiiragi283.ragium.mixin;

import hiiragi283.ragium.common.init.RagiumComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    
    @Unique
    private LivingEntity self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrementUnlessCreative(ILnet/minecraft/entity/LivingEntity;)V"), cancellable = true)
    private void ragium$eatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity entity = self();
        if (stack.contains(RagiumComponentTypes.DAMAGE_INSTEAD_OF_DECREASE)) {
            if (!entity.isInCreativeMode()) {
                stack.damage(1, entity, switch (entity.getActiveHand()) {
                    case MAIN_HAND -> EquipmentSlot.MAINHAND;
                    case OFF_HAND -> EquipmentSlot.OFFHAND;
                });
            }
            entity.emitGameEvent(GameEvent.EAT);
            cir.setReturnValue(stack);
        }
    }
    
    /*@Inject(method = "onEquipStack", at = @At("RETURN"))
    private void ragium$onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        HTEquippedArmorCallback.EVENT.invoker().onEquip(self(), slot, oldStack, newStack);
    }*/
    
}
