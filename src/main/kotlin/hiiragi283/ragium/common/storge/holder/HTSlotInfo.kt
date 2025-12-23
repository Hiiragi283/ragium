package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.Util

enum class HTSlotInfo(val canInsert: Boolean, val canExtract: Boolean, val color: ChatFormatting) : HTTranslation {
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

    override val translationKey: String = Util.makeDescriptionId("description", RagiumAPI.id("access.${name.lowercase()}"))
}
