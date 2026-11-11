package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.capability.HTItemCapabilities
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.widget.HTCursorInteractiveWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.recipe.widget.HTGhostWidget
import hiiragi283.lib.recipe.widget.HTIngredientWidget
import hiiragi283.lib.transfer.item.HTItemStackSlot
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.ragium.common.gui.slot.HTItemContainerSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.access.ItemAccess

private typealias ItemStackSetter = (ItemStack) -> Unit

sealed class HTItemWidget(val backgroundType: HTBackgroundType) :
    HTWidget,
    HTIngredientWidget {
    abstract fun getStack(): ItemStack

    final override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.ITEM.get()

    final override fun getIngredient(): ItemStack = getStack()

    //    Container    //

    class Container(val slot: Slot, backgroundType: HTBackgroundType) : HTItemWidget(backgroundType) {
        constructor(
            slot: HTItemStackSlot,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ) : this(HTItemContainerSlot.create(slot, x, y), backgroundType)

        constructor(
            slot: HTItemStackSlot,
            index: Int,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
        ) : this(HTItemContainerSlot.create(slot, x, y), backgroundType)

        override fun getStack(): ItemStack = slot.item

        override val bounds: HTBounds = HTBounds.createSlot(slot.x - 1, slot.y - 1)

        override fun onInit(access: HTWidget.Access) {
            access.isActive = false
        }

        override fun toString(): String = "HTItemWidget.Container(slot=$slot, backgroundType=$backgroundType)"
    }

    //    Fake    //

    class Fake(
        private val stackGetter: () -> ItemStack,
        private val stackSetter: ItemStackSetter?,
        override val bounds: HTBounds,
        backgroundType: HTBackgroundType,
        val isGhost: Boolean,
    ) : HTItemWidget(backgroundType),
        HTCursorInteractiveWidget,
        HTGhostWidget {
        constructor(
            stackGetter: () -> ItemStack,
            stackSetter: ItemStackSetter?,
            x: Int,
            y: Int,
            backgroundType: HTBackgroundType,
            isGhost: Boolean,
        ) : this(stackGetter, stackSetter, HTBounds.createSlot(x - 1, y - 1), backgroundType, isGhost)

        constructor(slot: HTItemStackSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(
            slot::getStackCopy,
            slot::setStack,
            x,
            y,
            backgroundType,
            isGhost,
        )

        override fun getStack(): ItemStack = stackGetter()

        override fun interactWithCursor(access: ItemAccess) {
            if (isGhost) {
                val cursorStack: ItemStack = HTItemCapabilities.getSlot(access, 0)?.getItemStack() ?: return
                stackSetter?.invoke(cursorStack)
            }
        }

        override fun getGhostConsumer(): HTGhostWidget.ItemConsumer = HTGhostWidget.ItemConsumer { stack: Any ->
            if (stack is ItemStack) {
                stackSetter?.invoke(stack)
            }
        }

        override fun toString(): String = "HTItemWidget.Fake(bounds=$bounds, stack=${getStack()}, backgroundType=$backgroundType, isGhost=$isGhost)"
    }
}
