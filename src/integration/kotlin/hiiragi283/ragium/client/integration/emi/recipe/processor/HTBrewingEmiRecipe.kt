package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * @see mekanism.client.recipe_viewer.emi.recipe.ItemStackToItemStackEmiRecipe
 */
class HTBrewingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBrewingRecipe>) :
    HTEmiHolderRecipe<HTBrewingRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(RagiumFluidContents.AWKWARD_WATER.toFluidEmi(1000))
        addOutputs(HTPotionHelper.createPotion(RagiumItems.POTION_DROP, recipe.contents).toEmi())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets.addSlot(input(1), getPosition(1), getPosition(2))

        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
