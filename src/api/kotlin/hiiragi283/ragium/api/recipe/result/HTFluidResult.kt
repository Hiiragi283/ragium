package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutableOrThrow
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
            BiCodec.INT.fieldOf("amount"),
            ::HTFluidResult,
        )
    }

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): ImmutableFluidStack =
        FluidStack(holder, amount, components).toImmutableOrThrow()
}
