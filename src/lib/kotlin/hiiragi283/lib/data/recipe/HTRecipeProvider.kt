package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTComparators
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.ExporterDataProvider
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
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
