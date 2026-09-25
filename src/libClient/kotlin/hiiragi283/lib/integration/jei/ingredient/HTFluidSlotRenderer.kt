package hiiragi283.lib.integration.jei.ingredient

import hiiragi283.lib.gui.HTGuiRenderHelper
import hiiragi283.lib.text.HTTextUtil
import hiiragi283.lib.text.translatableText
import mezz.jei.api.ingredients.IIngredientRenderer
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
data object HTFluidSlotRenderer : IIngredientRenderer<FluidStack> {
    override fun render(guiGraphics: GuiGraphicsExtractor, ingredient: FluidStack) {
        render(guiGraphics, ingredient, 0, 0)
    }

    override fun render(guiGraphics: GuiGraphicsExtractor, ingredient: FluidStack, posX: Int, posY: Int) {
        HTGuiRenderHelper.renderFluid(guiGraphics, ingredient, posX, posY, width, height)
    }

    @Suppress("removal")
    @Deprecated("Deprecated in Java")
    override fun getTooltip(ingredient: FluidStack, tooltipFlag: TooltipFlag): List<Component> {
        val minecraft: Minecraft = Minecraft.getInstance()
        val tooltipContext: Item.TooltipContext = Item.TooltipContext.of(minecraft.level)
        return getTooltip(ingredient, tooltipContext, minecraft.player, tooltipFlag)
    }

    override fun getTooltip(
        ingredient: FluidStack,
        tooltipContext: Item.TooltipContext,
        player: Player?,
        tooltipFlag: TooltipFlag
    ): List<Component> = when {
        ingredient.isEmpty -> listOf()

        else -> buildList {
            addAll(ingredient.getTooltipLines(tooltipContext, player, tooltipFlag))
            add(
                translatableText(
                    "jei.tooltip.liquid.amount",
                    HTTextUtil.INT_FORMAT.format(ingredient.amount)
                ).withStyle(ChatFormatting.GRAY)
            )
        }
    }
}
