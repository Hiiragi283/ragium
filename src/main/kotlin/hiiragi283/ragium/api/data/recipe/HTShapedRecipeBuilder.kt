package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
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

class HTShapedRecipeBuilder(private val output: ItemStack, private val category: CraftingBookCategory) : RecipeBuilder {
    constructor(item: ItemLike, count: Int = 1, category: CraftingBookCategory = CraftingBookCategory.MISC) : this(
        ItemStack(item, count),
        category,
    )

    private val symbols: MutableMap<Char, Ingredient> = mutableMapOf()

    fun define(symbol: Char, prefix: HTTagPrefix, key: HTMaterialKey): HTShapedRecipeBuilder = define(symbol, prefix.createTag(key))

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

    fun hollow(): HTShapedRecipeBuilder = pattern("AAA", "ABA", "AAA")

    //    RecipeBuilder    //

    private var groupName: String? = null

    @Deprecated("Advancements not supported")
    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = throw UnsupportedOperationException()

    override fun group(groupName: String?): HTShapedRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun getResult(): Item = output.item

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
