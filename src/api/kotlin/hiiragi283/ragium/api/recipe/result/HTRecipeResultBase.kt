package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.data.MapBiCodec
import hiiragi283.ragium.api.tag.HTKeyOrTagEntry
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

abstract class HTRecipeResultBase<T : Any, S : Any>(
    protected val entry: HTKeyOrTagEntry<T>,
    protected val amount: Int,
    protected val components: DataComponentPatch,
) : HTRecipeResult<S> {
    companion object {
        @JvmStatic
        fun <T : Any, R : HTRecipeResultBase<T, *>> createCodec(
            registryKey: ResourceKey<out Registry<T>>,
            amountCodec: MapBiCodec<ByteBuf, Int>,
            factory: (HTKeyOrTagEntry<T>, Int, DataComponentPatch) -> R,
        ): BiCodec<RegistryFriendlyByteBuf, R> = BiCodec.composite(
            HTKeyOrTagEntry.codec(registryKey).fieldOf("id"),
            HTRecipeResultBase<T, *>::entry,
            amountCodec,
            HTRecipeResultBase<T, *>::amount,
            BiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
            HTRecipeResultBase<T, *>::components,
            factory,
        )
    }

    protected abstract fun createStack(holder: Holder<T>, amount: Int, components: DataComponentPatch): DataResult<S>

    protected abstract val emptyStack: S

    //    HTRecipeResult    //

    final override val id: ResourceLocation = entry.id

    override fun getStackResult(provider: HolderLookup.Provider?): DataResult<S> =
        entry.getFirstHolder(provider).flatMap { holder: Holder<T> -> createStack(holder, amount, components) }

    final override fun getOrEmpty(provider: HolderLookup.Provider?): S =
        getStackResult(provider).mapOrElse(Function.identity()) { emptyStack }
}
