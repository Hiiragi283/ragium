package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTTranslationFormatter
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

enum class HTTagPrefix(
    val prefix: String,
    override val enPattern: String,
    override val jaPattern: String,
    val enableAutoGen: Boolean = true,
) : HTTranslationFormatter,
    StringIdentifiable {
    DUST("dusts", "%s Dust", "%sの粉"),
    GEM("gems", "%s", "%s") {
        override fun createPath(key: HTMaterialKey): String = key.name
    },
    INGOT("ingots", "%s Ingot", "%sインゴット"),
    ORE("ores", "%s Ore", "%s鉱石", false),
    PLATE("plates", "%s Plate", "%s板"),
    RAW_MATERIAL("raw_materials", "Raw %s", "%sの原石") {
        override fun createPath(key: HTMaterialKey): String = "raw_${key.name}"
    },
    STORAGE_BLOCK("storage_blocks", "Block of %s", "%sブロック", false) {
        override fun createPath(key: HTMaterialKey): String = "${key.name}_block"
    },
    ;

    open fun createPath(key: HTMaterialKey): String = "${key.name}_${asString()}"

    fun createId(key: HTMaterialKey, namespace: String = RagiumAPI.MOD_ID): Identifier = Identifier.of(namespace, createPath(key))

    private fun commonId(path: String): Identifier = Identifier.of(TagUtil.C_TAG_NAMESPACE, path)

    val commonTagKey: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, commonId(prefix))

    fun createTag(value: StringIdentifiable): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, commonId("$prefix/${value.asString()}"))

    override fun asString(): String = name.lowercase()
}
