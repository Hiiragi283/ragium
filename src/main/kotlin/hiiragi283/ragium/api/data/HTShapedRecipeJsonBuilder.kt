package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.withPrefix
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.HolderGetter
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.ItemLike

/**
 * [ShapedRecipeBuilder]を改良したクラス
 *
 * レシピIDは最終的に"shaped/"で前置されます。
 */
class HTShapedRecipeJsonBuilder private constructor(
    val getter: HolderGetter<Item>,
    val stack: ItemStack,
) : RecipeBuilder {
    companion object {
        /**
         * 指定した[getter]と[output]を完成品とするビルダーを返します。
         * @throws IllegalStateException [ItemStack.isEmpty]がtrueの場合
         */
        @JvmStatic
        fun create(
            getter: HolderGetter<Item>,
            output: ItemStack,
        ): HTShapedRecipeJsonBuilder = HTShapedRecipeJsonBuilder(getter, output)

        /**
         * 指定した[output], [count], [componentPatch]を完成品とするビルダーを返します。
         * @throws IllegalStateException [ItemStack.isEmpty]がtrueの場合
         */
        @Suppress("DEPRECATION")
        @JvmStatic
        fun create(
            getter: HolderGetter<Item>,
            output: ItemLike,
            count: Int = 1,
            componentPatch: DataComponentPatch = DataComponentPatch.EMPTY,
        ): HTShapedRecipeJsonBuilder = create(getter, ItemStack(output.asItem().builtInRegistryHolder(), count, componentPatch))
    }

    private lateinit var patterns: Array<out String>
    private val inputMap: MutableMap<Char, Ingredient> = mutableMapOf()
    private val criteriaMap: MutableMap<String, Criterion<*>> = mutableMapOf()
    private var group: String? = null
    private var category: RecipeCategory = RecipeCategory.MISC

    init {
        check(!stack.isEmpty) { "Invalid output found!" }
    }

    fun category(category: RecipeCategory): HTShapedRecipeJsonBuilder =
        apply {
            this.category = category
        }

    fun input(
        char: Char,
        prefix: HTTagPrefix,
        material: HTMaterialKey,
    ): HTShapedRecipeJsonBuilder = input(char, prefix.createTag(material))

    fun input(
        char: Char,
        content: HTMaterialProvider,
    ): HTShapedRecipeJsonBuilder = input(char, content.prefixedTagKey)

    fun input(
        char: Char,
        item: ItemLike,
    ): HTShapedRecipeJsonBuilder = input(char, Ingredient.of(item))

    fun input(
        char: Char,
        tagKey: TagKey<Item>,
    ): HTShapedRecipeJsonBuilder = input(char, Ingredient.of(getter.getOrThrow(tagKey)))

    fun input(
        char: Char,
        ingredient: Ingredient,
    ): HTShapedRecipeJsonBuilder =
        apply {
            when {
                inputMap.contains(char) -> throw IllegalArgumentException("Symbol '$char' is already defined!")
                char == ' ' -> throw java.lang.IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined!")
                else -> inputMap[char] = ingredient
            }
        }

    fun patterns(patterns: List<String>): HTShapedRecipeJsonBuilder = patterns(*patterns.toTypedArray())

    fun patterns(vararg patterns: String): HTShapedRecipeJsonBuilder =
        apply {
            when {
                patterns
                    .map(String::length)
                    .toSet()
                    .size > 1 -> throw IllegalArgumentException("Pattern must be the same width on every line!")

                else -> this.patterns = patterns
            }
        }

    fun pattern2x2(): HTShapedRecipeJsonBuilder =
        patterns(
            "AA",
            "AA",
        )

    fun pattern3x3(): HTShapedRecipeJsonBuilder =
        patterns(
            "AAA",
            "AAA",
            "AAA",
        )

    fun hollowPattern(): HTShapedRecipeJsonBuilder =
        patterns(
            "AAA",
            "A A",
            "AAA",
        )

    fun wrapPattern8(): HTShapedRecipeJsonBuilder =
        patterns(
            "AAA",
            "ABA",
            "AAA",
        )

    fun slabPattern(): HTShapedRecipeJsonBuilder = patterns("AAA")

    fun stairPattern(): HTShapedRecipeJsonBuilder =
        patterns(
            "A  ",
            "AA ",
            "AAA",
        )

    fun unlockedBy(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
    ): HTShapedRecipeJsonBuilder = unlockedBy(prefix.createTag(material))

    fun unlockedBy(content: HTMaterialProvider): HTShapedRecipeJsonBuilder = unlockedBy(content.prefixedTagKey)

    fun unlockedBy(item: ItemLike): HTShapedRecipeJsonBuilder = unlockedBy("has_the_item", TODO())

    fun unlockedBy(tagKey: TagKey<Item>): HTShapedRecipeJsonBuilder = unlockedBy("has_the_item", TODO())

    /**
     * 完成品のアイテムIDを[prefix]で前置したものをレシピIDとして使用します。
     */
    fun savePrefix(
        exporter: RecipeOutput,
        prefix: String,
    ) {
        save(
            exporter,
            ResourceKey.create(Registries.RECIPE, RecipeBuilder.getDefaultRecipeId(result).withPrefix(prefix)),
        )
    }

    /**
     * 完成品のアイテムIDを[suffix]で後置したものをレシピIDとして使用します。
     */
    fun saveSuffix(
        exporter: RecipeOutput,
        suffix: String,
    ) {
        save(
            exporter,
            ResourceKey.create(Registries.RECIPE, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix)),
        )
    }

    //    CraftingRecipeJsonBuilder    //

    override fun unlockedBy(
        name: String,
        criterion: Criterion<*>,
    ): HTShapedRecipeJsonBuilder =
        apply {
            criteriaMap[name] = criterion
        }

    override fun group(group: String?): HTShapedRecipeJsonBuilder =
        apply {
            this.group = group
        }

    override fun getResult(): Item = stack.item

    override fun save(
        output: RecipeOutput,
        resourceKey: ResourceKey<Recipe<*>>,
    ) {
        val fixedKey: ResourceKey<Recipe<*>> = resourceKey.withPrefix("shaped/")
        val rawRecipe: ShapedRecipePattern = ShapedRecipePattern.of(inputMap, patterns.toList())
        val builder: Advancement.Builder =
            output
                .advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(fixedKey))
                .rewards(AdvancementRewards.Builder.recipe(fixedKey))
                .requirements(AdvancementRequirements.Strategy.OR)
        criteriaMap.forEach(builder::addCriterion)
        val recipe =
            ShapedRecipe(
                group ?: "",
                RecipeBuilder.determineBookCategory(category),
                rawRecipe,
                this.stack,
                true,
            )
        output.accept(
            fixedKey,
            recipe,
            builder.build(fixedKey.location().withPrefix("recipes/${category.folderName}/")),
        )
    }
}
