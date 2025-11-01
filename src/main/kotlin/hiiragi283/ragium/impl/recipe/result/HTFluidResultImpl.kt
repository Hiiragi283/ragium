package hiiragi283.ragium.impl.recipe.result

import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

internal class HTFluidResultImpl(entry: HTKeyOrTagEntry<Fluid>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Fluid, ImmutableFluidStack>(entry, amount, components),
    HTFluidResult {
    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): ImmutableFluidStack =
        FluidStack(holder, amount, components).toImmutableOrThrow()

    override fun copyWithAmount(amount: Int): HTFluidResult = HTFluidResultImpl(entry, amount, components)
}
