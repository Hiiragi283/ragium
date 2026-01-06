package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.integration.emi.toItemEmi
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrNull

class HTSimulatingEmiRecipe(holder: RecipeHolder<HTSimulatingRecipe<*>>) :
    HTProcessingEmiRecipe<HTSimulatingRecipe<*>>(RagiumConst.SIMULATOR, RagiumEmiRecipeCategories.SIMULATING, holder) {
    init {
        addInput(recipe.ingredient.getOrNull())

        when (val recipe: HTSimulatingRecipe<*> = this.recipe) {
            is HTBlockSimulatingRecipe -> recipe.catalyst.map(Holder<Block>::toItemEmi)
            is HTEntitySimulatingRecipe -> recipe.catalyst.map(HTImitationSpawnerBlock::createStack).map(ItemStack::toEmi)
            else -> listOf()
        }.let(EmiIngredient::of).let(::addCatalyst)

        addOutputs(recipe.result)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        super.addWidgets(widgets)
        // Input
        widgets.addInput(0, getPosition(1), getPosition(0))
        // Catalyst
        widgets.addCatalyst(0, getPosition(1), getPosition(2))
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0.5))
        widgets.addTank(output(1), getPosition(7), getCapacity(RagiumFluidConfigType.FIRST_OUTPUT)).recipeContext(this)
    }

    override fun getArrowX(): Int = getPosition(2.5)
}
