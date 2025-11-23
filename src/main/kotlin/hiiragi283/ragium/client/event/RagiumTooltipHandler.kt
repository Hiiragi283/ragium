package hiiragi283.ragium.client.event

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
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
import java.util.function.Consumer

@EventBusSubscriber(value = [Dist.CLIENT], modid = RagiumAPI.MOD_ID)
object RagiumTooltipHandler {
    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        val context: Item.TooltipContext = event.context
        val consumer: Consumer<Component> = Consumer { event.toolTip.add(1, it) }
        val consumer1: Consumer<Component> = Consumer(event.toolTip::add)
        val flag: TooltipFlag = event.flags

        information(stack, consumer, flag)
        if (RagiumConfig.COMMON.showFoodEffect.asBoolean) {
            food(stack, consumer, event.context.tickRate())
        }
        machineUpgrade(stack, consumer1)
        workInProgress(stack, consumer1)

        RagiumDataComponents.REGISTER
            .asSequence()
            .mapNotNull(stack::get)
            .filterIsInstance<TooltipProvider>()
            .forEach { provider: TooltipProvider -> provider.addToTooltip(context, consumer1, flag) }
    }

    @JvmStatic
    private fun information(stack: ItemStack, consumer: Consumer<Component>, flag: TooltipFlag) {
        val translation: HTTranslation = stack.get(RagiumDataComponents.DESCRIPTION) ?: return
        if (flag.hasShiftDown()) {
            consumer.accept(translation.translate())
        } else {
            consumer.accept(RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW))
        }
    }

    @JvmStatic
    private fun food(stack: ItemStack, consumer: Consumer<Component>, tickRate: Float) {
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
    private fun machineUpgrade(stack: ItemStack, consumer: Consumer<Component>) {
        RagiumPlatform.INSTANCE.getMachineUpgrade(stack)?.addToTooltip(consumer)
    }

    @JvmStatic
    private fun workInProgress(stack: ItemStack, consumer: Consumer<Component>) {
        if (stack.`is`(RagiumModTags.Items.WIP)) {
            consumer.accept(RagiumTranslation.TOOLTIP_WIP.translateColored(ChatFormatting.DARK_RED))
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
