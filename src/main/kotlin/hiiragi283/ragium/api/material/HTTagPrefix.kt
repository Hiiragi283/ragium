package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.identifiedCodec
import hiiragi283.ragium.api.extension.identifiedPacketCodec
import hiiragi283.ragium.api.extension.itemTagKey
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

/**
 * Represent [TagKey] prefixes
 */
enum class HTTagPrefix(val prefix: String, val enableAutoGen: Boolean = true) : StringIdentifiable {
    DUST("dusts"),
    GEAR("gears"),
    GEM("gems") {
        override fun createPath(key: HTMaterialKey): String = key.name
    },
    INGOT("ingots"),
    NUGGET("nuggets"),
    ORE("ores", false),
    PLATE("plates"),
    RAW_MATERIAL("raw_materials") {
        override fun createPath(key: HTMaterialKey): String = "raw_${key.name}"
    },
    ROD("rods"),
    STORAGE_BLOCK("storage_blocks", false) {
        override fun createPath(key: HTMaterialKey): String = "${key.name}_block"
    },
    WIRE("wires"),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTTagPrefix> = identifiedCodec(HTTagPrefix.entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTTagPrefix> = identifiedPacketCodec(HTTagPrefix.entries)

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

    val commonTagKey: TagKey<Item> = itemTagKey(commonId(prefix))

    fun createTag(key: HTMaterialKey): TagKey<Item> = itemTagKey(commonId("$prefix/${key.name}"))

    //    Translation    //

    val translationKey = "tag_prefix.${RagiumAPI.MOD_ID}.${asString()}"

    fun createText(key: HTMaterialKey): MutableText = Text.translatable(translationKey, key.text)

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
