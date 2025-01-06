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
import net.minecraft.recipe.ShapelessRecipe
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList

/**
 * [net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder]を改良したクラス
 *
 * レシピIDは最終的に"shapeless/"で前置されます。
 */
class HTShapelessRecipeJsonBuilder private constructor(val output: ItemStack) : CraftingRecipeJsonBuilder {
    companion object {
        /**
         * 指定した[output]を完成品とするビルダーを返します。
         * @throws IllegalStateException [ItemStack.isEmpty]がtrueの場合
         */
        @JvmStatic
        fun create(output: ItemStack): HTShapelessRecipeJsonBuilder = HTShapelessRecipeJsonBuilder(output)

        /**
         * 指定した[output], [count], [components]を完成品とするビルダーを返します。
         * @throws IllegalStateException [ItemStack.isEmpty]がtrueの場合
         */
        @JvmStatic
        fun create(
            output: ItemConvertible,
            count: Int = 1,
            components: ComponentChanges = ComponentChanges.EMPTY,
        ): HTShapelessRecipeJsonBuilder = create(ItemStack(Registries.ITEM.getEntry(output.asItem()), count, components))
    }

    private val inputs: DefaultedList<Ingredient> = DefaultedList.of()
    private val criteriaMap: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private var group: String? = null
    private var category: RecipeCategory = RecipeCategory.MISC

    init {
        check(!output.isEmpty) { "Invalid output found!" }
    }

    fun category(category: RecipeCategory): HTShapelessRecipeJsonBuilder = apply {
        this.category = category
    }

    fun input(prefix: HTTagPrefix, material: HTMaterialKey): HTShapelessRecipeJsonBuilder = input(prefix.createTag(material))

    fun input(content: HTMaterialProvider): HTShapelessRecipeJsonBuilder = input(content.prefixedTagKey)

    fun input(item: ItemConvertible): HTShapelessRecipeJsonBuilder = input(Ingredient.ofItems(item))

    fun input(tagKey: TagKey<Item>): HTShapelessRecipeJsonBuilder = input(Ingredient.fromTag(tagKey))

    fun input(ingredient: Ingredient): HTShapelessRecipeJsonBuilder = apply {
        inputs.add(ingredient)
    }

    fun fluidInput(content: HTFluidContent): HTShapelessRecipeJsonBuilder = fluidInput(content.get())

    fun fluidInput(fluid: Fluid): HTShapelessRecipeJsonBuilder =
        input(DefaultCustomIngredients.components(RagiumAPI.getInstance().createFilledCube(fluid)))

    fun unlockedBy(prefix: HTTagPrefix, material: HTMaterialKey): HTShapelessRecipeJsonBuilder = unlockedBy(prefix.createTag(material))

    fun unlockedBy(content: HTMaterialProvider): HTShapelessRecipeJsonBuilder = unlockedBy(content.prefixedTagKey)

    fun unlockedBy(item: ItemConvertible): HTShapelessRecipeJsonBuilder = criterion("has_the_item", RecipeProvider.conditionsFromItem(item))

    fun unlockedBy(tagKey: TagKey<Item>): HTShapelessRecipeJsonBuilder = criterion("has_the_item", RecipeProvider.conditionsFromTag(tagKey))

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

    override fun criterion(name: String, criterion: AdvancementCriterion<*>): HTShapelessRecipeJsonBuilder = apply {
        criteriaMap[name] = criterion
    }

    override fun group(group: String?): HTShapelessRecipeJsonBuilder = apply {
        this.group = group
    }

    override fun getOutputItem(): Item = output.item

    override fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val fixedId: Identifier = recipeId.withPrefixedPath("shapeless/")
        val builder: Advancement.Builder = exporter.advancementBuilder
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(fixedId))
            .rewards(AdvancementRewards.Builder.recipe(fixedId))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
        criteriaMap.forEach(builder::criterion)
        val recipe = ShapelessRecipe(
            group ?: "",
            CraftingRecipeJsonBuilder.toCraftingCategory(category),
            output,
            inputs,
        )
        exporter.accept(
            fixedId,
            recipe,
            builder.build(fixedId.withPrefixedPath("recipes/misc/")),
        )
    }
}
