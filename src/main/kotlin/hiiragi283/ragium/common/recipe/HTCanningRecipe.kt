package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTItemAndFluidRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCanningRecipe(
    fluidIngredient: HTFluidIngredient,
    itemIngredient: HTItemIngredient,
    val result: HTItemResult,
    parameters: SubParameters,
) : HTItemAndFluidRecipe(fluidIngredient, itemIngredient, parameters) {
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CANNING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CANNING.get()
}
