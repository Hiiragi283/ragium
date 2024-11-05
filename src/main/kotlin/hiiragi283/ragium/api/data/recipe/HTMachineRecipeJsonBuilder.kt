package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.RagiumMaterials
import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.common.init.RagiumFluids
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.component.ComponentChanges
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTMachineRecipeJsonBuilder private constructor(
    private val key: HTMachineKey,
    private val tier: HTMachineTier = HTMachineTier.PRIMITIVE,
) {
    companion object {
        @JvmStatic
        fun create(machine: HTMachine, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilder =
            HTMachineRecipeJsonBuilder(machine.key, minTier)

        @JvmStatic
        fun createRecipeId(item: ItemConvertible): Identifier = CraftingRecipeJsonBuilder
            .getItemId(item)
            .path
            .let { RagiumAPI.id(it) }

        @JvmStatic
        fun createRecipeId(fluid: Fluid): Identifier = Registries.FLUID
            .getId(fluid)
            .path
            .let { RagiumAPI.id(it) }

        @JvmStatic
        fun createRecipeId(fluid: RagiumFluids): Identifier = createRecipeId(fluid.value)
    }

    private val itemInputs: MutableList<HTIngredient.Item> = mutableListOf()
    private val fluidInputs: MutableList<HTIngredient.Fluid> = mutableListOf()
    private val itemOutputs: MutableList<HTRecipeResult.Item> = mutableListOf()
    private val fluidOutputs: MutableList<HTRecipeResult.Fluid> = mutableListOf()
    private var catalyst: HTIngredient.Item? = null

    //    Input    //

    fun itemInput(ingredient: HTIngredient.Item): HTMachineRecipeJsonBuilder = apply {
        itemInputs.add(ingredient)
    }

    fun itemInput(prefix: HTTagPrefix, material: RagiumMaterials, count: Int = 1): HTMachineRecipeJsonBuilder =
        itemInput(prefix.createTag(material), count)

    fun itemInput(content: HTContent.Material<*>, count: Int = 1): HTMachineRecipeJsonBuilder = itemInput(content.prefixedTagKey, count)

    fun itemInput(item: ItemConvertible, count: Int = 1): HTMachineRecipeJsonBuilder = itemInput(HTIngredient.ofItem(item, count))

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeJsonBuilder = itemInput(HTIngredient.ofItem(tagKey, count))

    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTIngredient.ofFluid(fluid, amount))
    }

    fun fluidInput(fluid: RagiumFluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTIngredient.ofFluid(fluid.value, amount))
    }

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTIngredient.ofFluid(tagKey, amount))
    }

    //    Output    //

    fun itemOutput(
        item: ItemConvertible,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        itemOutputs.add(HTRecipeResult.ofItem(item, count, components))
    }

    fun itemOutput(stack: ItemStack): HTMachineRecipeJsonBuilder = apply { itemOutputs.add(HTRecipeResult.ofItem(stack)) }

    @Deprecated("Experimental Feature")
    fun itemOutput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        itemOutputs.add(HTRecipeResult.ofItem(tagKey, count, components))
    }

    fun fluidOutput(
        fluid: Fluid,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTRecipeResult.ofFluid(fluid, amount, components))
    }

    fun fluidOutput(
        fluid: RagiumFluids,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTRecipeResult.ofFluid(fluid.value, amount, components))
    }

    @Deprecated("Experimental Feature")
    fun fluidOutput(
        fluid: TagKey<Fluid>,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTRecipeResult.ofFluid(fluid, amount, components))
    }

    //    Catalyst    //

    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTIngredient.ofItem(item)
    }

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTIngredient.ofItem(tagKey)
    }

    @Deprecated(
        "Experimental Feature",
        ReplaceWith(
            "offerTo(exporter, RagiumAPI.id(output.id.path).withSuffixedPath(suffix))",
            "hiiragi283.ragium.api.RagiumAPI",
        ),
    )
    fun offerTo(exporter: RecipeExporter, output: TagKey<*>, suffix: String = "") {
        offerTo(exporter, RagiumAPI.id(output.id.path).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: ItemConvertible, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: Fluid, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: RagiumFluids, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val prefix = "${key.id.path}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        val recipe = HTMachineRecipe(
            HTMachineDefinition(key, tier),
            itemInputs,
            fluidInputs,
            catalyst,
            itemOutputs,
            fluidOutputs,
        )
        exporter.accept(
            prefixedId,
            recipe,
            null,
        )
    }
}
