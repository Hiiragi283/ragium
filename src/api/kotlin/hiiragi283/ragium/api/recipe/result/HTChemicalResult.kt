package hiiragi283.ragium.api.recipe.result

import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.getOrElse
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTChemicalResult(val item: ItemStack, val first: FluidStack, val second: FluidStack) {
    companion object {
        @JvmStatic
        fun create(fluidResults: List<HTFluidResult>, itemResult: Option<HTItemResult>): HTChemicalResult = HTChemicalResult(
            itemResult.map { it.createOrEmpty() }.getOrElse(ItemStack::EMPTY),
            fluidResults.first().create(),
            fluidResults.getOrNull(1)?.create() ?: FluidStack.EMPTY,
        )
    }
}
