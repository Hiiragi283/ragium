package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTHandlerBlockEntity
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

abstract class HTDefinitionContainerScreen<T : HTDefinitionContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    HTContainerScreen<T>(menu, inventory, title) {
    abstract val texture: ResourceLocation

    abstract val progressPosX: Int
    abstract val progressPosY: Int

    open val progressSizeX: Int = 24
    open val progressSizeY: Int = 16
    open val progressTex: ResourceLocation = vanillaId("container/furnace/burn_progress")

    private fun getFluidHandler(): IFluidHandler? = menu.usePosition { level: Level, pos: BlockPos ->
        (level.getBlockEntity(pos) as? HTHandlerBlockEntity)?.getFluidHandler(null)
    }

    fun getFluidStack(index: Int): FluidStack = getFluidHandler()?.getFluidInTank(index) ?: FluidStack.EMPTY

    fun getFluidCapacity(index: Int): Int = getFluidHandler()?.getTankCapacity(index) ?: 0

    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        // fluid tooltip
        menu.fluidSlots.forEach { index: Int, (slotX: Int, slotY: Int) ->
            renderFluidTooltip(
                guiGraphics,
                getFluidStack(index),
                getFluidCapacity(index),
                slotX,
                slotY,
                mouseX,
                mouseY,
            )
        }
        // energy amount
        renderEnergyTooltip(guiGraphics, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(0), mouseX, mouseY)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        // background
        guiGraphics.blit(texture, startX, startY, 0, 0, imageWidth, imageHeight)
        // progress bar
        renderProgress(guiGraphics)
        // fluids
        menu.fluidSlots.forEach { index: Int, (slotX: Int, slotY: Int) ->
            renderFluid(guiGraphics, getFluidStack(index), slotX, slotY)
        }
        // item slots
        /*menu.itemSlots.forEach { (slotX: Int, slotY: Int) ->
            guiGraphics.blitSprite(
                RagiumAPI.id("item_slot"),
                startX + getSlotPosX(slotX) - 1,
                startY + getSlotPosY(slotY) - 1,
                18,
                18,
            )
        }
        // fluid slots
        menu.fluidSlots.forEach { index: Int, (slotX: Int, slotY: Int) ->
            guiGraphics.blitSprite(
                RagiumAPI.id("fluid_slot"),
                startX + getSlotPosX(slotX) - 1,
                startY + getSlotPosY(slotY) - 1,
                18,
                18,
            )
            renderFluid(guiGraphics, getFluidStack(index), slotX, slotY)
        }*/
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

    override fun getEnergyNetwork(menu: T): IEnergyStorage? = menu.usePosition { level: Level, pos: BlockPos ->
        (level.getBlockEntity(pos) as? HTHandlerBlockEntity)?.getEnergyStorage(null)
    }
}
