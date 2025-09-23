package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class HTSimulatingRecipe(
    override val ingredient: Optional<HTItemIngredient>,
    override val catalyst: HTItemIngredient,
    override val result: HTItemResult,
) : HTItemWithCatalystToItemRecipe {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMULATING.get()
}
