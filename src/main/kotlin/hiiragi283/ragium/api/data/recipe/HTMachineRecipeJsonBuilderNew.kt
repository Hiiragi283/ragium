package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTIngredientNew
import hiiragi283.ragium.api.recipe.HTRecipeResultNew
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeNew
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
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
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTMachineRecipeJsonBuilderNew private constructor(
    private val type: HTMachineType,
    private val tier: HTMachineTier = HTMachineTier.PRIMITIVE,
) {
    companion object {
        @JvmStatic
        fun create(type: HTMachineConvertible, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilderNew = type
            .asProcessor()
            ?.let { HTMachineRecipeJsonBuilderNew(it, minTier) }
            ?: throw IllegalStateException("Machine Type;  ${type.asMachine().id} must be Processor!")
    }

    private val itemInputs: MutableList<HTIngredientNew<Item, ItemVariant>> = mutableListOf()
    private val fluidInputs: MutableList<HTIngredientNew<Fluid, FluidVariant>> = mutableListOf()
    private val itemOutputs: MutableList<HTRecipeResultNew<Item, ItemVariant>> = mutableListOf()
    private val fluidOutputs: MutableList<HTRecipeResultNew<Fluid, FluidVariant>> = mutableListOf()
    private var catalyst: HTIngredientNew<Item, ItemVariant> = HTIngredientNew.EMPTY_ITEM
    private val criteria: MutableMap<String, AdvancementCriterion<*>> = mutableMapOf()
    private var suffixCache: Int = 0

    //    Input    //

    fun input(item: ItemConvertible, amount: Long = 1): HTMachineRecipeJsonBuilderNew = apply {
        itemInputs.add(HTIngredientNew.ofItem(item, amount))
        hasInput(item, suffixCache.toString())
        suffixCache++
    }

    @JvmName("inputItemTag")
    fun input(tagKey: TagKey<Item>, amount: Long = 1): HTMachineRecipeJsonBuilderNew = apply {
        itemInputs.add(HTIngredientNew.ofItem(tagKey, amount))
        hasInput(tagKey, suffixCache.toString())
        suffixCache++
    }

    fun input(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew = apply {
        fluidInputs.add(HTIngredientNew.ofFluid(fluid, amount))
    }

    @JvmName("inputFluidTag")
    fun input(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilderNew = apply {
        fluidInputs.add(HTIngredientNew.ofFluid(tagKey, amount))
    }

    //    Output    //

    fun output(
        item: ItemConvertible,
        amount: Long = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilderNew = apply {
        itemOutputs.add(HTRecipeResultNew.ofItem(item, amount, components))
    }

    fun output(
        fluid: Fluid,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilderNew = apply {
        fluidOutputs.add(HTRecipeResultNew.ofFluid(fluid, amount, components))
    }

    //    Catalyst    //

    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilderNew = apply {
        catalyst = HTIngredientNew.ofItem(item)
    }

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilderNew = apply {
        catalyst = HTIngredientNew.ofItem(tagKey)
    }

    //    Criterion    //

    fun criterion(name: String, criterion: AdvancementCriterion<*>): HTMachineRecipeJsonBuilderNew = apply {
        criteria[name] = criterion
    }

    fun hasInput(item: ItemConvertible, suffix: String = ""): HTMachineRecipeJsonBuilderNew =
        criterion("has_input$suffix", RecipeProvider.conditionsFromItem(item))

    fun hasInput(tagKey: TagKey<Item>, suffix: String = ""): HTMachineRecipeJsonBuilderNew =
        criterion("has_input$suffix", RecipeProvider.conditionsFromTag(tagKey))

    fun offerSuffix(exporter: RecipeExporter, suffix: String = "") {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(itemOutputs[0].firstObj).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier = CraftingRecipeJsonBuilder.getItemId(itemOutputs[0].firstObj)) {
        val prefix = "${type.id.path}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        exporter.accept(
            prefixedId,
            HTMachineRecipeNew(
                HTMachineDefinition(type, tier),
                catalyst,
                itemInputs,
                fluidInputs,
                itemOutputs,
                fluidOutputs,
            ),
            exporter.advancementBuilder
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(prefixedId))
                .rewards(AdvancementRewards.Builder.recipe(prefixedId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                .apply { criteria.forEach(this::criterion) }
                .build(
                    recipeId.withPrefixedPath("recipes/misc/$prefix"),
                ),
        )
    }
}
