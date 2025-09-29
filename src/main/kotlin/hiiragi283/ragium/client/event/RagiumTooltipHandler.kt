package hiiragi283.ragium.client.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.component.TooltipProvider
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
        val context: Item.TooltipContext = event.context
        val flag: TooltipFlag = event.flags

        information(stack, consumer, flag)
        if (RagiumConfig.COMMON.showFoodEffect.asBoolean) {
            food(stack, consumer, event.context.tickRate())
        }
        workInProgress(stack, consumer)

        RagiumDataComponents.REGISTER
            .asSequence()
            .mapNotNull(stack::get)
            .filterIsInstance<TooltipProvider>()
            .forEach { provider: TooltipProvider -> provider.addToTooltip(context, consumer, flag) }
    }

    @JvmStatic
    private fun information(stack: ItemStack, consumer: (Component) -> Unit, flag: TooltipFlag) {
        if (stack.itemHolder.idOrThrow.namespace == RagiumAPI.MOD_ID) {
            val text: Component = RagiumTranslation.getTooltipText(stack) ?: return
            if (flag.hasShiftDown()) {
                consumer(text)
            } else {
                consumer(RagiumTranslation.TOOLTIP_SHOW_INFO.getColoredComponent(ChatFormatting.YELLOW))
            }
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
