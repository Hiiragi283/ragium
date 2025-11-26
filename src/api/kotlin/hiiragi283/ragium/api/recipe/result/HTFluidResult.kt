package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [ImmutableFluidStack]向けの[HTRecipeResult]の実装
 */
class HTFluidResult(entry: HTKeyOrTagEntry<Fluid>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Fluid, ImmutableFluidStack>(entry, amount, components) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = createCodec(
            Registries.FLUID,
            BiCodec.INT.fieldOf(RagiumConst.AMOUNT),
            ::HTFluidResult,
        )
    }

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): HTTextResult<ImmutableFluidStack> =
        when (val stack: ImmutableFluidStack? = FluidStack(holder, amount, components).toImmutable()) {
            null -> HTTextResult.failure(RagiumTranslation.EMPTY)
            else -> HTTextResult.success(stack)
        }
}
