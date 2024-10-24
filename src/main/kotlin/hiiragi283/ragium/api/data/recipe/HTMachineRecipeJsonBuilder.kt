package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeResult
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
import java.util.*

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
        fun createRecipeId(fluid: RagiumContents.Fluids): Identifier = createRecipeId(fluid.asFluid())
    }

    private val itemInputs: MutableList<HTIngredient.Item> = mutableListOf()
    private val fluidInputs: MutableList<HTIngredient.Fluid> = mutableListOf()
    private val itemOutputs: MutableList<HTRecipeResult.Item> = mutableListOf()
    private val fluidOutputs: MutableList<HTRecipeResult.Fluid> = mutableListOf()
    private var catalyst: HTIngredient.Item? = null

    //    Input    //

    fun itemInput(item: ItemConvertible, count: Int = 1): HTMachineRecipeJsonBuilder = apply {
        itemInputs.add(HTIngredient.ofItem(item, count))
    }

    fun itemInput(tagKey: TagKey<Item>, count: Int = 1): HTMachineRecipeJsonBuilder = apply {
        itemInputs.add(HTIngredient.ofItem(tagKey, count))
    }

    fun fluidInput(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTIngredient.ofFluid(fluid, amount))
    }

    fun fluidInput(fluid: RagiumContents.Fluids, amount: Long = FluidConstants.BUCKET): HTMachineRecipeJsonBuilder = apply {
        fluidInputs.add(HTIngredient.ofFluid(fluid.asFluid(), amount))
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

    fun fluidOutput(
        fluid: Fluid,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTRecipeResult.ofFluid(fluid, amount, components))
    }

    fun fluidOutput(
        fluid: RagiumContents.Fluids,
        amount: Long = FluidConstants.BUCKET,
        components: ComponentChanges = ComponentChanges.EMPTY,
    ): HTMachineRecipeJsonBuilder = apply {
        fluidOutputs.add(HTRecipeResult.ofFluid(fluid.asFluid(), amount, components))
    }

    //    Catalyst    //

    fun catalyst(item: ItemConvertible): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTIngredient.ofItem(item)
    }

    fun catalyst(tagKey: TagKey<Item>): HTMachineRecipeJsonBuilder = apply {
        catalyst = HTIngredient.ofItem(tagKey)
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
            Optional.ofNullable(catalyst),
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
