package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.Optional

data class HTAlloyingRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult, val extra: Optional<HTItemResult>) :
    HTRecipe {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = HTRecipeInput.hasMatchingSlots(ingredients, input.items)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = result.getStackOrNull(provider)
}
