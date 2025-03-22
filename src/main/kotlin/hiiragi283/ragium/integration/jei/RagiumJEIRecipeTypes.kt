package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.common.init.RagiumRecipes
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

object RagiumJEIRecipeTypes {
    @JvmStatic
    fun create(recipeType: HTMachineRecipeType): RecipeType<RecipeHolder<HTMachineRecipe>> = RecipeType.createFromVanilla(recipeType)

    @JvmField
    val CENTRIFUGING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.CENTRIFUGING)

    @JvmField
    val CRUSHING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.CRUSHING)

    @JvmField
    val EXTRACTING: RecipeType<RecipeHolder<HTMachineRecipe>> = create(RagiumRecipes.EXTRACTING)
}
