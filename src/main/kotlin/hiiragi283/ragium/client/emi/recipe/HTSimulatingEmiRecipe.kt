package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.integration.emi.HTEmiHolderRecipe
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.integration.emi.toItemEmi
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrNull

class HTSimulatingEmiRecipe(holder: RecipeHolder<HTSimulatingRecipe<*>>) :
    HTEmiHolderRecipe<HTSimulatingRecipe<*>>(RagiumEmiRecipeCategories.SIMULATING, holder) {
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
        widgets.addArrow(getPosition(3.5), getPosition(1), recipe.time)
        // input
        widgets.addSlot(input(0), getPosition(1.5), getPosition(0), HTBackgroundType.INPUT)
        // catalyst
        widgets.addSlot(catalyst(0), getPosition(1.5), getPosition(2), HTBackgroundType.NONE)
        // outputs
        widgets.addSlot(output(0), getPosition(5.5), getPosition(0.5), HTBackgroundType.OUTPUT)
        widgets.addTank(output(1), getPosition(7), HTBackgroundType.OUTPUT).recipeContext(this)
    }
}
