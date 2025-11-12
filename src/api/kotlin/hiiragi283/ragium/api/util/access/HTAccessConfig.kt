package hiiragi283.ragium.api.util.access

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.Util

enum class HTAccessConfig(val canInsert: Boolean, val canExtract: Boolean, val color: Int) : HTTranslation {
    INPUT_ONLY(true, false, 0xFF0033),
    OUTPUT_ONLY(false, true, 0x3300FF),
    BOTH(true, true, 0x00FF33),
    DISABLED(false, false, 0x333333),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTAccessConfig> = BiCodecs.enum(HTAccessConfig::values)
    }

    override val translationKey: String = Util.makeDescriptionId("description", RagiumAPI.id("access.${name.lowercase()}"))

    val nextEntry: HTAccessConfig
        get() = when (this) {
            INPUT_ONLY -> OUTPUT_ONLY
            OUTPUT_ONLY -> BOTH
            BOTH -> DISABLED
            DISABLED -> INPUT_ONLY
        }
}
