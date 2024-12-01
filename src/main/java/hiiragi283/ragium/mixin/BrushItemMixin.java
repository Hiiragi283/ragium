package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.event.HTBrushingCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin {

    @Shadow
    protected abstract HitResult getHitResult(PlayerEntity user);

    @Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V"))
    private void ragium$usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) user;
        HitResult hitResult = getHitResult(player);
        if (hitResult instanceof BlockHitResult blockHitResult) {
            if (HTBrushingCallback.EVENT.invoker().onBrush(world, player, stack, blockHitResult)) {
                EquipmentSlot equipmentSlot = stack.equals(player.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                stack.damage(1, user, equipmentSlot);
            }
        }
    }

}
