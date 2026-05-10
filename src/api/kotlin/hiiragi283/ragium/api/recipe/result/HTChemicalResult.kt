package hiiragi283.ragium.api.recipe.result

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTListFluidResult
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

@JvmRecord
data class HTChemicalResult(val item: ItemStack, val first: FluidStack, val second: FluidStack) {
    companion object {
        @JvmStatic
        fun create(fluidResults: HTListFluidResult, itemResult: Optional<HTItemResult>): HTChemicalResult {
            val stacks: List<FluidStack> = fluidResults.toList()
            return HTChemicalResult(
                itemResult.map { it.getOrEmpty() }.orElseGet(ItemStack::EMPTY),
                stacks.first(),
                stacks.getOrNull(1) ?: FluidStack.EMPTY,
            )
        }
    }
}
