package hiiragi283.ragium.client.integration.emi.recipe.custom

import dev.emi.emi.EmiUtil
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.resources.ResourceLocation

class HTExpExtractingEmiRecipe(id: ResourceLocation) : HTEmiRecipe<Unit>(RagiumEmiRecipeCategories.EXTRACTING, id, Unit) {
    companion object {
        @JvmStatic
        private val UNIQUE_ID: Int = EmiUtil.RANDOM.nextInt()
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))

        // Input
        widgets.addGeneratedSlot({ HTEnchantEmiRecipeHelper.getTool(it, 0) }, UNIQUE_ID, getPosition(1), getPosition(0))
        widgets.addSlot(EmiStack.EMPTY, getPosition(1), getPosition(2)).catalyst(true)
        // Output
        widgets.addGeneratedOutput({ HTEnchantEmiRecipeHelper.getTool(it, 1) }, UNIQUE_ID, getPosition(4.5), getPosition(0) + 4, true)
        widgets.addSlot(RagiumFluidContents.EXPERIENCE.toFluidEmi(), getPosition(4.5), getPosition(2))
    }
}
