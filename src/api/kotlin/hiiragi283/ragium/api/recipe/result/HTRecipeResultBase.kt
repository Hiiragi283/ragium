package hiiragi283.ragium.api.recipe.result

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.data.MapBiCodec
import hiiragi283.ragium.api.tag.HTTagHelper
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import java.util.function.Function

abstract class HTRecipeResultBase<T : Any, S : Any>(
    protected val entry: Either<ResourceKey<T>, TagKey<T>>,
    protected val amount: Int,
    protected val components: DataComponentPatch,
) : HTRecipeResult<S> {
    companion object {
        @JvmStatic
        fun <T : Any, R : HTRecipeResultBase<T, *>> createCodec(
            registryKey: ResourceKey<out Registry<T>>,
            amountCodec: MapBiCodec<ByteBuf, Int>,
            factory: (Either<ResourceKey<T>, TagKey<T>>, Int, DataComponentPatch) -> R,
        ): BiCodec<RegistryFriendlyByteBuf, R> = BiCodec.composite(
            BiCodecs.keyOrTag(registryKey).fieldOf("id"),
            HTRecipeResultBase<T, *>::entry,
            amountCodec,
            HTRecipeResultBase<T, *>::amount,
            BiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
            HTRecipeResultBase<T, *>::components,
            factory,
        )
    }

    protected fun getFirstHolder(): DataResult<out Holder<T>> = entry.map(::getFirstHolderFromId, ::getFirstHolderFromTag)

    protected fun getFirstHolderFromId(key: ResourceKey<T>): DataResult<out Holder<T>> = lookup
        ?.get(key)
        ?.map(DataResult<Holder<T>>::success)
        ?.orElse(DataResult.error { "Missing id in ${getRegistryId()}: $key" })
        ?: DataResult.error { "Could not find HolderLookup!" }

    protected fun getFirstHolderFromTag(tagKey: TagKey<T>): DataResult<out Holder<T>> = lookup
        ?.get(tagKey)
        ?.flatMap(HTTagHelper::getFirstHolder)
        ?.map(DataResult<Holder<T>>::success)
        ?.orElse(DataResult.error { "Missing tag in ${getRegistryId()}: ${tagKey.location}" })
        ?: DataResult.error { "Could not find HolderLookup!" }

    private fun getRegistryId(): ResourceLocation? = lookup?.key()?.location()

    protected abstract val lookup: HolderLookup.RegistryLookup<T>?

    protected abstract fun createStack(holder: Holder<T>, amount: Int, components: DataComponentPatch): DataResult<S>

    protected abstract val emptyStack: S

    //    HTRecipeResult    //

    final override val id: ResourceLocation = entry.map(ResourceKey<T>::location, TagKey<*>::location)

    final override fun getStackResult(): DataResult<S> =
        getFirstHolder().flatMap { holder: Holder<T> -> createStack(holder, amount, components) }

    final override fun hasNoMatchingStack(): Boolean = getStackResult().isError

    final override fun getOrEmpty(): S = getStackResult().mapOrElse(Function.identity()) { emptyStack }
}
