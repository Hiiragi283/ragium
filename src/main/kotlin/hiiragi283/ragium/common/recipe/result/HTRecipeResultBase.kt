package hiiragi283.ragium.common.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.codec.MapBiCodec
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.common.util.HTKeyOrTagEntry
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation

abstract class HTRecipeResultBase<TYPE : Any, STACK : Any>(
    protected val entry: HTKeyOrTagEntry<TYPE>,
    protected val amount: Int,
    protected val components: DataComponentPatch,
) : HTRecipeResult<STACK> {
    companion object {
        @JvmStatic
        fun <T : Any, R : HTRecipeResultBase<T, *>> createCodec(
            registryKey: RegistryKey<T>,
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

    protected abstract fun createStack(holder: Holder<TYPE>, amount: Int, components: DataComponentPatch): DataResult<STACK>

    //    HTRecipeResult    //

    final override val id: ResourceLocation = entry.id

    final override fun getStackResult(provider: HolderLookup.Provider?): DataResult<STACK> =
        entry.getFirstHolder(provider).flatMap { holder: Holder<TYPE> -> createStack(holder, amount, components) }
}
