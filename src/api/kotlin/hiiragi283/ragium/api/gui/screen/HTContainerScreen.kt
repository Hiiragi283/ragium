package hiiragi283.ragium.api.gui.screen

import hiiragi283.ragium.api.gui.component.HTEnergyNetworkWidget
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.level.Level
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<T : HTContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    AbstractContainerScreen<T>(menu, inventory, title) {
    abstract val texture: ResourceLocation

    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        guiGraphics.blit(texture, startX, startY, 0, 0, imageWidth, imageHeight)
    }

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2

    fun createFluidWidget(
        index: Int,
        x: Int,
        y: Int,
        width: Int = 16,
        height: Int = 18 * 3 - 2,
    ): HTFluidWidget {
        val handler: IFluidHandler? = menu.getHandlerBlockEntity()?.getFluidHandler(null)
        return HTFluidWidget(
            handler?.getFluidInTank(index) ?: FluidStack.EMPTY,
            handler?.getTankCapacity(index) ?: 0,
            startX + x,
            startY + y,
            width,
            height,
        )
    }

    fun createEnergyWidget(
        key: ResourceKey<Level>,
        x: Int = HTSlotHelper.getSlotPosX(0),
        y: Int = HTSlotHelper.getSlotPosY(0),
    ): HTEnergyNetworkWidget = HTEnergyNetworkWidget(
        key,
        startX + x,
        startY + y,
        16,
        18 * 3 - 2,
    )

    /*protected fun renderFluid(guiGraphics: GuiGraphics, stack: FluidStack, slot: HTFluidSlot) {
        if (stack.isEmpty) return
        val (sprite: TextureAtlasSprite, color: Int) = stack.getSpriteAndColor()

        val minU: Float = sprite.u0
        val maxU: Float = sprite.u1
        val minV: Float = sprite.v0
        val maxV: Float = sprite.v1
        val delta: Float = maxV - minV
        val fillLevel: Float = (stack.amount / slot.capacity.toFloat()) * slot.height

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS)
        setShaderColor(color) {
            RenderSystem.enableBlend()
            val times: Int = 1 + (Mth.ceil(fillLevel) / 16)
            for (i: Int in (0 until times)) {
                val subHeight: Float = min(16f, fillLevel - (16 * i))
                val offsetY: Float = slot.height - 16 * i - subHeight
                drawQuad(
                    (startX + slot.x).toFloat(),
                    startY + slot.y + offsetY,
                    16f,
                    subHeight,
                    minU,
                    maxV - delta * (subHeight / 16),
                    maxU,
                    maxV,
                )
            }
            RenderSystem.disableBlend()
        }
    }

    protected fun renderEnergy(guiGraphics: GuiGraphics, x: Int, y: Int) {
        val network: IEnergyStorage = getEnergyNetwork(menu) ?: return
        val percentage: Float = Mth.clamp(network.energyStored / network.maxEnergyStored.toFloat(), 0f, 1f)
        val yHeight: Int = Mth.ceil(percentage * 56)
        guiGraphics.blitSprite(
            RagiumAPI.id("container/energy_gauge"),
            18,
            56,
            0,
            0,
            startX + x - 1,
            startY + y - (1 + yHeight),
            18,
            yHeight,
        )
    }

    protected fun renderTooltip(
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
        rangeX: Int = 18,
        rangeY: Int = 18,
        action: () -> Unit,
    ) {
        if (HTSlotHelper.isIn(mouseX, startX + x, rangeX) && HTSlotHelper.isIn(mouseY, startY + y, rangeY)) {
            action()
        }
    }

    protected fun renderFluidTooltip(
        guiGraphics: GuiGraphics,
        stack: FluidStack,
        capacity: Int,
        flag: TooltipFlag,
        slot: HTFluidSlot,
        mouseX: Int,
        mouseY: Int,
    ) {
        renderTooltip(slot.x, slot.y, mouseX, mouseY, rangeX = slot.width, rangeY = slot.height) {
            guiGraphics.renderComponentTooltip(
                font,
                buildList { addFluidTooltip(stack, this::add, flag) },
                mouseX,
                mouseY,
            )
        }
    }

    protected fun renderEnergyTooltip(
        guiGraphics: GuiGraphics,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        val network: IEnergyStorage = getEnergyNetwork(menu) ?: return
        renderTooltip(x, y, mouseX, mouseY, rangeY = 18 * 3) {
            guiGraphics.renderTooltip(
                font,
                energyText(network),
                mouseX,
                mouseY,
            )
        }
    }

    protected open fun getEnergyNetwork(menu: T): IEnergyStorage? = null*/
}
