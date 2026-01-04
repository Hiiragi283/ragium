package hiiragi283.ragium.common.item

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.ragium.setup.RagiumItems
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class HTMoldType(private val enPattern: String, private val jaPattern: String) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike<Item> {
    BLANK("Blank", "空"),
    BALL("Ball", "ボール"),
    BLOCK("Block", "ブロック"),
    GEAR("Gear", "歯車"),
    GEM("Gem", "宝石"),
    INGOT("Ingot", "インゴット"),
    NUGGET("Nugget", "ナゲット"),
    PLATE("Plate", "板材"),
    ROD("Rod", "棒材"),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTMoldType> = BiCodecs.stringEnum(HTMoldType::getSerializedName)
    }

    override fun getId(): ResourceLocation = getHolder().id

    override fun getHolder(): HTSimpleDeferredItem = RagiumItems.MOLDS[this]!!

    override fun get(): Item = getHolder().get()

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
