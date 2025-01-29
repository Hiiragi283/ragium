package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.machine.HTMachineTier
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.ingredients.IIngredientRenderer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.item.TooltipFlag

object HTMaterialTierRenderer : IIngredientRenderer<HTMachineTier> {
    override fun render(guiGraphics: GuiGraphics, ingredient: HTMachineTier) {
        guiGraphics.renderFakeItem(
            ingredient.getCircuit().toStack(),
            0,
            0,
        )
    }

    override fun getTooltip(tooltip: ITooltipBuilder, ingredient: HTMachineTier, tooltipFlag: TooltipFlag) {
        tooltip.add(ingredient.text)
    }

    @Suppress("removal")
    @Deprecated("Deprecated in Java")
    override fun getTooltip(ingredient: HTMachineTier, tooltipFlag: TooltipFlag): List<Component> = listOf()

    override fun getWidth(): Int = 16

    override fun getHeight(): Int = 16
}
