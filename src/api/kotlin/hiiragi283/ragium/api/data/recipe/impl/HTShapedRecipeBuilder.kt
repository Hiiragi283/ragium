package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient

class HTShapedRecipeBuilder(private val output: ItemStack, private val category: CraftingBookCategory = CraftingBookCategory.MISC) :
    HTRecipeBuilder {
    constructor(item: ItemLike, count: Int = 1, category: CraftingBookCategory = CraftingBookCategory.MISC) : this(
        ItemStack(item, count),
        category,
    )

    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    fun define(symbol: Char, variant: HTMaterialVariant, material: HTMaterialType): HTShapedRecipeBuilder =
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

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    private var groupName: String? = null

    override fun group(groupName: String?): HTShapedRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("shaped/"),
            ShapedRecipe(
                groupName ?: "",
                category,
                ShapedRecipePattern.of(symbols, patterns),
                output,
                true,
            ),
            null,
        )
    }
}
