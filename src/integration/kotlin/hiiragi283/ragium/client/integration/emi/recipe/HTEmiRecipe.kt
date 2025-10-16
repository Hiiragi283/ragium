package hiiragi283.ragium.client.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.createErrorStack
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see [mekanism.client.recipe_viewer.emi.recipe.MekanismEmiRecipe]
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

    protected fun output(index: Int): EmiStack = outputs.getOrNull(index) ?: EmiStack.EMPTY

    protected fun addInput(ingredient: HTItemIngredient?) {
        addInput(ingredient?.let(::ingredient))
    }

    protected fun addInput(ingredient: HTFluidIngredient?) {
        addInput(ingredient?.let(::ingredient))
    }

    protected fun addInput(ingredient: EmiIngredient?) {
        inputs.add(ingredient ?: EmiStack.EMPTY)
    }

    protected fun addCatalyst(ingredient: HTItemIngredient) {
        addCatalyst(ingredient(ingredient))
    }

    protected fun addCatalyst(ingredient: EmiIngredient) {
        catalysts.add(ingredient)
    }

    protected fun addOutputs(result: HTItemResult?) {
        addOutputs(result?.let(::result))
    }

    protected fun addChancedOutputs(result: HTChancedItemResult?) {
        addOutputs(result?.let(::result)?.let { stack: EmiStack -> stack.setChance(result.chance) })
    }

    protected fun addOutputs(result: HTFluidResult?) {
        addOutputs(result?.let(::result))
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

    protected fun ingredient(ingredient: HTItemIngredient): EmiIngredient = ingredient
        .unwrap()
        .map(
            { (tagKey: TagKey<Item>, count: Int) -> tagKey.toEmi(count) },
            { stacks: List<ItemStack> -> stacks.map(ItemStack::toEmi).let(::ingredient) },
        ).apply {
            for (stack: EmiStack in emiStacks) {
                val itemStack: ItemStack = stack.itemStack
                if (itemStack.hasCraftingRemainingItem()) {
                    stack.remainder = itemStack.craftingRemainingItem.toEmi()
                }
            }
        }

    protected fun ingredient(ingredient: HTFluidIngredient): EmiIngredient = ingredient
        .unwrap()
        .map(
            { (tagKey: TagKey<Fluid>, count: Int) -> tagKey.toEmi(count) },
            { stacks: List<FluidStack> -> stacks.map(FluidStack::toEmi).let(::ingredient) },
        )

    private fun ingredient(stacks: List<EmiStack>): EmiIngredient = when {
        stacks.isEmpty() -> createErrorStack("No matching stacks")
        else -> EmiIngredient.of(stacks)
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
}
