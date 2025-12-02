package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.tag.createTagKey
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * タグのプレフィックスを表すクラス
 *
 * [HTRegisterPrefixEvent]を通して登録する必要があります。
 */
@JvmRecord
data class HTMaterialPrefix(
    val name: String,
    private val commonTagPath: String = "${RagiumConst.COMMON}:${name}s",
    private val tagPath: String = "$commonTagPath/%s",
) : HTPrefixLike {
    companion object {
        @JvmField
        val DIRECT_CODEC: BiCodec<ByteBuf, HTMaterialPrefix> = BiCodec.composite(
            BiCodec.STRING.fieldOf("common_tag_path").forGetter(HTMaterialPrefix::commonTagPath),
            BiCodec.STRING.fieldOf("tag_path").forGetter(HTMaterialPrefix::tagPath),
            ::HTMaterialPrefix,
        )

        @JvmField
        val CODEC: BiCodec<ByteBuf, HTMaterialPrefix> = BiCodec.STRING.flatXmap(
            { name: String ->
                checkNotNull(RagiumPlatform.INSTANCE.getPrefix(name)) { "Unknown material prefix: $name" }
            },
            HTMaterialPrefix::name,
        )
    }

    override fun asMaterialPrefix(): HTMaterialPrefix = this

    override fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = key.createTagKey(ResourceLocation.parse(commonTagPath))

    override fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> =
        key.createTagKey(ResourceLocation.parse(tagPath.replace("%s", name)))
}
