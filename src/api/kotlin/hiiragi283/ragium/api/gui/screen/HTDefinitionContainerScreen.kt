package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTDefinitionContainerScreen<T : HTDefinitionContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    HTContainerScreen<T>(menu, inventory, title) {
    abstract val progressPosX: Int
    abstract val progressPosY: Int

    open val progressSizeX: Int = 24
    open val progressSizeY: Int = 16
    open val progressTex: ResourceLocation = vanillaId("container/furnace/burn_progress")

    override fun init() {
        super.init()
        // Energy Widget
        addRenderableWidget(createEnergyWidget(menu.level.dimension()))
    }

    /*override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        // fluid tooltip
        menu.fluidSlots.forEach { (index: Int, slot: HTFluidSlot) ->
            renderFluidTooltip(
                guiGraphics,
                getFluidStack(index),
                0,
                getClientTooltipFlag(),
                slot,
                mouseX,
                mouseY,
            )
        }
        // energy amount
        renderEnergyTooltip(guiGraphics, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(0), mouseX, mouseY)
    }*/

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY)
        // progress bar
        renderProgress(guiGraphics)
        // fluids
        /*menu.fluidSlots.forEach { (index: Int, slot: HTFluidSlot) ->
            renderFluid(guiGraphics, getFluidStack(index), slot)
        }*/
        // energy
        // renderEnergy(guiGraphics, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(3))
    }

    protected open fun renderProgress(guiGraphics: GuiGraphics) {
        guiGraphics.blitSprite(
            progressTex,
            progressSizeX,
            progressSizeY,
            0,
            0,
            startX + progressPosX,
            startY + progressPosY,
            Mth.ceil(menu.progress * progressSizeX),
            progressSizeY,
        )
    }
}
