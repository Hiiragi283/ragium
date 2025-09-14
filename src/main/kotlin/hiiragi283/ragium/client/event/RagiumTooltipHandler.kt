package hiiragi283.ragium.client.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RagiumAPI.MOD_ID)
object RagiumTooltipHandler {
    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        val consumer: (Component) -> Unit = event.toolTip::add
        val consumerTop: (Component) -> Unit = { text: Component ->
            event.toolTip.add(1, text)
        }
        val provider: HolderLookup.Provider? = event.context.registries()
        val flag: TooltipFlag = event.flags

        information(stack, consumerTop, flag)
        enchantmentInfo(stack, consumerTop, provider, flag)
        food(stack, consumer, event.context.tickRate())
        workInProgress(stack, consumer)
    }

    @JvmStatic
    private fun information(stack: ItemStack, consumer: (Component) -> Unit, flag: TooltipFlag) {
        if (stack.itemHolder.idOrThrow.namespace == RagiumAPI.MOD_ID) {
            val descKey: String = RagiumTranslation.getTooltipKey(stack)
            if (!I18n.exists(descKey)) return
            if (flag.hasShiftDown()) {
                consumer(Component.translatable(descKey).withStyle(ChatFormatting.GREEN))
            } else {
                consumer(RagiumTranslation.TOOLTIP_SHOW_INFO.getColoredComponent(ChatFormatting.YELLOW))
            }
        }
    }

    @JvmStatic
    private fun enchantmentInfo(
        stack: ItemStack,
        consumer: (Component) -> Unit,
        provider: HolderLookup.Provider?,
        flag: TooltipFlag,
    ) {
        stack.get(RagiumDataComponents.INTRINSIC_ENCHANTMENT)?.getFullName(provider?.enchLookup())?.let { text: Component ->
            when {
                flag.hasShiftDown() -> RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT.getComponent(text)
                else -> RagiumTranslation.TOOLTIP_SHOW_INFO.getColoredComponent(ChatFormatting.YELLOW)
            }.let(consumer)
        }
    }

    @JvmStatic
    private fun food(stack: ItemStack, consumer: (Component) -> Unit, tickRate: Float) {
        val food: FoodProperties = stack.getFoodProperties(null) ?: return
        val effects: List<FoodProperties.PossibleEffect> = food.effects
        if (effects.isNotEmpty()) {
            PotionContents.addPotionTooltip(
                effects.map(FoodProperties.PossibleEffect::effect),
                consumer,
                1f,
                tickRate,
            )
        }
    }

    @JvmStatic
    private fun workInProgress(stack: ItemStack, consumer: (Component) -> Unit) {
        if (stack.`is`(RagiumModTags.Items.WIP)) {
            consumer(RagiumTranslation.TOOLTIP_WIP.getColoredComponent(ChatFormatting.DARK_RED))
        }
    }
}
