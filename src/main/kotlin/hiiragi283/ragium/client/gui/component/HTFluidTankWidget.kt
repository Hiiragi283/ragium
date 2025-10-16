package hiiragi283.ragium.client.gui.component

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.getClientExtensions
import hiiragi283.ragium.api.storage.fluid.getTintColor
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
    private val tank: HTFluidTank.Mutable,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTSpriteWidget(x, y, width, height, Component.empty()),
    HTFluidWidget {
    companion object {
        @JvmStatic
        fun createSlot(tank: HTFluidTank.Mutable, x: Int, y: Int): HTFluidTankWidget = HTFluidTankWidget(
            { 1f },
            { _, _ -> },
            tank,
            x,
            y,
            16,
            16,
        )

        @JvmStatic
        fun createTank(tank: HTFluidTank.Mutable, x: Int, y: Int): HTFluidTankWidget = HTFluidTankWidget(
            { widget: HTFluidWidget ->
                val capacity: Long = widget.getCapacity()
                when {
                    capacity <= 0 -> 0f
                    else -> widget.getStack().amountAsLong() / capacity.toFloat()
                }
            },
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
            tank,
            x,
            y,
            16,
            18 * 3 - 2,
        )
    }

    override fun shouldRender(): Boolean = !getStack().isEmpty()

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

    override fun getCapacity(): Long = tank.getCapacityAsLong(getStack())
}
