package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
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

class HTShapedRecipeBuilder(private val output: ItemStack, private val category: CraftingBookCategory) : HTRecipeBuilder<ShapedRecipe> {
    constructor(item: ItemLike, count: Int = 1, category: CraftingBookCategory = CraftingBookCategory.MISC) : this(
        ItemStack(item, count),
        category,
    )

    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    fun define(symbol: Char, prefix: HTTagPrefix, key: HTMaterialKey): HTShapedRecipeBuilder = define(symbol, prefix.createItemTag(key))

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

    fun storage9(): HTShapedRecipeBuilder = pattern("AAA", "AAA", "AAA")

    fun hollow(): HTShapedRecipeBuilder = pattern("AAA", "A A", "AAA")

    fun hollow4(): HTShapedRecipeBuilder = pattern(" A ", "ABA", " A ")

    fun hollow8(): HTShapedRecipeBuilder = pattern("AAA", "ABA", "AAA")

    fun cross4(): HTShapedRecipeBuilder = pattern(" A ", "BCB", " A ")

    fun cross8(): HTShapedRecipeBuilder = pattern("ABA", "BCB", "ABA")

    fun casing(): HTShapedRecipeBuilder = pattern("AAA", "ABA", "CCC")

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    private var groupName: String? = null

    override fun group(groupName: String?): HTShapedRecipeBuilder = apply {
        this.groupName = groupName
    }

    override val prefix: String = "shaped"

    override fun createRecipe(): ShapedRecipe = ShapedRecipe(
        groupName ?: "",
        category,
        ShapedRecipePattern.of(symbols, patterns),
        output,
        true,
    )
}
