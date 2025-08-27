package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler

class HTPotionBundleItem(properties: Properties) : Item(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val handler: IItemHandler =
            stack.getCapability(Capabilities.ItemHandler.ITEM) ?: return stack
        for (i: Int in (0 until handler.slots)) {
            val stackIn: ItemStack = handler.getStackInSlot(i)
            if (stackIn.isEmpty) continue
            val result: ItemStack = stackIn.finishUsingItem(level, livingEntity)
            if (!livingEntity.hasInfiniteMaterials()){
                handler.extractItem(i, 1, false)
            }
            dropStackAt(livingEntity, result)
            return stack
        }
        return stack
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        if (stack.isEmpty) return InteractionResultHolder.fail(stack)
        val handler: IItemHandler =
            stack.getCapability(Capabilities.ItemHandler.ITEM) ?: return InteractionResultHolder.fail(stack)
        // 　シフト注はGUIを開く
        if (player.isShiftKeyDown) {
            RagiumMenuTypes.POTION_BUNDLE.openMenu(player, stack.hoverName, handler) { buf: RegistryFriendlyByteBuf ->
                ItemStack.STREAM_CODEC.encode(buf, stack)
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        } else {
            return ItemUtils.startUsingInstantly(level, player, usedHand)
        }
    }
}
