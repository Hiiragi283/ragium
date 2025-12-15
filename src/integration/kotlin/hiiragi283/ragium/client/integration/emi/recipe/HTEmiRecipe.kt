package hiiragi283.ragium.client.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.TextureWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import java.util.Random
import java.util.function.Function

/**
 * @see mekanism.client.recipe_viewer.emi.recipe.MekanismEmiRecipe
 */
abstract class HTEmiRecipe<RECIPE : Any>(
    private val category: EmiRecipeCategory,
    private val id: ResourceLocation,
    protected val recipe: RECIPE,
    private val bounds: HTBounds,
) : AbstractContainerEventHandler(),
    EmiRecipe {
    constructor(
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : this(category, id, recipe, category.bounds)

    private val inputs: MutableList<EmiIngredient> = mutableListOf()
    private val catalysts: MutableList<EmiIngredient> = mutableListOf()
    private val outputs: MutableList<EmiStack> = mutableListOf()
    private val renderOutputs: MutableList<EmiIngredient> = mutableListOf()

    protected fun input(index: Int): EmiIngredient = inputs.getOrNull(index) ?: EmiStack.EMPTY

    protected fun catalyst(index: Int): EmiIngredient = catalysts.getOrNull(index) ?: EmiStack.EMPTY

    protected fun output(index: Int): EmiIngredient = renderOutputs.getOrNull(index) ?: EmiStack.EMPTY

    protected fun addInput(ingredient: HTItemIngredient?) {
        addInput(ingredient?.let(HTItemIngredient::toEmi))
    }

    protected fun addInput(ingredient: HTFluidIngredient?) {
        addInput(ingredient?.let(HTFluidIngredient::toEmi))
    }

    protected fun addInput(ingredient: EmiIngredient?) {
        inputs.add(ingredient ?: EmiStack.EMPTY)
    }

    protected fun addEmptyInput() {
        inputs.add(EmiStack.EMPTY)
    }

    protected fun addCatalyst(ingredient: HTItemIngredient?) {
        addCatalyst(ingredient?.let(HTItemIngredient::toEmi))
    }

    protected fun addCatalyst(ingredient: EmiIngredient?) {
        catalysts.add(ingredient ?: EmiStack.EMPTY)
    }

    protected fun addOutputs(result: HTItemResult?) {
        addOutputs(result?.let(::result))
    }

    protected fun addOutputs(result: HTFluidResult?) {
        addOutputs(result?.let(::result))
    }

    protected fun addOutputs(results: Ior<HTItemResult, HTFluidResult>) {
        addOutputs(results.getLeft())
        addOutputs(results.getRight())
    }

    protected fun addOutputs(stacks: EmiStack?) {
        addOutputs(listOfNotNull(stacks))
    }

    protected fun addOutputs(stacks: List<EmiStack>) {
        if (stacks.isEmpty()) {
            outputs.add(EmiStack.EMPTY)
            renderOutputs.add(EmiStack.EMPTY)
        } else {
            outputs.addAll(stacks)
            renderOutputs.add(EmiIngredient.of(stacks))
        }
    }

    protected fun result(result: HTItemResult): EmiStack = result.toEmi()

    protected fun result(result: HTFluidResult): EmiStack = result.toEmi()

    //    EmiRecipe    //

    final override fun getCategory(): EmiRecipeCategory = category

    final override fun getId(): ResourceLocation = id

    final override fun getInputs(): List<EmiIngredient> = inputs

    final override fun getCatalysts(): List<EmiIngredient> = catalysts

    final override fun getOutputs(): List<EmiStack> = outputs

    final override fun getDisplayWidth(): Int = bounds.width

    final override fun getDisplayHeight(): Int = bounds.height

    override fun getBackingRecipe(): RecipeHolder<*>? = null

    //    AbstractContainerEventHandler    //

    final override fun children(): List<GuiEventListener> = listOf()

    //    Extensions    //

    fun getPosition(index: Int): Int = index * 18

    fun getPosition(index: Double): Int = (index * 18).toInt()

    fun WidgetHolder.setShapeless(): TextureWidget = addTexture(EmiTexture.SHAPELESS, getPosition(6) + 1, getPosition(0) + 3)

    fun WidgetHolder.addCatalyst(index: Int, x: Int, y: Int): SlotWidget {
        val catalyst: EmiIngredient = catalyst(index)
        return addSlot(catalyst, x, y).catalyst(!catalyst.isEmpty)
    }

    fun WidgetHolder.addOutput(
        index: Int,
        x: Int,
        y: Int,
        large: Boolean = false,
        drawBack: Boolean = true,
    ): SlotWidget = when {
        large -> addSlot(output(index), x - 4, y - 4).large(true)
        else -> addSlot(output(index), x, y)
    }.recipeContext(this@HTEmiRecipe).drawBack(drawBack)

    fun WidgetHolder.addGeneratedOutput(
        factory: Function<Random, EmiIngredient>,
        unique: Int,
        x: Int,
        y: Int,
        large: Boolean = false,
        drawBack: Boolean = true,
    ): SlotWidget = when {
        large -> addGeneratedSlot(factory, unique, x - 4, y - 4).large(true)
        else -> addGeneratedSlot(factory, unique, x, y)
    }.recipeContext(this@HTEmiRecipe).drawBack(drawBack)
}
