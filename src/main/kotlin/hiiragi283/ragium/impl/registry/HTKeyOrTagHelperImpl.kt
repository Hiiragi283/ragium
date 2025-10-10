package hiiragi283.ragium.impl.registry

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

class HTKeyOrTagHelperImpl : HTKeyOrTagHelper {
    override fun <T : Any> create(registryKey: RegistryKey<T>, id: ResourceLocation): HTKeyOrTagEntry<T> =
        HTKeyOrTagEntryImpl.create(registryKey, id, false)

    override fun <T : Any> create(key: ResourceKey<T>): HTKeyOrTagEntry<T> =
        HTKeyOrTagEntryImpl.create(key.registryKey(), key.location(), false)

    override fun <T : Any> create(tagKey: TagKey<T>): HTKeyOrTagEntry<T> =
        HTKeyOrTagEntryImpl.create(tagKey.registry(), tagKey.location(), true)

    override fun <T : Any> codec(registryKey: RegistryKey<T>): BiCodec<ByteBuf, HTKeyOrTagEntry<T>> = BiCodecs
        .xor(VanillaBiCodecs.resourceKey(registryKey), VanillaBiCodecs.tagKey(registryKey))
        .xmap(
            { either: Either<ResourceKey<T>, TagKey<T>> ->
                either.map(HTKeyOrTagHelper.INSTANCE::create, HTKeyOrTagHelper.INSTANCE::create)
            },
            HTKeyOrTagEntry<T>::toEither,
        )
}
