package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.common.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

object RagiumJEIRecipeTypes {
    @JvmField
    val CENTRIFUGING: RecipeType<RecipeHolder<HTCentrifugingRecipe>> = RecipeType.createFromVanilla(RagiumRecipes.CENTRIFUGING)

    @JvmField
    val CRUSHING: RecipeType<RecipeHolder<HTCrushingRecipe>> = RecipeType.createFromVanilla(RagiumRecipes.CRUSHING)

    @JvmField
    val EXTRACTING: RecipeType<RecipeHolder<HTExtractingRecipe>> = RecipeType.createFromVanilla(RagiumRecipes.EXTRACTING)
}
