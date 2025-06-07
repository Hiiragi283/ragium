package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.SlotItemHandler

abstract class HTContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    val inventory: Inventory,
    val pos: BlockPos,
) : AbstractContainerMenu(menuType.get(), containerId) {
    @JvmField
    val player: Player = inventory.player
    val level: Level get() = player.level()

    override fun stillValid(player: Player): Boolean = true

    abstract val inputSlots: IntRange
    abstract val outputSlots: IntRange

    private val playerStartIndex: Int get() = outputSlots.last + 1

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        var result: ItemStack = ItemStack.EMPTY
        val slotIn: Slot = slots[index]
        if (slotIn.hasItem()) {
            val stackIn: ItemStack = slotIn.item
            result = stackIn.copy()
            when (index) {
                in inputSlots -> {
                    if (!moveItemStackTo(stackIn, playerStartIndex, playerStartIndex + 36, true)) {
                        return ItemStack.EMPTY
                    }
                }

                in outputSlots -> {
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

    val fluidSlots: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()

    protected fun addSlot(
        handler: IItemHandler,
        index: Int,
        x: Double,
        y: Double,
    ) {
        addSlot(SlotItemHandler(handler, index, HTSlotPos.getSlotPosX(x), HTSlotPos.getSlotPosY(y)))
    }

    protected fun addFluidSlot(index: Int, x: Int, y: Int) {
        fluidSlots.put(index, x to y)
    }

    protected fun addPlayerInv(yOffset: Int = 0, immovable: Boolean = false) {
        // inventory
        for (index: Int in 0..26) {
            addSlot(
                Slot(
                    inventory,
                    index + 9,
                    HTSlotPos.getSlotPosX(index % 9),
                    HTSlotPos.getSlotPosY(3 + (index / 9)) + 12 + yOffset,
                ),
            )
        }
        // hotbar
        for (index: Int in 0..8) {
            addSlot(
                when {
                    immovable && index == inventory.selected -> ::HTImmovableSlot
                    else -> ::Slot
                }(inventory, index, HTSlotPos.getSlotPosX(index), HTSlotPos.getSlotPosY(7) - 2 + yOffset),
            )
        }
    }

    companion object {
        @JvmStatic
        protected fun decodePos(registryBuf: RegistryFriendlyByteBuf?): BlockPos =
            registryBuf?.let(BlockPos.STREAM_CODEC::decode) ?: BlockPos.ZERO

        @JvmStatic
        protected fun emptySlot(): HTItemSlot = HTItemSlot.create("")

        @JvmStatic
        protected fun emptyUpgrades(): IItemHandler = ItemStackHandler(4)
    }
}
