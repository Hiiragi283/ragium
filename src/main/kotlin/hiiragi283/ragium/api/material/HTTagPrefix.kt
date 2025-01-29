package hiiragi283.ragium.api.material

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.toDataResult
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

/**
 * Represent [TagKey] prefixes
 */
enum class HTTagPrefix(private val commonName: String, private val tagPrefix: String = "$commonName/") : StringRepresentable {
    CLUMP("clumps"),
    CRYSTAL("crystals"),
    DIRTY_DUST("dirty_dusts"),
    DUST("dusts"),
    GEAR("gears"),
    GEM("gems") {
        override fun createPath(key: HTMaterialKey): String = key.name
    },
    INGOT("ingots"),
    NUGGET("nuggets"),
    ORE("ores"),
    PELLET("pellets"),
    PLATE("plates"),
    RAW_MATERIAL("raw_materials") {
        override fun createPath(key: HTMaterialKey): String = "raw_${key.name}"
    },
    RAW_STORAGE("storage_blocks", "storage_blocks/raw_"),
    ROD("rods"),
    SHARD("shards"),
    STORAGE_BLOCK("storage_blocks") {
        override fun createPath(key: HTMaterialKey): String = "${key.name}_block"
    },
    WIRE("wires"),
    ;

    companion object {
        @JvmStatic
        fun fromSerializedName(name: String): DataResult<HTTagPrefix> =
            HTTagPrefix.entries.firstOrNull { it.serializedName == name }.toDataResult { "Unknown prefix: $name" }
    }

    //    Id    //

    open fun createPath(key: HTMaterialKey): String = "${key.name}_$serializedName"

    fun createId(key: HTMaterialKey, namespace: String = RagiumAPI.MOD_ID): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(namespace, createPath(key))

    //    TagKey    //

    val commonTagKey: TagKey<Item> = itemTagKey(commonId(commonName))

    fun createTag(key: HTMaterialKey): TagKey<Item> = itemTagKey(commonId("$tagPrefix${key.name}"))

    //    Translation    //

    val translationKey = "tag_prefix.${RagiumAPI.MOD_ID}.$serializedName"

    fun createText(key: HTMaterialKey): MutableComponent = Component.translatable(translationKey, key.text)

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
