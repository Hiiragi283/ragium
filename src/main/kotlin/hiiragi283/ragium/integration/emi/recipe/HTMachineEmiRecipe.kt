package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import net.minecraft.resources.ResourceLocation

abstract class HTMachineEmiRecipe(
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    definition: HTRecipeDefinition,
) : HTEmiRecipe {
    protected val itemInputs: List<EmiIngredient> = definition.itemInputs.map(NeoForgeEmiIngredient::of)
    protected val fluidInputs: List<EmiIngredient> = definition.fluidInputs.map(NeoForgeEmiIngredient::of)
    protected val catalyst: EmiIngredient = EmiIngredient.of(definition.catalyst)
    protected val itemOutputs: List<EmiStack> = definition.itemOutputs.map(HTItemOutput::get).map(EmiStack::of)
    protected val fluidOutput: List<EmiStack> =
        definition.fluidOutputs.map(HTFluidOutput::get).map(NeoForgeEmiStack::of)

    fun WidgetHolder.addItemInput(index: Int, x: Double, y: Double) {
        addInput(itemInputs.getOrNull(index) ?: EmiStack.EMPTY, x, y)
    }

    fun WidgetHolder.addFluidInput(index: Int, x: Double, y: Double) {
        addInput(fluidInputs.getOrNull(index) ?: EmiStack.EMPTY, x, y)
    }

    fun WidgetHolder.addCatalyst(x: Double, y: Double) {
        addInput(catalyst, x, y)
    }

    fun WidgetHolder.addItemOutput(index: Int, x: Double, y: Double) {
        addOutput(itemOutputs.getOrNull(index) ?: EmiStack.EMPTY, x, y)
    }

    fun WidgetHolder.addFluidOutput(index: Int, x: Double, y: Double) {
        addOutput(fluidOutput.getOrNull(index) ?: EmiStack.EMPTY, x, y)
    }

    override fun getCategory(): EmiRecipeCategory = category

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = buildList {
        addAll(itemInputs)
        addAll(fluidInputs)
    }

    override fun getOutputs(): List<EmiStack> = buildList {
        addAll(itemOutputs)
        addAll(fluidOutput)
    }
}
