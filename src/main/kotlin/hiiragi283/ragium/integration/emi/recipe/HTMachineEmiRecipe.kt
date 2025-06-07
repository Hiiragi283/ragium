package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.screen.tooltip.EmiTooltip
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.emi.toEmi
import net.minecraft.resources.ResourceLocation

abstract class HTMachineEmiRecipe(
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    definition: HTRecipeDefinition,
) : HTEmiRecipe {
    protected val itemInputs: List<EmiIngredient> = definition.itemInputs.map(NeoForgeEmiIngredient::of)
    protected val fluidInputs: List<EmiIngredient> = definition.fluidInputs.map(NeoForgeEmiIngredient::of)
    protected val catalyst: EmiIngredient = EmiIngredient.of(definition.catalyst)
    protected val itemOutputs: List<HTItemOutput> = definition.itemOutputs
    protected val fluidOutput: List<HTFluidOutput> = definition.fluidOutputs

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
        val output: HTItemOutput? = itemOutputs.getOrNull(index)
        if (output != null) {
            addOutput(output.toEmi(), x, y).appendTooltip { _: EmiIngredient -> EmiTooltip.chance("produce", output.chance) }
        } else {
            addOutput(EmiStack.EMPTY, x, y)
        }
    }

    fun WidgetHolder.addFluidOutput(index: Int, x: Double, y: Double) {
        addOutput(fluidOutput.getOrNull(index)?.toEmi() ?: EmiStack.EMPTY, x, y)
    }

    override fun getCategory(): EmiRecipeCategory = category

    override fun getId(): ResourceLocation = id

    override fun getInputs(): List<EmiIngredient> = buildList {
        addAll(itemInputs)
        addAll(fluidInputs)
    }

    override fun getOutputs(): List<EmiStack> = buildList {
        addAll(itemOutputs.map(HTItemOutput::toEmi))
        addAll(fluidOutput.map(HTFluidOutput::toEmi))
    }
}
