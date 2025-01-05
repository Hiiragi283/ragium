package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RawShapedRecipe
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

/**
 * [net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder]を改良したクラス
 *
 * レシピIDは最終的に"shaped/"で前置されます。
 */
class HTShapedRecipeJsonBuilder private constructor(val output: ItemStack) : CraftingRecipeJsonBuilder {
    companion object {
        /**
         * 指定した[output]を完成品とするビルダーを返します。
         * @throws IllegalStateException [ItemStack.isEmpty]がtrueの場合
         */
        @JvmStatic
        fun create(output: ItemStack): HTShapedRecipeJsonBuilder = HTShapedRecipeJsonBuilder(output)

        /**
         * 指定した[output], [count], [components]を完成品とするビルダーを返します。
         * @throws IllegalStateException [ItemStack.isEmpty]がtrueの場合
         */
        @Suppress("DEPRECATION")
        @JvmStatic
        fun create(
            output: ItemConvertible,
            count: Int = 1,
            components: ComponentChanges = ComponentChanges.EMPTY,
        ): HTShapedRecipeJsonBuilder = create(ItemStack(output.asItem().registryEntry, count, components))
    }

    private lateinit var patterns: Array<out String>
    private val inputMap: MutableMap<Char, Ingredient> = mutableMapOf()
    private val criteriaMap: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private var group: String? = null
    private var category: RecipeCategory = RecipeCategory.MISC

    init {
        check(!output.isEmpty) { "Invalid output found!" }
    }

    fun category(category: RecipeCategory): HTShapedRecipeJsonBuilder = apply {
        this.category = category
    }

    fun input(char: Char, prefix: HTTagPrefix, material: HTMaterialKey): HTShapedRecipeJsonBuilder = input(char, prefix.createTag(material))

    fun input(char: Char, content: HTMaterialProvider): HTShapedRecipeJsonBuilder = input(char, content.prefixedTagKey)

    fun input(char: Char, item: ItemConvertible): HTShapedRecipeJsonBuilder = input(char, Ingredient.ofItems(item))

    fun input(char: Char, tagKey: TagKey<Item>): HTShapedRecipeJsonBuilder = input(char, Ingredient.fromTag(tagKey))

    fun input(char: Char, ingredient: Ingredient): HTShapedRecipeJsonBuilder = apply {
        when {
            inputMap.contains(char) -> throw IllegalArgumentException("Symbol '$char' is already defined!")
            char == ' ' -> throw java.lang.IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined!")
            else -> inputMap[char] = ingredient
        }
    }

    fun fluidInput(char: Char, content: HTFluidContent): HTShapedRecipeJsonBuilder = fluidInput(char, content.get())

    fun fluidInput(char: Char, fluid: Fluid): HTShapedRecipeJsonBuilder = input(
        char,
        DefaultCustomIngredients.components(RagiumAPI.getInstance().createFilledCube(fluid)),
    )

    fun patterns(patterns: List<String>): HTShapedRecipeJsonBuilder = patterns(*patterns.toTypedArray())

    fun patterns(vararg patterns: String): HTShapedRecipeJsonBuilder = apply {
        when {
            patterns
                .map(String::length)
                .toSet()
                .size > 1 -> throw IllegalArgumentException("Pattern must be the same width on every line!")

            else -> this.patterns = patterns
        }
    }

    fun pattern2x2(): HTShapedRecipeJsonBuilder = patterns(
        "AA",
        "AA",
    )

    fun pattern3x3(): HTShapedRecipeJsonBuilder = patterns(
        "AAA",
        "AAA",
        "AAA",
    )

    fun hollowPattern(): HTShapedRecipeJsonBuilder = patterns(
        "AAA",
        "A A",
        "AAA",
    )

    fun wrapPattern8(): HTShapedRecipeJsonBuilder = patterns(
        "AAA",
        "ABA",
        "AAA",
    )

    fun slabPattern(): HTShapedRecipeJsonBuilder = patterns("AAA")

    fun stairPattern(): HTShapedRecipeJsonBuilder = patterns(
        "A  ",
        "AA ",
        "AAA",
    )

    fun unlockedBy(prefix: HTTagPrefix, material: HTMaterialKey): HTShapedRecipeJsonBuilder = unlockedBy(prefix.createTag(material))

    fun unlockedBy(content: HTMaterialProvider): HTShapedRecipeJsonBuilder = unlockedBy(content.prefixedTagKey)

    fun unlockedBy(item: ItemConvertible): HTShapedRecipeJsonBuilder = criterion("has_the_item", RecipeProvider.conditionsFromItem(item))

    fun unlockedBy(tagKey: TagKey<Item>): HTShapedRecipeJsonBuilder = criterion("has_the_item", RecipeProvider.conditionsFromTag(tagKey))

    /**
     * 完成品のアイテムIDを[prefix]で前置したものをレシピIDとして使用します。
     */
    fun offerPrefix(exporter: RecipeExporter, prefix: String) {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(outputItem).withPrefixedPath(prefix))
    }

    /**
     * 完成品のアイテムIDを[suffix]で後置したものをレシピIDとして使用します。
     */
    fun offerSuffix(exporter: RecipeExporter, suffix: String) {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(outputItem).withSuffixedPath(suffix))
    }

    //    CraftingRecipeJsonBuilder    //

    override fun criterion(name: String, criterion: AdvancementCriterion<*>): HTShapedRecipeJsonBuilder = apply {
        criteriaMap[name] = criterion
    }

    override fun group(group: String?): HTShapedRecipeJsonBuilder = apply {
        this.group = group
    }

    override fun getOutputItem(): Item = output.item

    override fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val fixedId: Identifier = recipeId.withPrefixedPath("shaped/")
        val rawRecipe: RawShapedRecipe = RawShapedRecipe.create(inputMap, patterns.toList())
        val builder: Advancement.Builder = exporter.advancementBuilder
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(fixedId))
            .rewards(AdvancementRewards.Builder.recipe(fixedId))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
        criteriaMap.forEach(builder::criterion)
        val recipe = ShapedRecipe(
            group ?: "",
            CraftingRecipeJsonBuilder.toCraftingCategory(category),
            rawRecipe,
            output,
            true,
        )
        exporter.accept(
            fixedId,
            recipe,
            builder.build(fixedId.withPrefixedPath("recipes/misc/")),
        )
    }
}
