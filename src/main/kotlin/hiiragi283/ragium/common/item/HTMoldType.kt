package hiiragi283.ragium.common.item

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.ragium.setup.RagiumItems
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class HTMoldType(private val enPattern: String, private val jaPattern: String, val prefix: HTTagPrefix?) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike.Delegated<Item> {
    BLANK("Blank", "空", null),
    BALL("Ball", "ボール", CommonTagPrefixes.PEARL),
    BLOCK("Block", "ブロック", CommonTagPrefixes.BLOCK),
    GEAR("Gear", "歯車", CommonTagPrefixes.GEAR),
    GEM("Gem", "宝石", CommonTagPrefixes.GEM),
    INGOT("Ingot", "インゴット", CommonTagPrefixes.INGOT),
    NUGGET("Nugget", "ナゲット", CommonTagPrefixes.NUGGET),
    PLATE("Plate", "板材", CommonTagPrefixes.PLATE),
    ROD("Rod", "棒材", CommonTagPrefixes.ROD),
    ;

    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTMoldType> = BiCodecs.stringEnum(HTMoldType::getSerializedName)
    }

    override fun getId(): ResourceLocation = getItemHolder().id

    override fun asItem(): Item = getItemHolder().asItem()

    override fun getItemHolder(): HTSimpleDeferredItem = RagiumItems.MOLDS[this]!!

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
