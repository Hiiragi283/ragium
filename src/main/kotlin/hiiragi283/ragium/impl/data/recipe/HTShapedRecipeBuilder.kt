package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient

class HTShapedRecipeBuilder(
    private val category: CraftingBookCategory,
    item: HTItemHolderLike,
    count: Int,
    component: DataComponentPatch,
) : HTStackRecipeBuilder<HTShapedRecipeBuilder>("shaped", item, count, component) {
    companion object {
        @JvmStatic
        fun building(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapedRecipeBuilder =
            HTShapedRecipeBuilder(CraftingBookCategory.BUILDING, HTItemHolderLike.fromItem(item), count, component)

        @JvmStatic
        fun redstone(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapedRecipeBuilder =
            HTShapedRecipeBuilder(CraftingBookCategory.REDSTONE, HTItemHolderLike.fromItem(item), count, component)

        @JvmStatic
        fun equipment(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapedRecipeBuilder =
            HTShapedRecipeBuilder(CraftingBookCategory.EQUIPMENT, HTItemHolderLike.fromItem(item), count, component)

        @JvmStatic
        fun misc(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapedRecipeBuilder =
            HTShapedRecipeBuilder(CraftingBookCategory.MISC, HTItemHolderLike.fromItem(item), count, component)
    }

    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    fun define(symbol: Char, variant: HTMaterialVariant.ItemTag, material: HTMaterialType): HTShapedRecipeBuilder =
        define(symbol, variant.itemTagKey(material))

    fun define(symbol: Char, tagKey: TagKey<Item>): HTShapedRecipeBuilder = define(symbol, Ingredient.of(tagKey))

    fun define(symbol: Char, item: ItemLike): HTShapedRecipeBuilder = define(symbol, Ingredient.of(item))

    fun define(symbol: Char, ingredient: ICustomIngredient): HTShapedRecipeBuilder = define(symbol, ingredient.toVanilla())

    fun define(symbol: Char, ingredient: Ingredient): HTShapedRecipeBuilder = apply {
        check(symbol !in symbols) { "Symbol '$symbol' is already used!" }
        check(symbol != ' ') { "Symbol ' ' is not allowed!" }
        symbols[symbol] = ingredient
    }

    private val patterns: MutableList<String> = mutableListOf()

    fun pattern(vararg pattern: String): HTShapedRecipeBuilder = apply {
        check(pattern.map(String::length).toSet().size == 1) { "Each pattern must be the same length!" }
        patterns.addAll(pattern)
    }

    fun storage4(): HTShapedRecipeBuilder = pattern("AA", "AA")

    fun storage9(): HTShapedRecipeBuilder = pattern("AAA", "AAA", "AAA")

    fun hollow(): HTShapedRecipeBuilder = pattern("AAA", "A A", "AAA")

    fun hollow4(): HTShapedRecipeBuilder = pattern(" A ", "ABA", " A ")

    fun hollow8(): HTShapedRecipeBuilder = pattern("AAA", "ABA", "AAA")

    fun cross4(): HTShapedRecipeBuilder = pattern(" A ", "BCB", " A ")

    fun cross8(): HTShapedRecipeBuilder = pattern("ABA", "BCB", "ABA")

    fun crossLayered(): HTShapedRecipeBuilder = pattern("ABA", "CDC", "ABA")

    fun casing(): HTShapedRecipeBuilder = pattern("AAA", "ABA", "CCC")

    fun mosaic4(): HTShapedRecipeBuilder = pattern("AB", "BA")

    //    RecipeBuilder    //

    private var groupName: String? = null

    override fun group(groupName: String?): HTShapedRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun createRecipe(output: ItemStack): ShapedRecipe = ShapedRecipe(
        groupName ?: "",
        category,
        ShapedRecipePattern.of(symbols, patterns),
        output,
        true,
    )
}
