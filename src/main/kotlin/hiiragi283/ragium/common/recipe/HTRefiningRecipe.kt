package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HTRefiningRecipe(
    val ingredient: HTFluidIngredient,
    val result: HTFluidResult,
    val extraResult: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testFluid(0, ingredient)

    override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack =
        extraResult.getLeft()?.getStackOrEmpty(registries) ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
