package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getClientExtensions
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.text.addFluidTooltip
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTFluidTankWidget(
    private val levelGetter: (HTFluidWidget) -> Float,
    private val background: (GuiGraphics, HTBounds) -> Unit,
    private val tank: HTStackView.Mutable<ImmutableFluidStack>,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTFluidWidget {
    companion object {
        @JvmStatic
        fun createSlot(view: HTStackView.Mutable<ImmutableFluidStack>, x: Int, y: Int): HTFluidTankWidget = HTFluidTankWidget(
            { 1f },
            { _, _ -> },
            view,
            x,
            y,
            16,
            16,
        )

        @JvmStatic
        fun createTank(view: HTStackView.Mutable<ImmutableFluidStack>, x: Int, y: Int): HTFluidTankWidget = HTFluidTankWidget(
            { widget: HTFluidWidget -> widget.getStoredLevelAsFloat(widget.getStack()) },
            { guiGraphics: GuiGraphics, bounds: HTBounds ->
                guiGraphics.blit(
                    HTFluidWidget.TANK_ID,
                    bounds.x - 1,
                    bounds.y - 1,
                    0f,
                    0f,
                    bounds.width + 2,
                    bounds.height + 2,
                    bounds.width + 2,
                    bounds.height + 2,
                )
            },
            view,
            x,
            y,
            16,
            18 * 3 - 2,
        )
    }

    override fun shouldRender(): Boolean = getStack().isNotEmpty()

    override fun getSprite(): TextureAtlasSprite? =
        getSprite(getStack().getClientExtensions().getStillTexture(getStack().stack), InventoryMenu.BLOCK_ATLAS)

    override fun getColor(): Int = getStack().getTintColor()

    override fun getLevel(): Float = levelGetter(this)

    override fun collectTooltips(consumer: (Component) -> Unit, flag: TooltipFlag) {
        addFluidTooltip(tank.getStack(), consumer, flag, true)
    }

    override fun renderBackground(guiGraphics: GuiGraphics) {
        background(guiGraphics, getBounds())
    }

    //    HTFluidWidgetNew    //

    override fun getStack(): ImmutableFluidStack = tank.getStack()

    override fun setStack(stack: ImmutableFluidStack) {
        tank.setStack(stack)
    }

    override fun getCapacityAsInt(stack: ImmutableFluidStack): Int = tank.getCapacityAsInt(stack)
}
