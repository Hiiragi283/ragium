package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient

class HTShapedRecipeBuilder(stack: ImmutableItemStack) : HTStackRecipeBuilder<HTShapedRecipeBuilder>("shaped", stack) {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTShapedRecipeBuilder = HTShapedRecipeBuilder(ImmutableItemStack.of(item, count))

        @JvmStatic
        fun cross8Mirrored(
            recipeOutput: RecipeOutput,
            item: ItemLike,
            count: Int = 1,
            suffix: String = "",
            builderAction: HTShapedRecipeBuilder.() -> Unit,
        ) {
            create(item, count)
                .pattern("ABA", "BCB", "ABA")
                .apply(builderAction)
                .setGroup()
                .saveSuffixed(recipeOutput, suffix)
            create(item, count)
                .pattern("BAB", "ACA", "BAB")
                .apply(builderAction)
                .setGroup()
                .saveSuffixed(recipeOutput, "_alt$suffix")
        }

        @JvmStatic
        fun crossLayeredMirrored(
            recipeOutput: RecipeOutput,
            item: ItemLike,
            count: Int = 1,
            suffix: String = "",
            builderAction: HTShapedRecipeBuilder.() -> Unit,
        ) {
            create(item, count)
                .pattern("ABA", "CDC", "ABA")
                .apply(builderAction)
                .setGroup()
                .saveSuffixed(recipeOutput, suffix)
            create(item, count)
                .pattern("ACA", "BDB", "ACA")
                .apply(builderAction)
                .setGroup()
                .saveSuffixed(recipeOutput, "_alt$suffix")
        }
    }

    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    fun define(symbol: Char, prefix: HTPrefixLike, material: HTMaterialLike): HTShapedRecipeBuilder =
        define(symbol, prefix.itemTagKey(material))

    fun define(symbol: Char, tagKey: TagKey<Item>): HTShapedRecipeBuilder = define(symbol, Ingredient.of(tagKey))

    fun define(symbol: Char, item: ItemLike): HTShapedRecipeBuilder = define(symbol, Ingredient.of(item))

    fun define(symbol: Char, ingredient: ICustomIngredient): HTShapedRecipeBuilder = define(symbol, ingredient.toVanilla())

    fun define(symbol: Char, ingredient: Ingredient): HTShapedRecipeBuilder = apply {
        check(symbol !in symbols) { "Symbol '$symbol' is already used!" }
        check(symbol != ' ') { "Symbol ' ' is not allowed!" }
        symbols[symbol] = ingredient
    }

    private val patterns: MutableList<String> = mutableListOf()

    fun pattern(vararg pattern: String): HTShapedRecipeBuilder = pattern(pattern.toList())

    fun pattern(pattern: Iterable<String>): HTShapedRecipeBuilder = apply {
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

    fun mosaic4(): HTShapedRecipeBuilder = pattern("AB", "BA")

    fun mosaic9(): HTShapedRecipeBuilder = pattern("ABA", "BAB", "ABA")

    //    RecipeBuilder    //

    private var group: String? = null
    private var category: CraftingBookCategory = CraftingBookCategory.MISC

    fun setGroup(): HTShapedRecipeBuilder = setGroup(getPrimalId().toDebugFileName())

    fun setGroup(group: String?): HTShapedRecipeBuilder = apply {
        this.group = group
    }

    fun setCategory(category: CraftingBookCategory): HTShapedRecipeBuilder = apply {
        this.category = category
    }

    override fun createRecipe(output: ItemStack): ShapedRecipe = ShapedRecipe(
        group ?: "",
        category,
        ShapedRecipePattern.of(symbols, patterns),
        output,
        true,
    )
}
