package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.ragium.common.text.RagiumTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

enum class HTSlotInfo(val canInsert: Boolean, val canExtract: Boolean, val color: ChatFormatting) : HTHasText {
    BOTH(true, true, ChatFormatting.DARK_PURPLE),
    INPUT(true, false, ChatFormatting.DARK_RED),
    OUTPUT(false, true, ChatFormatting.DARK_BLUE),
    EXTRA_INPUT(true, false, ChatFormatting.RED),
    EXTRA_OUTPUT(false, true, ChatFormatting.BLUE),
    NONE(false, false, ChatFormatting.GRAY),
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
