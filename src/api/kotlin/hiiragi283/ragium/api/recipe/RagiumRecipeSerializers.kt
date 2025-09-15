package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSawmillRecipe
import hiiragi283.ragium.api.recipe.impl.HTSimulatingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer

interface RagiumRecipeSerializers {
    companion object {
        @JvmField
        val INSTANCE: RagiumRecipeSerializers = RagiumAPI.getService()
    }

    val sawmill: RecipeSerializer<HTSawmillRecipe>

    //    Machine    //

    val alloying: RecipeSerializer<HTAlloyingRecipe>
    val compressing: RecipeSerializer<HTCompressingRecipe>
    val crushing: RecipeSerializer<HTCrushingRecipe>
    val enchanting: RecipeSerializer<HTEnchantingRecipe>
    val extracting: RecipeSerializer<HTExtractingRecipe>
    val fluidTransform: RecipeSerializer<HTRefiningRecipe>
    val melting: RecipeSerializer<HTMeltingRecipe>
    val pulverizing: RecipeSerializer<HTPulverizingRecipe>
    val simulating: RecipeSerializer<HTSimulatingRecipe>
}
