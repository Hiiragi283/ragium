package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTViewRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTPressingRecipe(
    val ingredient: HTItemIngredient,
    val catalyst: HTItemIngredient,
    val result: HTItemResult,
    val copyComponent: Boolean,
    parameters: SubParameters,
) : HTProcessingRecipe<HTViewRecipeInput>(parameters) {
    override fun matches(input: HTViewRecipeInput, level: Level): Boolean {
        val bool1: Boolean = ingredient.test(input.getItem(0))
        val bool2: Boolean = input.catalyst?.let(catalyst::testOnlyType) ?: false
        return bool1 && bool2
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRESSING.get()
}
