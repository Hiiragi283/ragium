package hiiragi283.lib.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.Identifier

/**
 * Hiiragi Seriesで使用される[IModPlugin]の抽象クラスです。
 *
 * 参照 : [Mekanism - MekanismJEI](https://github.com/mekanism/Mekanism/blob/26.3/src/integration/jei/main/java/mekanism/client/recipe_viewer/jei/MekanismJEI.java)
 * @param modId 対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTJeiPlugin(protected val modId: String) : IModPlugin {
    final override fun getPluginUid(): Identifier = modId.toId("jei_plugin")

    /**
     * @since 26.1.12
     */
    protected inline fun <RECIPE_A : Any, reified RECIPE_B : Any, VIEWER : Any> IRecipeRegistration.addRecipes(
        recipeType: IRecipeType<VIEWER>,
        lookup: HTRecipeLookup<RECIPE_A>,
        crossinline transform: (HTRecipeHolder<RECIPE_B>) -> VIEWER,
    ) {
        val viewers: List<VIEWER> = lookup.getAllRecipes(HTPhysicalSideHelper.createLookupContext())
            .mapNotNull { (key: RecipeKey, recipe: RECIPE_A) ->
                when (recipe) {
                    is RECIPE_B -> HTRecipeHolder(key, recipe).let(transform)
                    else -> null
                }
            }
            .toList()
        if (viewers.isEmpty()) return
        this.addRecipes(recipeType, viewers)
    }
}
