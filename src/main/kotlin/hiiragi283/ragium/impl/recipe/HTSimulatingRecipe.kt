package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithCatalystRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.Optional

class HTSimulatingRecipe(catalyst: HTItemIngredient, ingredient: Optional<HTItemIngredient>, results: HTComplexResult) :
    HTBasicItemWithCatalystRecipe(catalyst, ingredient, results) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testCatalyst(0, optional)
        val bool2: Boolean = input.testItem(1, required)
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMULATING.get()

    override fun getRequiredCount(): Int = optional.map(HTItemIngredient::getRequiredAmount).orElse(0)
}
