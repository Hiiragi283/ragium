package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.extension.RegistryKey
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

interface HTKeyOrTagHelper {
    companion object {
        @JvmField
        val INSTANCE: HTKeyOrTagHelper = RagiumAPI.getService()
    }

    fun <T : Any> create(registryKey: RegistryKey<T>, id: ResourceLocation): HTKeyOrTagEntry<T>

    fun <T : Any> create(key: ResourceKey<T>): HTKeyOrTagEntry<T>

    fun <T : Any> create(tagKey: TagKey<T>): HTKeyOrTagEntry<T>

    fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<ByteBuf, HTKeyOrTagEntry<T>>
}
