package hiiragi283.ragium.impl.recipe.result

import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

internal class HTFluidResultImpl(entry: HTKeyOrTagEntry<Fluid>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Fluid, FluidStack>(entry, amount, components),
    HTFluidResult {
    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): FluidStack {
        val stack = FluidStack(holder, amount, components)
        check(!stack.isEmpty) { "Empty fluid stack is not valid for recipe result" }
        return stack
    }

    override fun copyWithAmount(amount: Int): HTFluidResult = HTFluidResultImpl(entry, amount, components)
}
