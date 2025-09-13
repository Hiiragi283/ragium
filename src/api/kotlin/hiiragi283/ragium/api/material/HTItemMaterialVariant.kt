package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

enum class HTItemMaterialVariant(private val enUsPattern: String, private val jaJpPattern: String, private val tagPrefix: String) :
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
    ;

    override val itemCommonTag: TagKey<Item> = itemTagKey(commonId(tagPrefix))

    override fun canGenerateTag(): Boolean = true

    override fun itemTagKey(path: String): TagKey<Item> = itemTagKey(commonId("$tagPrefix/$path"))

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
