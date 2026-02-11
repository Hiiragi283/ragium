package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTSingleCatalystRecipe
import hiiragi283.ragium.common.recipe.input.HTSingleCatalystRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTSolidifyingRecipe(
    ingredient: HTFluidIngredient,
    catalyst: HTItemIngredient,
    val result: HTItemResult,
    parameters: SubParameters,
) : HTSingleCatalystRecipe<HTFluidIngredient>(ingredient, catalyst, parameters) {
    override fun matchIngredient(input: HTSingleCatalystRecipeInput): Boolean = ingredient.test(input.getFluid(0))

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
