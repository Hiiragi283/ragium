package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.ConditionalExporter
import hiiragi283.lib.data.ExporterDataProvider
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.resource.toId
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.WithConditions
import java.util.concurrent.CompletableFuture

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
typealias HTRecipeExporter = ConditionalExporter<Recipe<*>>

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
    override fun createExporter(map: MutableMap<RecipeKey, WithConditions<Recipe<*>>>): HTRecipeExporter =
        HTRecipeExporter { id: RecipeKey, recipe: Recipe<*>, conditions: List<ICondition> ->
            val fixedId: RecipeKey = id.let(::modifyId)
            check(map.put(fixedId, WithConditions(conditions, recipe)) == null) {
                "Duplicate recipe ${fixedId.identifier()}"
            }
        }

    protected fun modifyId(key: RecipeKey): RecipeKey = RecipeKey(key.identifier().let(::modifyId))

    /**
     * 受け取った[id]を[exporter]内で変換します。
     */
    protected open fun modifyId(id: Identifier): Identifier = modId.toId(id.path)

    //    Extensions    //

    // Recipe Builder
    protected inline fun netheriteUpgrade(builderAction: HTSmithingRecipeBuilder.() -> Unit): HTSmithingRecipeBuilder =
        HTSmithingRecipeBuilder.create {
            template { items { +Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE } }
            addition { +holderSet(Tags.Items.INGOTS_NETHERITE) }
            builderAction()
        }

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
