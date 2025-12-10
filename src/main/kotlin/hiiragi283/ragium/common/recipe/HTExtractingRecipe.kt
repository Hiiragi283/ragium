package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.common.recipe.base.HTBasicItemWithCatalystRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.Optional

class HTExtractingRecipe(ingredient: HTItemIngredient, catalyst: Optional<HTItemIngredient>, results: HTComplexResult) :
    HTBasicItemWithCatalystRecipe(ingredient, catalyst, results) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testItem(0, required)
        val bool2: Boolean = input.testCatalyst(1, optional)
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()

    override fun getRequiredCount(): Int = required.getRequiredAmount()
}
