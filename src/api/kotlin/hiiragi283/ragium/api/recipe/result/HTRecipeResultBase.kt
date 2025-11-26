package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.text.HTTextResult
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation

abstract class HTRecipeResultBase<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>>(
    val entry: HTKeyOrTagEntry<TYPE>,
    val amount: Int,
    val components: DataComponentPatch,
) : HTRecipeResult<STACK> {
    companion object {
        @JvmStatic
        fun <T : Any, R : HTRecipeResultBase<T, *>> createCodec(
            registryKey: RegistryKey<T>,
            amountCodec: MapBiCodec<ByteBuf, Int>,
            factory: (HTKeyOrTagEntry<T>, Int, DataComponentPatch) -> R,
        ): BiCodec<RegistryFriendlyByteBuf, R> = BiCodec.composite(
            HTKeyOrTagHelper.INSTANCE.mapCodec(registryKey),
            HTRecipeResultBase<T, *>::entry,
            amountCodec,
            HTRecipeResultBase<T, *>::amount,
            VanillaBiCodecs.COMPONENT_PATCH.optionalFieldOf(RagiumConst.COMPONENTS, DataComponentPatch.EMPTY),
            HTRecipeResultBase<T, *>::components,
            factory,
        )
    }

    protected abstract fun createStack(holder: Holder<TYPE>, amount: Int, components: DataComponentPatch): HTTextResult<STACK>

    //    HTRecipeResult    //

    final override val id: ResourceLocation = entry.getId()

    final override fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<STACK> =
        entry.getFirstHolder(provider).flatMap { holder: Holder<TYPE> -> createStack(holder, amount, components) }
}
