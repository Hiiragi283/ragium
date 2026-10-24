package hiiragi283.lib.integration.jei

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTVanillaRecipeLookup
import hiiragi283.lib.resource.toId
import mezz.jei.api.IModPlugin
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

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
    protected fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, VIEWER : Any> IRecipeRegistration.addRecipes(
        recipeType: IRecipeType<VIEWER>,
        recipeType1: RecipeType<RECIPE>,
        transform: (HTRecipeHolder<RECIPE>) -> VIEWER,
    ) {
        val viewers: List<VIEWER> = HTVanillaRecipeLookup(recipeType1).getAllRecipes(HTPhysicalSideHelper.createLookupContext())
            .map(transform)
            .toList()
        if (viewers.isEmpty()) return
        this.addRecipes(recipeType, viewers)
    }
}
