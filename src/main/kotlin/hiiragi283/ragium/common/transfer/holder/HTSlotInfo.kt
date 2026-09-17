package hiiragi283.ragium.common.transfer.holder

import com.mojang.serialization.Codec
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.ChatFormatting
import net.minecraft.util.StringRepresentable

enum class HTSlotInfo(val canInsert: Boolean, val canExtract: Boolean, val color: ChatFormatting) :
    StringRepresentable {
    BOTH(true, true, ChatFormatting.LIGHT_PURPLE),
    INPUT(true, false, ChatFormatting.RED),
    OUTPUT(false, true, ChatFormatting.BLUE),
    EXTRA_INPUT(true, false, ChatFormatting.YELLOW),
    EXTRA_OUTPUT(false, true, ChatFormatting.GREEN),
    NONE(false, false, ChatFormatting.GRAY)
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTSlotInfo> = HTCodecs.stringEnum(HTSlotInfo::getSerializedName)
    }

    /*fun getText(side: Direction): Text = when (this) {
        BOTH -> RagiumTranslation.GUI_SLOT_BOTH
        INPUT -> RagiumTranslation.GUI_SLOT_INPUT
        OUTPUT -> RagiumTranslation.GUI_SLOT_OUTPUT
        EXTRA_INPUT -> RagiumTranslation.GUI_SLOT_EXTRA_INPUT
        EXTRA_OUTPUT -> RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT
        NONE -> RagiumTranslation.GUI_SLOT_NONE
    }.translateColored(color, HTDefaultColor.WHITE, side)*/

    override fun getSerializedName(): String = name.lowercase()
}
