package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

enum class HTTagPrefix(val prefix: String, val enableAutoGen: Boolean = true) : StringIdentifiable {
    DEEP_ORE("ores", false) {
        override fun createPath(key: HTMaterialKey): String = "deepslate_${key.name}_ore"
    },
    END_ORE("ores", false) {
        override fun createPath(key: HTMaterialKey): String = "end_${key.name}_ore"
    },
    DUST("dusts"),
    GEAR("gears"),
    GEM("gems") {
        override fun createPath(key: HTMaterialKey): String = key.name
    },
    INGOT("ingots"),
    NETHER_ORE("ores", false) {
        override fun createPath(key: HTMaterialKey): String = "nether_${key.name}_ore"
    },
    NUGGET("nuggets"),
    ORE("ores", false),
    PLATE("plates"),
    RAW_MATERIAL("raw_materials") {
        override fun createPath(key: HTMaterialKey): String = "raw_${key.name}"
    },
    STORAGE_BLOCK("storage_blocks", false) {
        override fun createPath(key: HTMaterialKey): String = "${key.name}_block"
    },
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTTagPrefix> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTTagPrefix> = packetCodecOf(entries)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTTagPrefix> =
            ComponentType
                .builder<HTTagPrefix>()
                .codec(CODEC)
                .packetCodec(PACKET_CODEC)
                .build()
    }

    //    Id    //

    open fun createPath(key: HTMaterialKey): String = "${key.name}_${asString()}"

    fun createId(key: HTMaterialKey, namespace: String = RagiumAPI.MOD_ID): Identifier = Identifier.of(namespace, createPath(key))

    //    TagKey    //

    private fun commonId(path: String): Identifier = Identifier.of(TagUtil.C_TAG_NAMESPACE, path)

    val commonTagKey: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, commonId(prefix))

    fun createTag(value: StringIdentifiable): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, commonId("$prefix/${value.asString()}"))

    //    Translation    //

    val translationKey = "tag_prefix.${RagiumAPI.MOD_ID}.${asString()}"

    fun getText(key: HTMaterialKey): MutableText = Text.translatable(translationKey, key.text)

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
