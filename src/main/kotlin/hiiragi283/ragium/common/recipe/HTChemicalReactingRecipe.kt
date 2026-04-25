package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

class HTChemicalReactingRecipe(
    val primary: HTFluidIngredient,
    val secondary: Ior<HTFluidIngredient, Ingredient>,
    val fluidResults: List<HTFluidResult>,
    val itemResult: Optional<HTItemResult>,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HTChemicalReactingRecipe.Input> {
    override fun test(input: Input): Boolean {
        val (catalyst: ItemStack, first: FluidStack, second: FluidStack) = input
        if (!primary.test(first)) return false
        return secondary.fold(
            { it.test(second) && catalyst.isEmpty },
            { it.test(catalyst) && second.isEmpty },
            { secondary, catalyst1 ->
                catalyst1.test(catalyst) && secondary.test(second)
            },
        )
    }

    override fun assemble(input: Input, preview: Boolean): ItemStack = itemResult.map { it.getOrEmpty(false) }.orElseGet(ItemStack::EMPTY)

    fun assembleFluids(): List<FluidStack> = fluidResults.map { it.getOrEmpty() }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CHEMICAL_REACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CHEMICAL_REACTING.get()

    @JvmRecord
    data class Input(val catalyst: ItemStack, val first: FluidStack, val second: FluidStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = when (index) {
            0 -> first
            1 -> second
            else -> error("No fluid for index $index")
        }

        override fun getFluidSize(): Int = 2

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> catalyst
            else -> error("No item for index $index")
        }

        override fun size(): Int = 1
    }
}
