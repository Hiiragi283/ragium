package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.HTMachineScreenBase
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

@Environment(EnvType.CLIENT)
class HTSimpleMachineScreen(handler: HTSimpleMachineScreenHandler, inventory: PlayerInventory, title: Text) :
    HTMachineScreenBase<HTSimpleMachineScreenHandler>(handler, inventory, title) {
    companion object {
        @JvmField
        val TEXTURE: Identifier = RagiumAPI.id("textures/gui/simple_machine.png")
    }

    override val fluidCache: Array<FluidVariant> = Array(2) { FluidVariant.blank() }
    override val amountCache: LongArray = LongArray(2) { 0 }

    override fun drawMouseoverTooltip(context: DrawContext, x: Int, y: Int) {
        super.drawMouseoverTooltip(context, x, y)

        drawFluidTooltip(context, fluidCache[0], amountCache[0], 2, 2, x, y)
        drawFluidTooltip(context, fluidCache[1], amountCache[1], 6, 2, x, y)
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        context.drawTexture(TEXTURE, startX, startY, 0, 0, backgroundWidth, backgroundHeight)

        drawFluid(context, fluidCache[0], 2, 2)
        drawFluid(context, fluidCache[1], 6, 2)

        context.drawGuiTexture(
            Identifier.ofVanilla("container/furnace/burn_progress"),
            24,
            16,
            0,
            0,
            startX + getSlotPosX(4) - 3,
            startY + getSlotPosY(1),
            MathHelper.ceil(handler.getProgress() * 24f),
            16,
        )
    }
}
