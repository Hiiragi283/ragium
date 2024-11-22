package hiiragi283.ragium.client.gui

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class HTSmallMachineScreen(handler: HTSmallMachineScreenHandler, inventory: PlayerInventory, title: Text) :
    HTMachineScreenBase<HTSmallMachineScreenHandler>(handler, inventory, title) {
    override val texture: Identifier = RagiumAPI.id("textures/gui/small_machine.png")
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
        super.drawBackground(context, delta, mouseX, mouseY)
        drawFluid(context, fluidCache[0], 2, 2)
        drawFluid(context, fluidCache[1], 6, 2)
    }
}
