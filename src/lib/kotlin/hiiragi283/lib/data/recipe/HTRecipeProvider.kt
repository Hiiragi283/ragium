package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTComparators
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.ExporterDataProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
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
    //    Extension    //

    /**
     * @since 26.1.7
     */
    fun materialTags(tags: Nel<TagKey<Item>>): HTMaterialTagsIngredient = HTMaterialTagsIngredient(tags)

    /**
     * @since 26.1.7
     */
    fun materialTags(prefixes: Nel<HTTagPrefix>, materials: Nel<HTMaterialLike>): HTMaterialTagsIngredient =
        materialTags(
            prefixes
                .flatMap { prefix: HTTagPrefix -> materials.map(prefix::itemTagKey) }
                .toSortedSet(HTComparators.TAG_KEY)
                .toNel()
        )

    /**
     * @since 26.1.7
     */
    fun materialTags(prefixes: Nel<HTTagPrefix>, material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(prefixes.map { it.itemTagKey(material) })

    /**
     * @since 26.1.7
     */
    fun materialTags(prefix: HTTagPrefix, materials: Nel<HTMaterialLike>): HTMaterialTagsIngredient =
        materialTags(materials.map(prefix::itemTagKey))

    /**
     * @since 26.1.7
     */
    fun dustOrGem(material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(nelOf(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM), material)

    /**
     * @since 26.1.7
     */
    fun dustOrIngot(material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(nelOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT), material)

    /**
     * @since 26.1.7
     */
    fun registerSlabRecipes(slab: HTBasicDeferredBlockAndItem<SlabBlock>, base: Ingredient) {
        // Shaped
        VanillaRecipeBuilders.shaped {
            +"AAA"
            define('A') { +base }
            result {
                +slab
                count = 6
            }
            group = slab.idOrThrow.path
        }.save(exporter)
        // Stonecutting
        VanillaRecipeBuilders.stonecutting {
            ingredient = base
            result {
                +slab
                count = 2
            }
            group = slab.idOrThrow.path
        }.save(exporter)
    }

    /**
     * @since 26.1.7
     */
    fun registerStairsRecipes(stairs: HTBasicDeferredBlockAndItem<StairBlock>, base: Ingredient) {
        // Shaped
        VanillaRecipeBuilders.shaped {
            +"A  "
            +"AA "
            +"AAA"
            define('A') { +base }
            result {
                +stairs
                count = 4
            }
            group = stairs.idOrThrow.path
        }.save(exporter)
        // Stonecutting
        VanillaRecipeBuilders.stonecutting {
            ingredient = base
            result { +stairs }
            group = stairs.idOrThrow.path
        }.save(exporter)
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
            return if (namespace !in builtInIds) {
                val path: List<String> = id.path.split("/", limit = 2)
                id(path[0], namespace, path[1])
            } else {
                super.modifyId(id)
            }
        }
    }
}
