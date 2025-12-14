package hiiragi283.ragium.client.integration.emi.recipe.device

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.client.integration.emi.addArrow
import hiiragi283.ragium.client.integration.emi.addPlus
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.data.HTRockGenerationEmiData
import hiiragi283.ragium.client.integration.emi.recipe.HTEmiRecipe
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.minecraft.ChatFormatting
import net.minecraft.resources.ResourceLocation

class HTRockGeneratingEmiRecipe(id: ResourceLocation, recipe: HTRockGenerationEmiData) :
    HTEmiRecipe<HTRockGenerationEmiData>(RagiumEmiRecipeCategories.ROCK_GENERATING, id, recipe) {
    init {
        addInput(recipe.water)
        addInput(recipe.lava)
        addCatalyst(recipe.output)

        addOutputs(recipe.output)
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addArrow(getPosition(2.5), getPosition(1))
        widgets.addPlus(getPosition(1), getPosition(0))

        // inputs
        widgets.addSlot(input(0), getPosition(0), getPosition(0))
        widgets.addSlot(input(1), getPosition(2), getPosition(0))

        widgets
            .addCatalyst(0, getPosition(1), getPosition(2))
            .appendTooltip(RagiumCommonTranslation.EMI_BLOCK_CATALYST.translate(ChatFormatting.AQUA))
        // output
        widgets.addOutput(0, getPosition(4.5), getPosition(1), true)
    }
}
