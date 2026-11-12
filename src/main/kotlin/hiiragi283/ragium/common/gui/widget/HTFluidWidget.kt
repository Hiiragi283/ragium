package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTCursorInteractiveWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.recipe.widget.HTGhostWidget
import hiiragi283.lib.recipe.widget.HTIngredientWidget
import hiiragi283.lib.transfer.HTResourceSlotUtil
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidView
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.util.Either
import hiiragi283.ragium.common.network.HTInteractWithCursorPayload
import net.neoforged.neoforge.client.network.ClientPacketDistributor
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.access.ItemAccess

private typealias ContentSetter = Either<(FluidStack) -> Unit, HTFluidTank>

sealed class HTFluidWidget(
    view: HTFluidView,
    private val contentSetter: ContentSetter,
    x: Int,
    y: Int,
    val backgroundType: HTBackgroundType,
    width: Int,
    height: Int,
) : HTAbstractWidget(x - 1, y - 1, width, height),
    HTCursorInteractiveWidget,
    HTGhostWidget,
    HTIngredientWidget,
    HTFluidView by view {
    val isGhost: Boolean get() = contentSetter.isLeft()

    final override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.FLUID.get()

    final override fun mouseClicked(access: HTWidget.MenuAccess, mouseX: Double, mouseY: Double, button: Int) {
        HTInteractWithCursorPayload(access.clientMenu.containerId, access.widgetHolder.getWidgetId(this)).let(ClientPacketDistributor::sendToServer)
    }

    final override fun interactWithCursor(access: ItemAccess) {
        val cursorTank: HTFluidTank = HTFluidCapabilities.getSlot(access, 0) ?: return
        contentSetter.fold(
            { cursorTank.getFluidStack().let(it) },
            { tank: HTFluidTank ->
                val cursorFrom: Int = HTResourceSlotUtil.move(cursorTank, tank, Int.MAX_VALUE, null, HTTransferAccess.MANUAL, HTTransferAccess.MANUAL)
                if (cursorFrom == 0) {
                    HTResourceSlotUtil.move(tank, cursorTank, Int.MAX_VALUE, null, HTTransferAccess.MANUAL, HTTransferAccess.MANUAL)
                }
            },
        )
    }

    final override fun getIngredient(): FluidStack = this.getFluidStack()

    final override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = contentSetter.leftOrNull()?.let {
        HTGhostWidget.FluidConsumer { stack ->
            if (stack is FluidStack) {
                it(stack)
            }
        }
    }

    //    Slot    //

    class Slot(view: HTFluidView, contentSetter: ContentSetter, x: Int, y: Int, backgroundType: HTBackgroundType) :
        HTFluidWidget(view, contentSetter, x, y, backgroundType, 18, 18) {
        constructor(
            tank: HTBasicFluidTank,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ) : this(tank, Either.Right(tank), x, y, backgroundType)

        override fun toString(): String =
            "HTFluidWidget.Slot(bounds=$bounds, stack=${this.getFluidStack()}, backgroundType=$backgroundType, isGhost=$isGhost)"
    }

    //    Tank    //

    class Tank(view: HTFluidView, contentSetter: ContentSetter, x: Int, y: Int, backgroundType: HTBackgroundType) :
        HTFluidWidget(
            view,
            contentSetter,
            x,
            y,
            backgroundType,
            18,
            18 * 3,
        ) {
        constructor(
            tank: HTBasicFluidTank,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ) : this(tank, Either.Right(tank), x, y, backgroundType)

        override fun toString(): String =
            "HTFluidWidget.Tank(bounds=$bounds, stack=${this.getFluidStack()}, backgroundType=$backgroundType, isGhost=$isGhost)"
    }
}
