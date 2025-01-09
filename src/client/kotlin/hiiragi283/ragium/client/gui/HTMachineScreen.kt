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
open class HTMachineScreen<T : HTMachineScreenHandlerBase>(handler: T, inventory: PlayerInventory, title: Text) :
    HTHandledScreenBase<T>(handler, inventory, title) {
    companion object {
        @JvmField
        val TEXTURE: Identifier = RagiumAPI.Companion.id("textures/gui/machine.png")
    }

    val fluidCache: Array<FluidVariant> = Array(10) { FluidVariant.blank() }
    val amountCache: LongArray = LongArray(10) { 0 }

    override fun drawMouseoverTooltip(context: DrawContext, x: Int, y: Int) {
        super.drawMouseoverTooltip(context, x, y)
        drawEnergyTooltip(context, handler.player.world.registryKey, 4, 1, x, y)
        // fluid tooltip
        handler.fluidSlots.forEach { index: Int, (slotX: Int, slotY: Int) ->
            drawFluidTooltip(context, fluidCache[index], amountCache[index], slotX, slotY, x, y)
        }
    }

    override fun drawBackground(
        context: DrawContext,
        delta: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        context.drawTexture(TEXTURE, startX, startY, 0, 0, backgroundWidth, backgroundHeight)
        // progress bar
        context.drawGuiTexture(
            RagiumAPI.Companion.id("progress_bar"),
            16,
            16,
            0,
            0,
            startX + getSlotPosX(4),
            startY + getSlotPosY(1),
            MathHelper.ceil(handler.getProgress() * 16f),
            16,
        )
        // item slots
        handler.itemSlots.forEach { (slotX: Int, slotY: Int) ->
            context.drawGuiTexture(
                RagiumAPI.Companion.id("item_slot"),
                startX + getSlotPosX(slotX) - 1,
                startY + getSlotPosY(slotY) - 1,
                18,
                18,
            )
        }
        // fluid slots
        handler.fluidSlots.forEach { index: Int, (slotX: Int, slotY: Int) ->
            context.drawGuiTexture(
                RagiumAPI.Companion.id("fluid_slot"),
                startX + getSlotPosX(slotX) - 1,
                startY + getSlotPosY(slotY) - 1,
                18,
                18,
            )
            drawFluid(context, fluidCache[index], slotX, slotY)
        }
    }
}
