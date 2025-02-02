package hiiragi283.ragium.api.material

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.toDataResult
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

/**
 * Represent [TagKey] prefixes
 */
enum class HTTagPrefix(private val commonName: String, private val tagPrefix: String = "$commonName/") : StringRepresentable {
    // Common
    CASING("casings"),
    DUST("dusts"),
    GEAR("gears"),
    GEM("gems") {
        override fun createPath(key: HTMaterialKey): String = key.name
    },
    INGOT("ingots"),
    NUGGET("nuggets"),
    ORE("ores"),
    PLATE("plates"),
    RAW_MATERIAL("raw_materials") {
        override fun createPath(key: HTMaterialKey): String = "raw_${key.name}"
    },
    RAW_STORAGE("storage_blocks", "storage_blocks/raw_"),
    ROD("rods"),
    STORAGE_BLOCK("storage_blocks") {
        override fun createPath(key: HTMaterialKey): String = "${key.name}_block"
    },

    // Mekanism
    DIRTY_DUST("dirty_dusts"),
    CLUMP("clumps"),
    SHARD("shards"),
    CRYSTAL("crystals"),

    // MI
    TINY_DUST("tiny_dusts"),
    ;

    companion object {
        @JvmField
        val ORE_PARTS: List<HTTagPrefix> = listOf(
            ORE,
            RAW_MATERIAL,
            RAW_STORAGE,
        )

        @JvmField
        val METAL_PARTS: List<HTTagPrefix> = listOf(
            DUST,
            GEAR,
            INGOT,
            PLATE,
            ROD,
            STORAGE_BLOCK,
        )

        @JvmField
        val MEKANISM_PARTS: List<HTTagPrefix> = listOf(
            DIRTY_DUST,
            CLUMP,
            SHARD,
            CRYSTAL,
        )

        @JvmStatic
        fun fromSerializedName(name: String): DataResult<HTTagPrefix> =
            HTTagPrefix.entries.firstOrNull { it.serializedName == name }.toDataResult { "Unknown prefix: $name" }
    }

    //    Id    //

    open fun createPath(key: HTMaterialKey): String = "${key.name}_$serializedName"

    //    TagKey    //

    val commonTagKey: TagKey<Item> = itemTagKey(commonId(commonName))

    fun createTag(key: HTMaterialKey): TagKey<Item> = itemTagKey(commonId("$tagPrefix${key.name}"))

    //    Translation    //

    val translationKey = "tag_prefix.${RagiumAPI.MOD_ID}.$serializedName"

    fun createText(key: HTMaterialKey): MutableComponent = Component.translatable(translationKey, key.text)

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
