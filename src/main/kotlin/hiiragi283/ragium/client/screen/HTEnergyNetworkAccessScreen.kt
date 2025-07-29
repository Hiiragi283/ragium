package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.HTEnergyNetworkAccessMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.energy.IEnergyStorage

@OnlyIn(Dist.CLIENT)
class HTEnergyNetworkAccessScreen(menu: HTEnergyNetworkAccessMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTEnergyNetworkAccessMenu>(menu, inventory, title) {
    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        // energy amount
        renderEnergyTooltip(guiGraphics, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(0), mouseX, mouseY)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        guiGraphics.blit(
            RagiumAPI.id("textures/gui/container/energy_network_access.png"),
            startX,
            startY,
            0,
            0,
            imageWidth,
            imageHeight,
        )
        // energy
        renderEnergy(guiGraphics, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(3))
    }

    override fun getEnergyNetwork(menu: HTEnergyNetworkAccessMenu): IEnergyStorage? = menu.usePosition { level: Level, pos: BlockPos ->
        (level.getBlockEntity(pos) as? HTHandlerBlockEntity)?.getEnergyStorage(null)
    }
}
