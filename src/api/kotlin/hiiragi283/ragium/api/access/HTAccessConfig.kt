package hiiragi283.ragium.api.access

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
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
        val CODEC: BiCodec<ByteBuf, HTAccessConfig> = BiCodecs.enum()
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
