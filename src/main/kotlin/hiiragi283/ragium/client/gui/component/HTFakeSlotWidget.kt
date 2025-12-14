package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.client.gui.component.base.HTAbstractWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

/**
 * @see mekanism.client.gui.element.slot.GuiVirtualSlot
 */
class HTFakeSlotWidget(private val getter: Supplier<ItemStack>, x: Int, y: Int) : HTAbstractWidget(x, y, 18, 18, Component.empty()) {
    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val font: Font = Minecraft.getInstance().font
        // Render stack
        val stack: ItemStack = getter.get()
        if (stack.isEmpty) return
        guiGraphics.renderFakeItem(stack, x, y)
        guiGraphics.renderItemDecorations(font, stack, x, y)
        // Render tooltip
        if (isHovered(mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, stack, mouseX, mouseY)
        }
    }
}
