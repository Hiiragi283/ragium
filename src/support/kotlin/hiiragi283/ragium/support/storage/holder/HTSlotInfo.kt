package hiiragi283.ragium.support.storage.holder

import com.mojang.serialization.Codec
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.text.Text
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable

enum class HTSlotInfo(val canInsert: Boolean, val canExtract: Boolean, val color: HTDefaultColor) : StringRepresentable {
    BOTH(true, true, HTDefaultColor.PURPLE),
    INPUT(true, false, HTDefaultColor.RED),
    OUTPUT(false, true, HTDefaultColor.LIGHT_BLUE),
    EXTRA_INPUT(true, false, HTDefaultColor.YELLOW),
    EXTRA_OUTPUT(false, true, HTDefaultColor.GREEN),
    NONE(false, false, HTDefaultColor.GRAY),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTSlotInfo> = HTCodecs.stringEnum(HTSlotInfo::getSerializedName)
    }

    fun getText(side: Direction): Text = when (this) {
        BOTH -> RagiumTranslation.GUI_SLOT_BOTH
        INPUT -> RagiumTranslation.GUI_SLOT_INPUT
        OUTPUT -> RagiumTranslation.GUI_SLOT_OUTPUT
        EXTRA_INPUT -> RagiumTranslation.GUI_SLOT_EXTRA_INPUT
        EXTRA_OUTPUT -> RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT
        NONE -> RagiumTranslation.GUI_SLOT_NONE
    }.translateColored(color, HTDefaultColor.WHITE, side)

    override fun getSerializedName(): String = name.lowercase()
}
