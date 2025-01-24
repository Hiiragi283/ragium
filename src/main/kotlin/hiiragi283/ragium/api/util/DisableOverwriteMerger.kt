package hiiragi283.ragium.api.util

import com.mojang.datafixers.util.Either
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger

class DisableOverwriteMerger<T : Any, R : Any> : DataMapValueMerger<T, R> {
    override fun merge(
        registry: Registry<T>,
        first: Either<TagKey<T>, ResourceKey<T>>,
        firstValue: R,
        second: Either<TagKey<T>, ResourceKey<T>>,
        secondValue: R,
    ): R = when (firstValue) {
        secondValue -> firstValue
        else -> error("Overwriting value is not supported!")
    }
}
