package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.toDescriptionKey
import hiiragi283.ragium.api.text.HTHasComponent
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.util.StringRepresentable

enum class HTAccessConfiguration(val canInsert: Boolean, val canExtract: Boolean, val color: Int) :
    StringRepresentable,
    HTHasTranslationKey,
    HTHasComponent {
    INPUT_ONLY(true, false, 0xFF0033),
    OUTPUT_ONLY(false, true, 0x3300FF),
    BOTH(true, true, 0x00FF33),
    NONE(false, false, 0x333333),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<FriendlyByteBuf, HTAccessConfiguration> = BiCodecs.enum(HTAccessConfiguration::values)
    }

    override val translationKey: String = RagiumAPI.id(name.lowercase()).toDescriptionKey("access")

    override fun getComponent(): Component = Component.translatable(translationKey)

    val nextEntry: HTAccessConfiguration
        get() = when (this) {
            INPUT_ONLY -> OUTPUT_ONLY
            OUTPUT_ONLY -> BOTH
            BOTH -> NONE
            NONE -> INPUT_ONLY
        }

    override fun getSerializedName(): String = name.lowercase()

    //    Holder    //

    interface Holder {
        fun getAccessConfiguration(side: Direction): HTAccessConfiguration

        fun setAccessConfiguration(side: Direction, value: HTAccessConfiguration)
    }
}
