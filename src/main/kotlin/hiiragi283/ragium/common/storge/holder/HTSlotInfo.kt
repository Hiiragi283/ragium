package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.ragium.common.text.RagiumTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component

enum class HTSlotInfo(val canInsert: Boolean, val canExtract: Boolean, val color: HTDefaultColor) : HTHasText {
    BOTH(true, true, HTDefaultColor.PURPLE),
    INPUT(true, false, HTDefaultColor.RED),
    OUTPUT(false, true, HTDefaultColor.LIGHT_BLUE),
    EXTRA_INPUT(true, false, HTDefaultColor.YELLOW),
    EXTRA_OUTPUT(false, true, HTDefaultColor.GREEN),
    NONE(false, false, HTDefaultColor.GRAY),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTSlotInfo> = BiCodecs.enum()
    }

    override fun getText(): Component = when (this) {
        BOTH -> RagiumTranslation.GUI_SLOT_BOTH
        INPUT -> RagiumTranslation.GUI_SLOT_INPUT
        OUTPUT -> RagiumTranslation.GUI_SLOT_OUTPUT
        EXTRA_INPUT -> RagiumTranslation.GUI_SLOT_EXTRA_INPUT
        EXTRA_OUTPUT -> RagiumTranslation.GUI_SLOT_EXTRA_OUTPUT
        NONE -> RagiumTranslation.GUI_SLOT_NONE
    }.translateColored(color)
}
