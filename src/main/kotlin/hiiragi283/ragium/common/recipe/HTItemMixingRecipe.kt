package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTItemMixingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredient: HTFluidIngredient,
    val result: Ior<HTItemResult, HTFluidResult>,
    override val time: Int,
) : HTMixingRecipe.Serializable {
    override fun assembleFluids(input: HTMixingRecipeInput): List<FluidStack> =
        listOfNotNull(result.getRight()?.getOrEmpty()).filterNot(FluidStack::isEmpty)

    override fun assembleItems(input: HTMixingRecipeInput, preview: Boolean): List<ItemStack> =
        listOfNotNull(result.getLeft()?.getOrEmpty(preview)).filterNot(ItemStack::isEmpty)

    override fun getRequiredAmounts(input: HTMixingRecipeInput): HTMixingRecipe.RequiredAmounts {
        val (firstItem: ItemStack, secondItem: ItemStack, firstFluid: FluidStack, _: FluidStack) = input
        val amounts = IntArray(4) { 0 }
        // first fluid
        if (fluidIngredient.test(firstFluid)) {
            amounts[2] = fluidIngredient.amount
        }
        // items
        val (firstItemIng: HTItemIngredient, secondItemIng: HTItemIngredient) = itemIngredients
        if (firstItemIng.test(firstItem) && secondItemIng.test(secondItem)) {
            amounts[0] = firstItemIng.amount
            amounts[1] = secondItemIng.amount
        } else if (firstItemIng.test(secondItem) && secondItemIng.test(firstItem)) {
            amounts[1] = firstItemIng.amount
            amounts[0] = secondItemIng.amount
        }
        return HTMixingRecipe.RequiredAmounts(amounts)
    }

    override fun test(input: HTMixingRecipeInput): Boolean {
        val (firstItem: ItemStack, secondItem: ItemStack, firstFluid: FluidStack, _: FluidStack) = input
        return when {
            itemIngredients[0].test(firstItem) && itemIngredients[1].test(secondItem) ->
                fluidIngredient.test(firstFluid)
            itemIngredients[1].test(firstItem) && itemIngredients[0].test(secondItem) ->
                fluidIngredient.test(firstFluid)
            else -> false
        }
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ITEM_MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
