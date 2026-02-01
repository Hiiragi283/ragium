package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTAlloyingRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult, parameters: SubParameters) :
    HTViewProcessingRecipe(parameters) {
    override fun matches(input: HTViewRecipeInput, level: Level): Boolean =
        !HTShapelessRecipeHelper.shapelessMatch(ingredients, input.items).isEmpty()

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()
}
