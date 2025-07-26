package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.extension.getClientTooltipFlag
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.inventory.HTDefinitionContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTHandlerBlockEntity
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
abstract class HTDefinitionContainerScreen<T : HTDefinitionContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    HTContainerScreen<T>(menu, inventory, title) {
    abstract val texture: ResourceLocation

    abstract val progressPosX: Int
    abstract val progressPosY: Int

    open val progressSizeX: Int = 24
    open val progressSizeY: Int = 16
    open val progressTex: ResourceLocation = vanillaId("container/furnace/burn_progress")

    private var fluidCache: MutableMap<Int, FluidStack> = mutableMapOf()

    override fun init() {
        super.init()
        for ((index: Int, _) in menu.fluidSlots) {
            val stack: FluidStack = menu
                .usePosition { level: Level, pos: BlockPos ->
                    (level.getBlockEntity(pos) as? HTHandlerBlockEntity)?.getFluidHandler(null)
                }?.getFluidInTank(index) ?: continue
            fluidCache.put(index, stack)
        }
    }

    fun getFluidStack(index: Int): FluidStack = fluidCache[index] ?: FluidStack.EMPTY

    fun setFluidStack(index: Int, stack: FluidStack) {
        if (stack.isEmpty) {
            fluidCache.remove(index)
        } else {
            fluidCache[index] = stack
        }
    }

    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        // fluid tooltip
        menu.fluidSlots.forEach { (index: Int, vec: Vec3i) ->
            renderFluidTooltip(
                guiGraphics,
                getFluidStack(index),
                0,
                getClientTooltipFlag(),
                vec.x,
                vec.y,
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
        menu.fluidSlots.forEach { (index: Int, vec: Vec3i) ->
            renderFluid(guiGraphics, getFluidStack(index), vec.x, vec.y)
        }
        // energy
        renderEnergy(guiGraphics, HTSlotHelper.getSlotPosX(0), HTSlotHelper.getSlotPosY(3))
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
