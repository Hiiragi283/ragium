package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.client.integration.emi.toItemEmi
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.recipe.HTBasicSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.Block
import kotlin.jvm.optionals.getOrNull

class HTSimulatingEmiRecipe(holder: RecipeHolder<HTBasicSimulatingRecipe<*>>) :
    HTEmiHolderRecipe<HTBasicSimulatingRecipe<*>>(RagiumEmiRecipeCategories.SIMULATING, holder) {
    init {
        addInput(recipe.ingredient.getOrNull())

        when (recipe) {
            is HTBlockSimulatingRecipe -> recipe.catalyst.map(Holder<Block>::toItemEmi)
            is HTEntitySimulatingRecipe -> recipe.catalyst.map(HTImitationSpawnerBlock::createStack).map(ItemStack::toEmi)
            else -> listOf()
        }.let(EmiIngredient::of).let(::addCatalyst)

        addOutputs(recipe.results)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets
            .addCatalyst(0, getPosition(1), getPosition(2))
            .appendTooltip(RagiumCommonTranslation.EMI_BLOCK_CATALYST.translate(ChatFormatting.AQUA))
        // Output
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addSlot(output(1), getPosition(4.5), getPosition(2))
    }
}
