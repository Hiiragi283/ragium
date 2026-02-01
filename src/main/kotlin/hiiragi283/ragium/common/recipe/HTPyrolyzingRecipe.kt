package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTPyrolyzingRecipe(
    val ingredient: HTItemIngredient,
    val itemResult: HTItemResult,
    val fluidResult: HTFluidResult,
    parameters: SubParameters,
) : HTViewProcessingRecipe(parameters) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = fluidResult.getStackOrEmpty(provider)

    override fun matches(input: HTViewRecipeInput, level: Level): Boolean = ingredient.test(input.getItemView(0))

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemResult.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PYROLYZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PYROLYZING.get()
}
