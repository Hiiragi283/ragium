package hiiragi283.ragium.api.util.access

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.toDescriptionKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component

enum class HTAccessConfig(val canInsert: Boolean, val canExtract: Boolean, val color: Int) :
    HTHasTranslationKey,
    HTHasText {
    INPUT_ONLY(true, false, 0xFF0033),
    OUTPUT_ONLY(false, true, 0x3300FF),
    BOTH(true, true, 0x00FF33),
    DISABLED(false, false, 0x333333),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTAccessConfig> = BiCodecs.enum(HTAccessConfig::values)
    }

    override val translationKey: String = RagiumAPI.id(name.lowercase()).toDescriptionKey("access")

    override fun getText(): Component = Component.translatable(translationKey)

    val nextEntry: HTAccessConfig
        get() = when (this) {
            INPUT_ONLY -> OUTPUT_ONLY
            OUTPUT_ONLY -> BOTH
            BOTH -> DISABLED
            DISABLED -> INPUT_ONLY
        }
}
