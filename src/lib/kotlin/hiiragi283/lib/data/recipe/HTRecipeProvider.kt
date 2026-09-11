package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.ExporterDataProvider
import hiiragi283.lib.item.component.HTToolCollection
import hiiragi283.lib.item.component.HTToolType
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.Tags
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
    //    Extensions    //

    // Recipe Builder

    /**
     * @since 26.1.4
     */
    protected fun registerTools(tools: HTToolCollection<Holder<Item>>, material: ToolMaterial) {
        fun registerTool(toolType: HTToolType, patterns: Iterable<String>) {
            HTShapedRecipeBuilder.create {
                pattern(patterns)
                define('A') { +holderSet(material.repairItems) }
                define('B') { +holderSet(Tags.Items.RODS_WOODEN) }
                result { +tools[toolType] }
            }.save(exporter)
        }

        registerTool(HTToolType.SWORD, listOf("B", "A", "A"))
        registerTool(HTToolType.SHOVEL, listOf("B", "B", "A"))
        registerTool(HTToolType.PICKAXE, listOf(" B ", " B ", "AAA"))
        registerTool(HTToolType.AXE, listOf("B ", "BA", "AA"))
        registerTool(HTToolType.HOE, listOf("B ", "B ", "AA"))
    }

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
