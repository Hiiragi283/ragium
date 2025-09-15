package hiiragi283.ragium.api.registry

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import java.util.function.Function

interface HTKeyOrTagEntry<T : Any> : HTHolderLike {
    fun <U> map(fromKey: Function<ResourceKey<T>, U>, fromTag: Function<TagKey<T>, U>): U

    fun toEither(): Either<ResourceKey<T>, TagKey<T>> =
        map(Either<ResourceKey<T>, TagKey<T>>::left, Either<ResourceKey<T>, TagKey<T>>::right)

    fun getFirstHolder(provider: HolderLookup.Provider?): DataResult<out Holder<T>>
}
