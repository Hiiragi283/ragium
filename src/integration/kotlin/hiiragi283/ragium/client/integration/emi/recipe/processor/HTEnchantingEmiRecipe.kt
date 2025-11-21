package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.crafting.RecipeHolder

class HTEnchantingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTEnchantingRecipe>) :
    HTEmiHolderRecipe<HTEnchantingRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(RagiumFluidContents.EXPERIENCE.toFluidEmi(recipe.getRequiredExpFluid()))

        addOutputs(recipe.getEnchantedBook().toEmi())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))

        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
