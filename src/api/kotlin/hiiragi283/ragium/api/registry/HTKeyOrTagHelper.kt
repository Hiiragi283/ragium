package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * [HTKeyOrTagEntry]に関するヘルパー
 */
interface HTKeyOrTagHelper {
    companion object {
        /**
         * [HTKeyOrTagHelper]の実装のインスタンス
         */
        @JvmField
        val INSTANCE: HTKeyOrTagHelper = RagiumAPI.getService()
    }

    /**
     * 指定した[RegistryKey]と[ResourceLocation]から[HTKeyOrTagEntry]を返します。
     * @param T 種類のクラス
     */
    fun <T : Any> create(registryKey: RegistryKey<T>, id: ResourceLocation): HTKeyOrTagEntry<T>

    /**
     * 指定した[ResourceKey]から[HTKeyOrTagEntry]を返します。
     * @param T 種類のクラス
     */
    fun <T : Any> create(key: ResourceKey<T>): HTKeyOrTagEntry<T>

    /**
     * 指定した[TagKey]から[HTKeyOrTagEntry]を返します。
     * @param T 種類のクラス
     */
    fun <T : Any> create(tagKey: TagKey<T>): HTKeyOrTagEntry<T>

    /**
     * 指定した[ResourceKey]から[HTKeyOrTagEntry]の[BiCodec]を返します。
     * @param T 種類のクラス
     */
    fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<ByteBuf, HTKeyOrTagEntry<T>>
}
