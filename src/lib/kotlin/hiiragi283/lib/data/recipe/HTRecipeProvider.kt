package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.ExporterDataProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import java.util.concurrent.CompletableFuture

/**
 * Hiiragi Seriesで使用される，レシピ向けの[ExporterDataProvider]の拡張クラスです。
 * 参照 : [Minecraft - RecipeProvider][net.minecraft.data.recipes.RecipeProvider]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTRecipeProvider(
    packOutput: PackOutput,
    future: CompletableFuture<HolderLookup.Provider>,
    modId: String
) : ExporterDataProvider<Recipe<*>>(packOutput, future, Registries.RECIPE, modId, Recipe.CONDITIONAL_CODEC) {
    //    Integration    //

    abstract class Integration(
        packOutput: PackOutput,
        future: CompletableFuture<HolderLookup.Provider>,
        modId: String,
        integrationModId: String
    ) : HTRecipeProvider(packOutput, future, modId) {
        val condition = ModLoadedCondition(integrationModId)
        private val builtInIds: Set<String> = HTConstants.getBuiltInIdSet(modId)

        final override fun modifyId(id: Identifier): Identifier {
            val namespace: String = id.namespace
            return if (namespace in builtInIds) {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], modId, path[1])
            } else {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], namespace, path[1])
            }
        }
    }
}
