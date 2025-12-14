package hiiragi283.ragium.common

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.util.StringRepresentable

enum class HTMoldType(private val enPattern: String, private val jaPattern: String) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike.Delegate {
    BLANK("Blank", "空"),
    STORAGE_BLOCK("Block", "ブロック"),
    GEM("Gem", "宝石"),
    GEAR("Gear", "歯車"),
    INGOT("Ingot", "インゴット"),
    PLATE("Plate", "板材"),
    ;

    val prefix: HTPrefixLike?
        get() = when (this) {
            BLANK -> null
            STORAGE_BLOCK -> CommonMaterialPrefixes.STORAGE_BLOCK
            GEM -> CommonMaterialPrefixes.GEM
            GEAR -> CommonMaterialPrefixes.GEAR
            INGOT -> CommonMaterialPrefixes.INGOT
            PLATE -> CommonMaterialPrefixes.PLATE
        }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }

    override fun getDelegate(): HTItemHolderLike = RagiumItems.MOLDS[this]!!

    override fun getSerializedName(): String = name.lowercase()
}
