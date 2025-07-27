package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.util.HTPotionBundle
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.BundleItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.Items
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import java.util.Optional

/**
 * @see [BundleItem]
 */
class HTPotionBundleItem(properties: Properties) : Item(properties) {
    private fun hasFirstPotion(stack: ItemStack): Boolean {
        val bundle: HTPotionBundle = stack.get(RagiumDataComponents.POTION_BUNDLE) ?: return false
        return !bundle.isEmpty
    }

    override fun overrideStackedOnOther(
        stack: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
    ): Boolean {
        if (stack.count != 1) return false
        if (action != ClickAction.SECONDARY) return false
        val bundle: HTPotionBundle = stack.get(RagiumDataComponents.POTION_BUNDLE) ?: return false
        val mutable = HTPotionBundle.Mutable(bundle)
        val stackIn: ItemStack = slot.item
        if (stackIn.isEmpty) {
            val removed: ItemStack? = mutable.removeOne()
            if (removed != null) {
                val inserted: ItemStack = slot.safeInsert(removed)
                mutable.tryInsert(inserted)
            }
        }
        stack.set(RagiumDataComponents.POTION_BUNDLE, mutable.toImmutable())
        return true
    }

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess,
    ): Boolean {
        if (stack.count != 1) return false
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            val bundle: HTPotionBundle = stack.get(RagiumDataComponents.POTION_BUNDLE) ?: return false
            val mutable = HTPotionBundle.Mutable(bundle)
            if (other.isEmpty) {
                val stackIn: ItemStack? = mutable.removeOne()
                if (stackIn != null) {
                    access.set(stackIn)
                }
            } else if (other.`is`(Items.POTION)) {
                mutable.tryInsert(other)
            }
            stack.set(RagiumDataComponents.POTION_BUNDLE, mutable.toImmutable())
            return true
        } else {
            return false
        }
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val bundle: HTPotionBundle = stack.get(RagiumDataComponents.POTION_BUNDLE) ?: return stack
        val mutable = HTPotionBundle.Mutable(bundle)
        val firstStack: ItemStack = mutable.removeOne() ?: return stack
        firstStack.finishUsingItem(level, livingEntity)
        stack.set(RagiumDataComponents.POTION_BUNDLE, mutable.toImmutable())
        return stack
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = when {
        hasFirstPotion(stack) -> 32
        else -> super.getUseDuration(stack, entity)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = when {
        hasFirstPotion(stack) -> UseAnim.DRINK
        else -> super.getUseAnimation(stack)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        ItemUtils.startUsingInstantly(level, player, usedHand)

    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> {
        if (stack.has(DataComponents.HIDE_TOOLTIP)) return Optional.empty()
        if (stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) return Optional.empty()
        val bundle: HTPotionBundle = stack.get(RagiumDataComponents.POTION_BUNDLE) ?: return Optional.empty()
        if (bundle.isEmpty) return Optional.empty()
        return Optional.ofNullable(bundle)
    }

    override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
        val stack: ItemStack = itemEntity.item
        val bundle: HTPotionBundle = stack.get(RagiumDataComponents.POTION_BUNDLE) ?: return
        stack.set(RagiumDataComponents.POTION_BUNDLE, HTPotionBundle.EMPTY)
        ItemUtils.onContainerDestroyed(itemEntity, bundle.itemsCopy())
    }
}
