package hiiragi283.ragium.gui.widget

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.recipe.widget.HTGhostWidget
import hiiragi283.lib.recipe.widget.HTIngredientWidget
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.gui.HTContainerItemSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

private typealias ItemStackSetter = (ItemStack) -> Unit

sealed class HTItemWidget(val backgroundType: HTBackgroundType) :
    HTWidget,
    HTIngredientWidget {
    abstract fun getStack(): ItemStack

    final override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.ITEM

    final override fun getIngredient(): ItemStack = getStack()

    //    Container    //

    class Container(val slot: Slot, backgroundType: HTBackgroundType) : HTItemWidget(backgroundType) {
        constructor(slot: HTBasicItemSlot, index: Int, x: Int, y: Int, backgroundType: HTBackgroundType) : this(HTContainerItemSlot.create(slot, index, x, y, backgroundType), backgroundType)

        override fun getStack(): ItemStack = slot.item

        override val bounds: HTBounds = HTBounds.createSlot(slot.x - 1, slot.y - 1)

        override fun onInit(access: HTWidget.Access) {
            access.isActive = false
        }
    }

    //    Fake    //

    class Fake(private val stackGetter: () -> ItemStack, private val stackSetter: ItemStackSetter?, override val bounds: HTBounds, backgroundType: HTBackgroundType, val isGhost: Boolean) :
        HTItemWidget(backgroundType),
        HTGhostWidget {
        constructor(stackGetter: () -> ItemStack, stackSetter: ItemStackSetter?, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(stackGetter, stackSetter, HTBounds.createSlot(x - 1, y - 1), backgroundType, isGhost)

        constructor(slot: HTBasicItemSlot, bounds: HTBounds, backgroundType: HTBackgroundType, isGhost: Boolean) : this(slot::stack, slot::stack::set, bounds, backgroundType, isGhost)

        constructor(slot: HTBasicItemSlot, x: Int, y: Int, backgroundType: HTBackgroundType, isGhost: Boolean) : this(slot, HTBounds.createSlot(x - 1, y - 1), backgroundType, isGhost)

        override fun getStack(): ItemStack = stackGetter()

        override fun mouseClicked(access: HTWidget.Access, mouseX: Double, mouseY: Double, button: Int) {
            if (isGhost) {
                stackSetter?.invoke(access.carried.copy())
            }
        }

        override fun getGhostConsumer(): HTGhostWidget.ItemConsumer = HTGhostWidget.ItemConsumer { stack: Any ->
            if (stack is ItemStack) {
                stackSetter?.invoke(stack)
            }
        }
    }
}
