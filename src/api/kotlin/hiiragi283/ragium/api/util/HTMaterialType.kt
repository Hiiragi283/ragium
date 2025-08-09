package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import kotlin.enums.enumEntries

enum class HTMaterialType(private val factory: (HTLanguageType, String) -> String) : StringRepresentable {
    // Mineral
    RAGINITE("Raginite", "ラギナイト"),
    CINNABAR("Cinnabar", "辰砂"),
    SALTPETER("Saltpeter", "硝石"),
    SULFUR("Sulfur", "硫黄"),

    // Gem
    RAGI_CRYSTAL("Ragi-Crystal", "ラギクリスタリル"),
    AZURE("Azure Shard", "紺碧の欠片"),
    CRIMSON_CRYSTAL("Crimson Crystal", "深紅の結晶"),
    WARPED_CRYSTAL("Warped Crystal", "歪んだ結晶"),
    ELDRITCH_PEARL("Eldritch Pearl", "異質な真珠"),

    // Metal
    RAGI_ALLOY("Ragi-Alloy", "ラギ合金"),
    ADVANCED_RAGI_ALLOY("Advanced Ragi-Alloy", "発展ラギ合金"),
    AZURE_STEEL("Azure Steel", "紺鉄"),
    DEEP_STEEL("Deep Steel", "深層鋼"),

    // Glass
    QUARTZ("Quartz", "水晶"),
    SOUL("Soul", "ソウル"),
    OBSIDIAN("Obsidian", "黒曜石"),

    // Tier
    BASIC("Basic", "基本"),
    ADVANCED("Advanced", "発展"),
    ELITE("Elite", "精鋭"),
    ULTIMATE("Ultimate", "究極"),

    // Food
    CHOCOLATE("Chocolate", "チョコレート"),
    MEAT("Meat", "生肉"),
    COOKED_MEAT("Cooked Meat", "焼肉"),

    // Other
    ASH("Ash", "灰"),
    WOOD({ type: HTLanguageType, _: String ->
        when (type) {
            HTLanguageType.EN_US -> "Sawdust"
            HTLanguageType.JA_JP -> "おがくず"
        }
    }),
    ;

    companion object {
        @JvmStatic
        inline fun <reified T> getFromVariant(material: HTMaterialType): T? where T : Provider, T : Enum<T> =
            enumEntries<T>().firstOrNull { holder: T -> holder.material == material }
    }

    constructor(enName: String, jpName: String) : this({ type: HTLanguageType, pattern: String ->
        pattern.replace(
            "%s",
            when (type) {
                HTLanguageType.EN_US -> enName
                HTLanguageType.JA_JP -> jpName
            },
        )
    })

    fun translate(type: HTLanguageType): String = translate(type, "%s")

    fun translate(type: HTLanguageType, pattern: String): String = factory(type, pattern)

    fun blockTag(prefix: String): TagKey<Block> = blockTagKey(commonId(prefix, serializedName))

    fun itemTag(prefix: String): TagKey<Item> = itemTagKey(commonId(prefix, serializedName))

    override fun getSerializedName(): String = name.lowercase()

    interface Provider {
        val material: HTMaterialType
    }
}
