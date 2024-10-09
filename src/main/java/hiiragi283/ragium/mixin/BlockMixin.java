package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.event.HTModifyBlockDropsCallback;
import hiiragi283.ragium.api.tags.RagiumEnchantmentTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void ragium$getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (EnchantmentHelper.getEnchantments(stack).getEnchantments().stream().anyMatch(entry -> entry.isIn(RagiumEnchantmentTags.MODIFYING_EXCLUSIVE_SET))) {
            var newDrops = HTModifyBlockDropsCallback.EVENT.invoker().modify(state, world, pos, blockEntity, entity, stack, cir.getReturnValue());
            cir.setReturnValue(newDrops);
        }
    }

}
