package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAssemblingRecipe(val itemIngredients: List<HTItemIngredient>, val result: HTItemResult, override val time: Int) :
    HTProcessingRecipe.Serializable<HTDoubleRecipeInput> {
    override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return itemIngredients[0].test(first) && itemIngredients[1].test(second)
    }

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ASSEMBLING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ASSEMBLING.get()
}
