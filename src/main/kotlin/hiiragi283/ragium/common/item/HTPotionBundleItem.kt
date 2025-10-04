package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.item.base.HTContainerItem
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class HTPotionBundleItem(properties: Properties) : HTContainerItem(properties.stacksTo(1)) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        for (slot: HTItemSlot in getItemSlots(stack)) {
            if (slot.isEmpty()) continue
            val stackIn: ItemStack = slot.getStack()
            val result: ItemStack = stackIn.finishUsingItem(level, livingEntity)
            if (!livingEntity.hasInfiniteMaterials()) {
                slot.extract(1, false, HTStorageAccess.INTERNAl)
            }
            if (result != stackIn) {
                dropStackAt(livingEntity, result)
            }
            return stack
        }
        return stack
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        if (stack.isEmpty) return InteractionResultHolder.fail(stack)
        // 　シフト中はGUIを開く
        return when {
            player.isShiftKeyDown -> InteractionResultHolder(
                RagiumMenuTypes.POTION_BUNDLE.openMenu(
                    player,
                    usedHand,
                    stack,
                ),
                stack,
            )

            else -> ItemUtils.startUsingInstantly(level, player, usedHand)
        }
    }
}
