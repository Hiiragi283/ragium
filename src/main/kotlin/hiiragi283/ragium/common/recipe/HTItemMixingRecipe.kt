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
import net.minecraft.core.HolderLookup
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
    override fun assembleFluids(input: HTMixingRecipeInput, registries: HolderLookup.Provider): List<FluidStack> =
        listOfNotNull(result.getRight()?.getStackOrEmpty(registries))

    override fun assembleItems(input: HTMixingRecipeInput, registries: HolderLookup.Provider): List<ItemStack> =
        listOfNotNull(result.getLeft()?.getStackOrEmpty(registries))

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
