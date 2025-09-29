package hiiragi283.ragium.impl.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.filterNot
import hiiragi283.ragium.api.extension.wrapDataResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

internal class HTFluidResultImpl(entry: HTKeyOrTagEntry<Fluid>, amount: Int, components: DataComponentPatch) :
    HTRecipeResultBase<Fluid, FluidStack>(entry, amount, components),
    HTFluidResult {
    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): DataResult<FluidStack> =
        FluidStack(holder, amount, components)
            .wrapDataResult()
            .filterNot(FluidStack::isEmpty, "Empty fluid stack is not valid for recipe result")
}
