package hiiragi283.ragium.client.event

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.text.HTTranslation
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
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RagiumAPI.MOD_ID)
object RagiumTooltipHandler {
    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        val tooltips: MutableList<Component> = event.toolTip
        val context: Item.TooltipContext = event.context
        val flag: TooltipFlag = event.flags

        information(stack, tooltips, flag)
        if (RagiumConfig.COMMON.showFoodEffect.asBoolean) {
            food(stack, tooltips, event.context.tickRate())
        }
        workInProgress(stack, tooltips)

        RagiumDataComponents.REGISTER
            .asSequence()
            .mapNotNull(stack::get)
            .filterIsInstance<TooltipProvider>()
            .forEach { provider: TooltipProvider -> provider.addToTooltip(context, tooltips::add, flag) }
    }

    @JvmStatic
    private fun information(stack: ItemStack, tooltips: MutableList<Component>, flag: TooltipFlag) {
        val translation: HTTranslation = stack.get(RagiumDataComponents.DESCRIPTION) ?: return
        if (flag.hasShiftDown()) {
            tooltips.add(1, translation.translate())
        } else {
            tooltips.add(1, RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW))
        }
    }

    @JvmStatic
    private fun food(stack: ItemStack, tooltips: MutableList<Component>, tickRate: Float) {
        val food: FoodProperties = stack.getFoodProperties(null) ?: return
        val effects: List<FoodProperties.PossibleEffect> = food.effects
        if (effects.isNotEmpty()) {
            PotionContents.addPotionTooltip(
                effects.map(FoodProperties.PossibleEffect::effect),
                tooltips::add,
                1f,
                tickRate,
            )
        }
    }

    @JvmStatic
    private fun workInProgress(stack: ItemStack, tooltips: MutableList<Component>) {
        if (stack.`is`(RagiumModTags.Items.WIP)) {
            tooltips.add(1, RagiumTranslation.TOOLTIP_WIP.translateColored(ChatFormatting.DARK_RED))
        }
    }

    @SubscribeEvent
    fun gatherClientComponents(event: RenderTooltipEvent.GatherComponents) {
        val stack: ItemStack = event.itemStack
        val contents: HTItemContents = stack.get(RagiumDataComponents.ITEM_CONTENT) ?: return
        contents.indices
            .mapNotNull(contents::get)
            .map(::HTItemTooltipContent)
            .forEach { content: HTItemTooltipContent ->
                event.tooltipElements.add(Either.right(content))
            }
    }
}
