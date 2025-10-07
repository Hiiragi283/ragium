package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.extension.createCommonTag
import hiiragi283.ragium.api.material.HTMaterialVariant
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

enum class HTItemMaterialVariant(private val enPattern: String, private val jaPattern: String, private val tagPrefix: String) :
    HTMaterialVariant.ItemTag {
    // Item - Common
    DUST("%s Dust", "%sの粉", RagiumConst.DUSTS),
    GEM("%s", "%s", RagiumConst.GEMS),
    GEAR("%s Gear", "%sの歯車", "gears"),
    INGOT("%s Ingot", "%sインゴット", RagiumConst.INGOTS),
    NUGGET("%s Nugget", "%sナゲット", RagiumConst.NUGGETS),
    PLATE("%s Plate", "%s板", "plates"),
    RAW_MATERIAL("Raw %s", "%sの原石", "raw_materials"),
    ROD("%s Rod", "%s棒", "rods"),

    // Item - Custom
    CIRCUIT("%s Circuit", "%s回路", RagiumConst.CIRCUITS),
    FUEL("%s", "%s", RagiumConst.FUELS),
    CHIP("%s Chip", "%sチップ", RagiumConst.CHIPS),
    SCRAP("%s Scrap", "%sの欠片", RagiumConst.SCRAPS),
    ;

    override val itemCommonTag: TagKey<Item> = Registries.ITEM.createCommonTag(tagPrefix)

    override fun canGenerateTag(): Boolean = true

    override fun itemTagKey(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(tagPrefix, path)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
