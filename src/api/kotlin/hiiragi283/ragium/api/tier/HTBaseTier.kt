package hiiragi283.ragium.api.tier

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.util.StringRepresentable

/**
 * Ragiumで使用する基本のティア
 * @see mekanism.api.tier.BaseTier
 */
enum class HTBaseTier(val color: ChatFormatting, private val enName: String, private val jpName: String) :
    StringRepresentable,
    HTTierProvider,
    HTLangName,
    HTTranslation {
    BASIC(ChatFormatting.GREEN, "Basic", "基本"),
    ADVANCED(ChatFormatting.YELLOW, "Advanced", "発展"),
    ELITE(ChatFormatting.AQUA, "Elite", "精鋭"),
    ULTIMATE(ChatFormatting.LIGHT_PURPLE, "Ultimate", "究極"),
    CREATIVE(ChatFormatting.RED, "Creative", "クリエイティブ"),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTBaseTier> = BiCodecs.stringEnum(HTBaseTier::getSerializedName)
    }

    override fun getBaseTier(): HTBaseTier = this

    override val translationKey: String = Util.makeDescriptionId("constants", RagiumAPI.id("tier.${name.lowercase()}"))

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun getSerializedName(): String = name.lowercase()
}
