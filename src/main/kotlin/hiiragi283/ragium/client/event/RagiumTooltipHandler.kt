package hiiragi283.ragium.client.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.idOrNull
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = RagiumAPI.Companion.MOD_ID)
object RagiumTooltipHandler {
    @SubscribeEvent
    fun onItemTooltip(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        val consumer: (Component) -> Unit = event.toolTip::add
        val consumerTop: (Component) -> Unit = { text: Component ->
            event.toolTip.add(1, text)
        }
        val flag: TooltipFlag = event.flags

        information(stack, consumerTop, flag)
        workInProgress(stack, consumer)
    }

    @JvmStatic
    private fun information(stack: ItemStack, consumer: (Component) -> Unit, flag: TooltipFlag) {
        if (stack.itemHolder.idOrNull?.namespace == RagiumAPI.MOD_ID) {
            val descKey: String = RagiumTranslationKeys.getTooltipKey(stack)
            if (!I18n.exists(descKey)) return
            if (flag.hasShiftDown()) {
                consumer(Component.translatable(descKey).withStyle(ChatFormatting.GREEN))
            } else {
                consumer(Component.translatable(RagiumTranslationKeys.TEXT_SHOW_INFO).withStyle(ChatFormatting.YELLOW))
            }
        }
    }

    @JvmStatic
    private fun workInProgress(stack: ItemStack, consumer: (Component) -> Unit) {
        if (stack.`is`(RagiumModTags.Items.WIP)) {
            consumer(Component.translatable(RagiumTranslationKeys.TEXT_WIP).withStyle(ChatFormatting.DARK_RED))
        }
    }
}
