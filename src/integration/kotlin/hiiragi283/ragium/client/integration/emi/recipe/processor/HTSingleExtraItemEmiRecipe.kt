package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiHolderRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleExtraItemRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTSingleExtraItemEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTBasicSingleExtraItemRecipe>) :
    HTEmiHolderRecipe<HTBasicSingleExtraItemRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
        addOutputs(recipe.extra.getOrNull())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // input
        widgets.addSlot(input(0), getPosition(1), getPosition(0))
        widgets
            .addSlot(RagiumFluidContents.LUBRICANT.getFluidTag().toEmi(), getPosition(1), getPosition(2))
            .catalyst(true)
        // outputs
        widgets.addOutput(0, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addSlot(output(1), getPosition(4.5), getPosition(2))
    }
}
