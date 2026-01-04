package hiiragi283.ragium.client.emi.recipe

import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.client.emi.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.emi.RagiumEmiTextures
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.world.item.crafting.RecipeHolder

class HTMixingEmiRecipe(holder: RecipeHolder<HTMixingRecipe>) :
    HTComplexEmiRecipe<HTMixingRecipe>(RagiumConst.MIXER, RagiumEmiRecipeCategories.MIXING, holder) {
    override fun addSubProgress(widgets: WidgetHolder) {
        RagiumEmiTextures.addWidget(
            widgets,
            "mix",
            getPosition(2),
            getPosition(1.5),
            recipe.time,
            endToStart = true,
        )
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.mixer
}
