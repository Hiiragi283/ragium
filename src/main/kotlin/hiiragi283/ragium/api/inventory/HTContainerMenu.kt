package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.extension.getBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerCopySlot
import java.util.function.Supplier

abstract class HTContainerMenu(
    menuType: Supplier<out MenuType<*>>,
    containerId: Int,
    val playerInv: Inventory,
    val itemHandler: IItemHandler,
    val access: ContainerLevelAccess,
) : AbstractContainerMenu(menuType.get(), containerId) {
    val player: Player = playerInv.player
    val level: Level
        get() = player.level()
    val blockEntity: BlockEntity?
        get() = access.getBlockEntity()

    override fun stillValid(player: Player): Boolean = true

    abstract val inputSlots: IntRange
    abstract val outputSlots: IntRange

    private val playerStartIndex: Int
        get() = outputSlots.last + 1

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[index]
        if (slotIn.hasItem()) {
            val stackIn: ItemStack = slotIn.item
            result = stackIn.copy()
            when {
                index in inputSlots -> {
                    if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                index in outputSlots -> {
                    if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                else -> {
                    if (moveItemStackTo(stackIn, inputSlots.first, inputSlots.last + 1, false)) {
                        if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, false)) {
                            return ItemStack.EMPTY
                        }
                    }
                }
            }

            if (stackIn.isEmpty) {
                slotIn.setByPlayer(ItemStack.EMPTY)
            } else {
                slotIn.setChanged()
            }

            if (stackIn.count == result.count) {
                return ItemStack.EMPTY
            }
            slotIn.onTake(player, stackIn)
            if (index == 0) {
                player.drop(stackIn, false)
            }
        }
        return result
    }

    //    Extensions    //

    val itemSlots: MutableList<Pair<Int, Int>> = mutableListOf()
    val fluidSlots: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()

    private fun getSlotPosX(index: Int): Int = 8 + index * 18

    private fun getSlotPosY(index: Int): Int = 18 + index * 18

    protected fun addSlot(index: Int, x: Int, y: Int) {
        addSlot(ItemHandlerCopySlot(itemHandler, index, getSlotPosX(x), getSlotPosY(y)))
        itemSlots.add(x to y)
    }

    protected fun addOutputSlot(index: Int, x: Int, y: Int) {
        addSlot(ItemHandlerCopySlot(itemHandler, index, getSlotPosX(x), getSlotPosY(y)))
        itemSlots.add(x to y)
    }

    protected fun addFluidSlot(index: Int, x: Int, y: Int) {
        fluidSlots.put(index, x to y)
    }

    protected fun addPlayerInv(yOffset: Int = 0) {
        // inventory
        (0..26).forEach { index: Int ->
            addSlot(Slot(playerInv, index + 9, getSlotPosX(index % 9), getSlotPosY(3 + (index / 9)) + 12 + yOffset))
        }
        // hotbar
        (0..8).forEach { index: Int ->
            addSlot(Slot(playerInv, index, getSlotPosX(index), getSlotPosY(7) - 2 + yOffset))
        }
    }
}
