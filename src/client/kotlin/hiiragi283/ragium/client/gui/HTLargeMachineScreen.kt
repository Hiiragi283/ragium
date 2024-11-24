package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class HTLargeMachineScreen(handler: HTLargeMachineScreenHandler, inventory: PlayerInventory, title: Text) :
    HTMachineScreenBase<HTLargeMachineScreenHandler>(handler, inventory, title) {
    override val texture: Identifier = RagiumAPI.id("textures/gui/large_machine.png")
    override val fluidCache: Array<FluidVariant> = Array(4) { FluidVariant.blank() }
    override val amountCache: LongArray = LongArray(4) { 0 }

    override fun drawMouseoverTooltip(context: DrawContext, x: Int, y: Int) {
        super.drawMouseoverTooltip(context, x, y)
        drawFluidTooltip(context, fluidCache[0], amountCache[0], 2, 2, x, y)
        drawFluidTooltip(context, fluidCache[1], amountCache[1], 3, 2, x, y)
        drawFluidTooltip(context, fluidCache[2], amountCache[2], 5, 2, x, y)
        drawFluidTooltip(context, fluidCache[3], amountCache[3], 6, 2, x, y)
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        super.drawBackground(context, delta, mouseX, mouseY)
        drawFluid(context, fluidCache[0], 2, 2)
        drawFluid(context, fluidCache[1], 3, 2)
        drawFluid(context, fluidCache[2], 5, 2)
        drawFluid(context, fluidCache[3], 6, 2)
    }
}
