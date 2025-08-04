package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

abstract class HTRangedItem(properties: Properties) : Item(properties.stacksTo(1)) {
    protected fun isActive(stack: ItemStack): Boolean = stack.getOrDefault(RagiumDataComponents.IS_ACTIVE, false)

    protected fun toggleActive(stack: ItemStack) {
        stack.update(RagiumDataComponents.IS_ACTIVE, false) { value: Boolean -> !value }
    }

    protected fun getRange(stack: ItemStack, level: Level?): Int {
        // val enchLookup: HolderLookup.RegistryLookup<Enchantment> = level?.registryAccess()?.enchLookup() ?: return 0
        val enchLevel: Int = 0 // stack.getAllEnchantments(enchLookup).getHighestLevel(RagiumModTags.Enchantments.RANGE)
        return RagiumAPI.getConfig().getEntityCollectorRange() * (enchLevel + 1)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!level.isClientSide && player.isShiftKeyDown) {
            val stack: ItemStack = player.getItemInHand(usedHand)
            toggleActive(stack)
            return InteractionResultHolder.consume(stack)
        }
        return super.use(level, player, usedHand)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        tooltips.add(
            Component
                .translatable(
                    RagiumTranslationKeys.TOOLTIP_EFFECT_RANGE,
                    intText(getRange(stack, context.level())).withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
    }

    override fun isFoil(stack: ItemStack): Boolean = isActive(stack)
}
