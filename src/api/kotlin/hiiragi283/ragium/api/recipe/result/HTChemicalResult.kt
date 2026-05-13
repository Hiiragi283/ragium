package hiiragi283.ragium.api.recipe.result

import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

@JvmRecord
data class HTChemicalResult(val item: ItemStack, val first: FluidStack, val second: FluidStack) {
    companion object {
        @JvmStatic
        fun create(fluidResults: List<HTFluidResult>, itemResult: Optional<HTItemResult>): HTChemicalResult = HTChemicalResult(
            itemResult.flatMap { it.create().resultOrPartial() }.orElseGet(ItemStack::EMPTY),
            fluidResults.first().create(),
            fluidResults.getOrNull(1)?.create() ?: FluidStack.EMPTY,
        )
    }
}
