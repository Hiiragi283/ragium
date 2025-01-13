package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

/**
 * Represent [TagKey] prefixes
 */
enum class HTTagPrefix(
    val prefix: String,
    val enableAutoGen: Boolean = true,
) : StringRepresentable {
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
        val CODEC: Codec<HTTagPrefix> = stringCodec(HTTagPrefix.entries)

        @JvmField
        val PACKET_CODEC: StreamCodec<ByteBuf, HTTagPrefix> = stringStreamCodec(HTTagPrefix.entries)
    }

    //    Id    //

    open fun createPath(key: HTMaterialKey): String = "${key.name}_$serializedName"

    fun createId(
        key: HTMaterialKey,
        namespace: String = RagiumAPI.MOD_ID,
    ): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, createPath(key))

    //    TagKey    //

    val commonTagKey: TagKey<Item> = itemTagKey(commonId(prefix))

    fun createTag(key: HTMaterialKey): TagKey<Item> = itemTagKey(commonId("$prefix/${key.name}"))

    //    Translation    //

    val translationKey = "tag_prefix.${RagiumAPI.MOD_ID}.$serializedName"

    fun createText(key: HTMaterialKey): MutableComponent = Component.translatable(translationKey, key.text)

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
