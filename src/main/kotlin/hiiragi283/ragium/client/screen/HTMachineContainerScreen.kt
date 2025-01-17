package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTMachineContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory

open class HTMachineContainerScreen<T : HTMachineContainerMenu>(menu: T, playerInventory: Inventory, title: Component) :
    HTContainerScreen<T>(menu, playerInventory, title) {
    companion object {
        @JvmField
        val TEXTURE: ResourceLocation = RagiumAPI.id("textures/gui/machine.png")
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        guiGraphics.blit(TEXTURE, startX, startY, 0, 0, imageWidth, imageHeight)
        // progress bar
        guiGraphics.blitSprite(
            RagiumAPI.Companion.id("progress_bar"),
            16,
            16,
            0,
            0,
            startX + getSlotPosX(4),
            startY + getSlotPosY(1),
            Mth.ceil(menu.getProgress() * 16f),
            16,
        )
        // item slots
        menu.itemSlots.forEach { (slotX: Int, slotY: Int) ->
            guiGraphics.blitSprite(
                RagiumAPI.Companion.id("item_slot"),
                startX + getSlotPosX(slotX) - 1,
                startY + getSlotPosY(slotY) - 1,
                18,
                18,
            )
        }
        // fluid slots
        menu.fluidSlots.forEach { index: Int, (slotX: Int, slotY: Int) ->
            guiGraphics.blitSprite(
                RagiumAPI.Companion.id("fluid_slot"),
                startX + getSlotPosX(slotX) - 1,
                startY + getSlotPosY(slotY) - 1,
                18,
                18,
            )
            // drawFluid(guiGraphics, fluidCache[index], slotX, slotY)
        }
    }
}
