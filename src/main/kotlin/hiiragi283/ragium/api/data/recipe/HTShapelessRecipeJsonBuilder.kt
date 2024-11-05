package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.RecipeProvider
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

class HTShapelessRecipeJsonBuilder private constructor(val output: ItemStack) : CraftingRecipeJsonBuilder {
    companion object {
        @JvmStatic
        fun create(output: ItemStack): HTShapelessRecipeJsonBuilder = HTShapelessRecipeJsonBuilder(output)

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

    init {
        check(!output.isEmpty) { "Invalid output found!" }
    }

    fun input(prefix: HTTagPrefix, material: HTMaterialKey): HTShapelessRecipeJsonBuilder = input(prefix.createTag(material))

    fun input(content: HTContent.Material<*>): HTShapelessRecipeJsonBuilder = input(content.prefixedTagKey)

    fun input(item: ItemConvertible): HTShapelessRecipeJsonBuilder = input(Ingredient.ofItems(item))

    fun input(tagKey: TagKey<Item>): HTShapelessRecipeJsonBuilder = input(Ingredient.fromTag(tagKey))

    fun input(ingredient: Ingredient): HTShapelessRecipeJsonBuilder = apply {
        inputs.add(ingredient)
    }

    fun unlockedBy(prefix: HTTagPrefix, material: HTMaterialKey): HTShapelessRecipeJsonBuilder = unlockedBy(prefix.createTag(material))

    fun unlockedBy(content: HTContent.Material<*>): HTShapelessRecipeJsonBuilder = unlockedBy(content.prefixedTagKey)

    fun unlockedBy(item: ItemConvertible): HTShapelessRecipeJsonBuilder = criterion("has_the_item", RecipeProvider.conditionsFromItem(item))

    fun unlockedBy(tagKey: TagKey<Item>): HTShapelessRecipeJsonBuilder = criterion("has_the_item", RecipeProvider.conditionsFromTag(tagKey))

    fun offerPrefix(exporter: RecipeExporter, prefix: String) {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(outputItem).withPrefixedPath(prefix))
    }

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
            CraftingRecipeJsonBuilder.toCraftingCategory(RecipeCategory.MISC),
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
