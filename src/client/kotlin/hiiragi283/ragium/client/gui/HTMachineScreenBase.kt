package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

@Environment(EnvType.CLIENT)
abstract class HTMachineScreenBase<T : HTMachineScreenHandlerBase>(handler: T, inventory: PlayerInventory, title: Text) :
    HTScreenBase<T>(handler, inventory, title) {
    abstract val texture: Identifier
    abstract val fluidCache: Array<FluidVariant>
    abstract val amountCache: LongArray

    override fun drawMouseoverTooltip(context: DrawContext, x: Int, y: Int) {
        super.drawMouseoverTooltip(context, x, y)
        drawEnergyTooltip(context, handler.player.world.registryKey, 4, 1, x, y)
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        context.drawTexture(texture, startX, startY, 0, 0, backgroundWidth, backgroundHeight)
        context.drawGuiTexture(
            RagiumAPI.id("progress_bar"),
            16,
            16,
            0,
            0,
            startX + getSlotPosX(4),
            startY + getSlotPosY(1),
            MathHelper.ceil(handler.getProgress() * 16f),
            16,
        )
    }
}
