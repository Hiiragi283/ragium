package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.HTFilteringPipeScreenHandler
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class HTFilteringPipeScreen(handler: HTFilteringPipeScreenHandler, inventory: PlayerInventory, title: Text) :
    HTScreenBase<HTFilteringPipeScreenHandler>(handler, inventory, title) {
    companion object {
        @JvmField
        val TEXTURE: Identifier = Identifier.of("textures/gui/container/generic_54.png")
    }

    init {
        backgroundHeight = 114 + 6 * 18
        playerInventoryTitleY = backgroundHeight - 94
    }

    override fun render(
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        super.render(context, mouseX, mouseY, delta)
        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        context.drawTexture(TEXTURE, startX, startY, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        RagiumAPI.LOGGER.info("X: $mouseX, Y: $mouseY, Button: $button")
        return super.mouseClicked(mouseX, mouseY, button)
    }
}
