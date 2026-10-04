package hiiragi283.ragium.api.data.chemical

import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentGetter

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
data object HTClientChemicalHelper {
    @JvmStatic
    fun addToTooltip(getter: DataComponentGetter, acceptor: (Text) -> Unit) {
        val holder: Holder<HTChemical> = getter.get(RagiumDataComponents.CHEMICAL) ?: return
        when {
            Minecraft.getInstance().hasShiftDown() -> holder.key?.toLanguageKey()?.let(::translatableText)
            else -> holder.value().getText()
        }?.withStyle(ChatFormatting.YELLOW)?.let(acceptor)
    }
}
