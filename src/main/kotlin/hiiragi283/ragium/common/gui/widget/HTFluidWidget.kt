package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.recipe.widget.HTGhostWidget
import hiiragi283.lib.recipe.widget.HTIngredientWidget
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidView
import hiiragi283.lib.transfer.fluid.getFluidStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidUtil

private typealias FluidStackSetter = (FluidStack) -> Unit

sealed class HTFluidWidget(
    view: HTFluidView,
    private val stackSetter: FluidStackSetter?,
    x: Int,
    y: Int,
    val backgroundType: HTBackgroundType,
    width: Int,
    height: Int,
    val isGhost: Boolean
) : HTAbstractWidget(x - 1, y - 1, width, height),
    HTGhostWidget,
    HTIngredientWidget,
    HTFluidView by view {
    final override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.FLUID

    final override fun mouseClicked(access: HTWidget.Access, mouseX: Double, mouseY: Double, button: Int) {
        if (isGhost) {
            stackSetter?.invoke(FluidUtil.getFirstStackContained(access.carried))
        }
    }

    final override fun getIngredient(): FluidStack = this.getFluidStack()

    final override fun getGhostConsumer(): HTGhostWidget.GhostIngredientConsumer? = when {
        isGhost -> HTGhostWidget.FluidConsumer { stack ->
            if (stack is FluidStack) {
                stackSetter?.invoke(stack)
            }
        }

        else -> null
    }

    //    Slot    //

    class Slot(
        view: HTFluidView,
        stackSetter: FluidStackSetter?,
        x: Int,
        y: Int,
        backgroundType: HTBackgroundType,
        isGhost: Boolean
    ) : HTFluidWidget(view, stackSetter, x, y, backgroundType, 18, 18, isGhost) {
        constructor(
            tank: HTBasicFluidTank,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
            isGhost: Boolean
        ) : this(tank, tank::setStack, x, y, backgroundType, isGhost)

        override fun toString(): String =
            "HTFluidWidget.Slot(bounds=$bounds, stack=${this.getFluidStack()}, backgroundType=$backgroundType, isGhost=$isGhost)"
    }

    //    Tank    //

    class Tank(
        view: HTFluidView,
        stackSetter: FluidStackSetter?,
        x: Int,
        y: Int,
        backgroundType: HTBackgroundType,
        isGhost: Boolean
    ) : HTFluidWidget(
        view,
        stackSetter,
        x,
        y,
        backgroundType,
        18,
        18 * 3,
        isGhost
    ) {
        constructor(
            tank: HTBasicFluidTank,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
            isGhost: Boolean
        ) : this(tank, tank::setStack, x, y, backgroundType, isGhost)

        override fun toString(): String =
            "HTFluidWidget.Tank(bounds=$bounds, stack=${this.getFluidStack()}, backgroundType=$backgroundType, isGhost=$isGhost)"
    }
}
