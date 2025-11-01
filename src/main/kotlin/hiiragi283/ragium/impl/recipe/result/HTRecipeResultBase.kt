package hiiragi283.ragium.impl.recipe.result

import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.stack.ImmutableStack
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation

abstract class HTRecipeResultBase<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>>(
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
            HTKeyOrTagHelper.INSTANCE.codec(registryKey).fieldOf("id"),
            HTRecipeResultBase<T, *>::entry,
            amountCodec,
            HTRecipeResultBase<T, *>::amount,
            VanillaBiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
            HTRecipeResultBase<T, *>::components,
            factory,
        )
    }

    protected abstract fun createStack(holder: Holder<TYPE>, amount: Int, components: DataComponentPatch): STACK

    //    HTRecipeResult    //

    final override val id: ResourceLocation = entry.getId()

    final override fun getStackResult(provider: HolderLookup.Provider?): Result<STACK> =
        entry.getFirstHolder(provider).mapCatching { holder: Holder<TYPE> -> createStack(holder, amount, components) }
}
