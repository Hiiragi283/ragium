package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.HTFireboxMachineScreenHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

@Environment(EnvType.CLIENT)
class HTFireboxMachineScreen(handler: HTFireboxMachineScreenHandler, inventory: PlayerInventory, title: Text) :
    HTScreenBase<HTFireboxMachineScreenHandler>(handler, inventory, title) {
    companion object {
        @JvmField
        val TEXTURE: Identifier = RagiumAPI.id("textures/gui/firebox.png")
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        context.drawTexture(TEXTURE, startX, startY, 0, 0, backgroundWidth, backgroundHeight)

        if (!handler.isBurning()) return
        val progress: Int = MathHelper.ceil(handler.getProgress() * 13f) + 1
        context.drawGuiTexture(
            Identifier.ofVanilla("container/furnace/lit_progress"),
            16,
            16,
            0,
            14 - progress,
            startX + getSlotPosX(4),
            startY + getSlotPosY(2) - progress - 1,
            14,
            progress,
        )
    }
}
