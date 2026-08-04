package hiiragi283.lib.tag

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.resource.toId
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey

/**
 * ジェネリクスのない[TagKey]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class RawTagKey(val location: Identifier) {
    companion object {
        @JvmField
        val CODEC: Codec<RawTagKey> = Identifier.CODEC.xmap(::RawTagKey, RawTagKey::location)

        @JvmField
        val HASHED_CODEC: Codec<RawTagKey> = Codec.STRING.comapFlatMap(
            { value: String ->
                when {
                    value.startsWith("#") ->
                        value
                            .substring(1)
                            .let(Identifier::read)
                            .map(::RawTagKey)
                    else -> DataResult.error { "Not a tag id" }
                }
            },
            { "#${it.location}" },
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, RawTagKey> = Identifier.STREAM_CODEC.map(::RawTagKey, RawTagKey::location)

        @JvmStatic
        fun common(path: String): RawTagKey = RawTagKey(HTConstants.COMMON, path)

        @JvmStatic
        fun common(vararg path: String): RawTagKey = RawTagKey(HTConstants.COMMON, *path)

        @JvmStatic
        fun copy(parent: TagKey<*>): RawTagKey = RawTagKey(parent.location())
    }

    fun withPrefix(prefix: String): RawTagKey = RawTagKey(location.withPrefix(prefix))

    fun withSuffix(suffix: String): RawTagKey = RawTagKey(location.withSuffix(suffix))

    fun withPath(transform: (String) -> String): RawTagKey = RawTagKey(location.withPath(transform))

    /**
     * [TagKey]に変換します。
     * @param T レジストリの要素のクラス
     * @param key レジストリのキー
     */
    fun <T : Any> create(key: RegistryKey<T>): TagKey<T> = TagKey.create(key, location)
}

//    Extensions    //

/**
 * 新しい[RawTagKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RawTagKey(namespace: String, path: String): RawTagKey = RawTagKey(namespace.toId(path))

/**
 * 新しい[RawTagKey]のインスタンスを作成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RawTagKey(namespace: String, vararg path: String): RawTagKey = RawTagKey(namespace.toId(*path))
