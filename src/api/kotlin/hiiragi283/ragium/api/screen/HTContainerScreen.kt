package hiiragi283.ragium.api.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.extension.getSpriteAndColor
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.extension.toFloatColor
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidStack

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<T : HTContainerMenu>(menu: T, inventory: Inventory, title: Component) :
    AbstractContainerScreen<T>(menu, inventory, title) {
    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2

    protected fun renderFluid(
        guiGraphics: GuiGraphics,
        stack: FluidStack,
        x: Int,
        y: Int,
    ) {
        if (stack.isEmpty) return
        val (sprite: TextureAtlasSprite, color: Int) = stack.getSpriteAndColor()
        val floatColor: Triple<Float, Float, Float> = toFloatColor(color)

        guiGraphics.blit(
            startX + x,
            startY + y,
            0,
            sprite.contents().width(),
            sprite.contents().height(),
            sprite,
            floatColor.first,
            floatColor.second,
            floatColor.third,
            1.0f,
        )
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
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int,
    ) {
        renderTooltip(x, y, mouseX, mouseY) {
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
                Component.translatable(
                    RagiumTranslationKeys.TOOLTIP_ENERGY_PERCENTAGE,
                    intText(network.energyStored),
                    intText(network.maxEnergyStored),
                ),
                mouseX,
                mouseY,
            )
        }
    }

    protected open fun getEnergyNetwork(menu: T): IEnergyStorage? = null
}
