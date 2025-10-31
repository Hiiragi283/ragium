package hiiragi283.ragium.client.event

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTClientItemTooltipComponent(val content: HTItemTooltipContent) : ClientTooltipComponent {
    override fun getHeight(): Int = 20

    override fun getWidth(font: Font): Int = 20

    override fun renderImage(
        font: Font,
        x: Int,
        y: Int,
        guiGraphics: GuiGraphics,
    ) {
        val stack: ItemStack = content.stack.unwrap()
        guiGraphics.renderFakeItem(stack, x, y)
        guiGraphics.renderItemDecorations(font, stack, x, y)
    }
}
