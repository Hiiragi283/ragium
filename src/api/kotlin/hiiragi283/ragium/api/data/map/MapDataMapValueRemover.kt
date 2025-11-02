package hiiragi283.ragium.api.data.map

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.serialization.codec.listOrElement
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover
import java.util.*

class MapDataMapValueRemover<R : Any, V : Any>(val keys: List<ResourceLocation>) : DataMapValueRemover<R, Map<ResourceLocation, V>> {
    companion object {
        @JvmStatic
        fun <R : Any, V : Any> codec(): Codec<MapDataMapValueRemover<R, V>> =
            ResourceLocation.CODEC.listOrElement().xmap(::MapDataMapValueRemover, MapDataMapValueRemover<R, V>::keys)
    }

    override fun remove(
        value: Map<ResourceLocation, V>,
        registry: Registry<R>,
        source: Either<TagKey<R>, ResourceKey<R>>,
        `object`: R,
    ): Optional<Map<ResourceLocation, V>> = Optional.of(value.filterKeys { location -> location !in keys })
}
