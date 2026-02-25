package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAlloyingRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult, override val time: Int) :
    HTProcessingRecipe.Serializable<HTShapelessRecipeInput> {
    override fun assemble(input: HTShapelessRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()

    override fun test(input: HTShapelessRecipeInput): Boolean = !HTShapelessRecipeHelper.shapelessMatch(ingredients, input.items).isEmpty()
}
