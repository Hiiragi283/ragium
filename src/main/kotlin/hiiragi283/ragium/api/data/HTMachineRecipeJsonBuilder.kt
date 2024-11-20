package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTFluidIngredient
import hiiragi283.ragium.api.recipe.HTFluidResult
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTMachineRecipe
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
        fun create(key: HTMachineKey, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilder =
            HTMachineRecipeJsonBuilder(key, minTier)

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

    private val itemInputs: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidInputs: MutableList<HTFluidIngredient> = mutableListOf()
    private val itemOutputs: MutableList<HTItemResult> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidResult> = mutableListOf()
    private var catalyst: HTItemIngredient? = null

    //    Input    //

    fun itemInput(ingredient: HTItemIngredient): HTMachineRecipeJsonBuilder = apply {
        itemInputs.add(ingredient)
    }

    fun itemInput(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(prefix, material, count, consumeType))

    fun itemInput(
        content: HTContent.Material<*>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(content.prefixedTagKey, count, consumeType)

    fun itemInput(
        item: ItemConvertible,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(item, count, consumeType))

    fun itemInput(
        tagKey: TagKey<Item>,
        count: Int = 1,
        consumeType: HTItemIngredient.ConsumeType = HTItemIngredient.ConsumeType.DECREMENT,
    ): HTMachineRecipeJsonBuilder = itemInput(HTItemIngredient.of(tagKey, count, consumeType))

    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTFluidIngredient.of(fluid, amount))
    }

    fun fluidInput(fluid: RagiumFluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTFluidIngredient.of(fluid.value, amount))
    }

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTFluidIngredient.of(tagKey, amount))
    }

    //    Output    //

    fun itemOutput(
        item: ItemConvertible,
        count: Int = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        itemOutputs.add(HTItemResult(item, count, components))
    }

    fun itemOutput(stack: ItemStack): HTMachineRecipeJsonBuilder = apply { itemOutputs.add(HTItemResult(stack)) }

    fun fluidOutput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult(fluid, amount))
    }

    fun fluidOutput(fluid: RagiumFluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult(fluid.value, amount))
    }

    //    Catalyst    //

    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.of(item)
    }

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.of(tagKey)
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
