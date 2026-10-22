package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTComparators
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.nelOf
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.HTSingleRegistryBootstrap
import hiiragi283.lib.recipe.ingredient.HTMaterialTagsIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe

/**
 * Hiiragi Seriesで使用される，レシピ向けの[HTSingleRegistryBootstrap]の拡張クラスです。
 * 参照 : [Minecraft - RecipeProvider][net.minecraft.data.recipes.RecipeProvider]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTRecipeProvider(modId: String) : HTSingleRegistryBootstrap<Recipe<*>>(modId) {
    //    Extension    //

    /**
     * @since 26.1.7
     */
    protected fun materialTags(tags: Nel<TagKey<Item>>): HTMaterialTagsIngredient = HTMaterialTagsIngredient(tags)

    /**
     * @since 26.1.7
     */
    protected fun materialTags(prefixes: Nel<HTTagPrefix>, materials: Nel<HTMaterialLike>): HTMaterialTagsIngredient =
        materialTags(
            prefixes
                .flatMap { prefix: HTTagPrefix -> materials.map(prefix::itemTagKey) }
                .toSortedSet(HTComparators.TAG_KEY)
                .toNel()
        )

    /**
     * @since 26.1.7
     */
    protected fun materialTags(prefixes: Nel<HTTagPrefix>, material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(prefixes.map { it.itemTagKey(material) })

    /**
     * @since 26.1.7
     */
    protected fun materialTags(prefix: HTTagPrefix, materials: Nel<HTMaterialLike>): HTMaterialTagsIngredient =
        materialTags(materials.map(prefix::itemTagKey))

    /**
     * @since 26.1.7
     */
    protected fun dustOrGem(material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(nelOf(CommonTagPrefixes.DUST, CommonTagPrefixes.GEM), material)

    /**
     * @since 26.1.7
     */
    protected fun dustOrIngot(material: HTMaterialLike): HTMaterialTagsIngredient =
        materialTags(nelOf(CommonTagPrefixes.DUST, CommonTagPrefixes.INGOT), material)
}
