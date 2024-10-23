package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTFluidIngredient
import hiiragi283.ragium.api.recipe.HTFluidResult
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.RagiumContents
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
    private val type: HTMachineType,
    private val tier: HTMachineTier = HTMachineTier.PRIMITIVE,
) {
    companion object {
        @JvmStatic
        fun create(type: HTMachineConvertible, minTier: HTMachineTier = HTMachineTier.PRIMITIVE): HTMachineRecipeJsonBuilder = type
            .asProcessor()
            ?.let { HTMachineRecipeJsonBuilder(it, minTier) }
            ?: throw IllegalStateException("Machine SizeType;  ${type.asMachine().id} must be Processor!")

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
        fun createRecipeId(fluid: RagiumContents.Fluids): Identifier = createRecipeId(fluid.fluidEntry.value())
    }

    private val itemInputs: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidInputs: MutableList<HTFluidIngredient> = mutableListOf()
    private val itemOutputs: MutableList<HTItemResult> = mutableListOf()
    private val fluidOutputs: MutableList<HTFluidResult> = mutableListOf()
    private var catalyst: HTItemIngredient = HTItemIngredient.EMPTY_ITEM

    //    Input    //

    fun itemInput(item: ItemConvertible, amount: Long = 1): HTMachineRecipeJsonBuilder = apply {
        itemInputs.add(HTItemIngredient.ofItem(item, amount))
    }

    fun itemInput(tagKey: TagKey<Item>, amount: Long = 1): HTMachineRecipeJsonBuilder = apply {
        itemInputs.add(HTItemIngredient.ofItem(tagKey, amount))
    }

    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTFluidIngredient.ofFluid(fluid, amount))
    }

    fun fluidInput(fluid: RagiumContents.Fluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTFluidIngredient.ofFluid(fluid.fluidEntry.value(), amount))
    }

    fun fluidInput(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTFluidIngredient.ofFluid(tagKey, amount))
    }

    //    Output    //

    fun itemOutput(
        item: ItemConvertible,
        amount: Long = 1,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        itemOutputs.add(HTItemResult.ofItem(item, amount, components))
    }

    fun itemOutput(stack: ItemStack): HTMachineRecipeJsonBuilder = apply { itemOutputs.add(HTItemResult.ofItem(stack)) }

    fun fluidOutput(
        fluid: Fluid,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult.ofFluid(fluid, amount, components))
    }

    fun fluidOutput(
        fluid: RagiumContents.Fluids,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTFluidResult.ofFluid(fluid.fluidEntry.value(), amount, components))
    }

    //    Catalyst    //

    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.ofItem(item)
    }

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTItemIngredient.ofItem(tagKey)
    }

    fun offerTo(exporter: RecipeExporter, output: ItemConvertible, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: Fluid, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, output: RagiumContents.Fluids, suffix: String = "") {
        offerTo(exporter, createRecipeId(output).withSuffixedPath(suffix))
    }

    fun offerTo(exporter: RecipeExporter, recipeId: Identifier) {
        val prefix = "${type.id.path}/"
        val prefixedId: Identifier = recipeId.withPrefixedPath(prefix)
        val recipe: HTMachineRecipe = HTMachineRecipe.createRecipe(
            HTMachineDefinition(type, tier),
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
