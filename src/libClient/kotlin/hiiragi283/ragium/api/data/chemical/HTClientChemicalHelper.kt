package hiiragi283.ragium.api.data.chemical

import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
data object HTClientChemicalHelper {
    @JvmStatic
    fun addToTooltip(instance: ItemInstance, acceptor: (Text) -> Unit) {
        val holder: Holder<HTChemical> = instance.getData(RagiumDataMaps.CHEMICAL)?.chemical ?: return
        when {
            Minecraft.getInstance().hasShiftDown() -> holder.key?.toLanguageKey()?.let(::translatableText)
            else -> holder.value().getText()
        }?.withStyle(ChatFormatting.YELLOW)?.let(acceptor)
    }

    @JvmStatic
    fun addToTooltip(instance: FluidInstance, acceptor: (Text) -> Unit) {
        val holder: Holder<HTChemical> = instance.get(RagiumDataComponents.CHEMICAL) ?: return
        when {
            Minecraft.getInstance().hasShiftDown() -> holder.key?.toLanguageKey()?.let(::translatableText)
            else -> holder.value().getText()
        }?.withStyle(ChatFormatting.YELLOW)?.let(acceptor)
    }
}
