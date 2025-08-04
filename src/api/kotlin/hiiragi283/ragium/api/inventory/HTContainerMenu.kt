package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class HTContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    val pos: BlockPos,
) : AbstractContainerMenu(menuType.get(), containerId) {
    @JvmField
    val player: Player = inventory.player
    val level: Level get() = player.level()

    override fun stillValid(player: Player): Boolean = true

    abstract val inputSlots: IntRange
    abstract val outputSlots: IntRange

    val playerStartIndex: Int get() = outputSlots.last + 1

    final override fun quickMoveStack(player: Player, index: Int): ItemStack {
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

    val fluidSlots: MutableMap<Int, HTFluidSlot> = mutableMapOf()

    protected fun addFluidSlot(
        index: Int,
        x: Int,
        y: Int,
        width: Int = 16,
        height: Int = 16,
        capacity: Int = RagiumAPI.getConfig().getDefaultTankCapacity(),
    ) {
        fluidSlots.put(index, HTFluidSlot(x, y, width, height, capacity))
    }

    protected fun addPlayerInv(inventory: Inventory, yOffset: Int = 0, immovable: Boolean = false) {
        // inventory
        for (index: Int in 0..26) {
            addSlot(
                Slot(
                    inventory,
                    index + 9,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(3 + (index / 9)) + 12 + yOffset,
                ),
            )
        }
        // hotbar
        for (index: Int in 0..8) {
            addSlot(
                when {
                    immovable && index == inventory.selected -> ::HTImmovableSlot
                    else -> ::Slot
                }(inventory, index, HTSlotHelper.getSlotPosX(index), HTSlotHelper.getSlotPosY(7) - 2 + yOffset),
            )
        }
    }

    companion object {
        @JvmStatic
        protected fun decodePos(registryBuf: RegistryFriendlyByteBuf?): BlockPos =
            registryBuf?.let(BlockPos.STREAM_CODEC::decode) ?: BlockPos.ZERO
    }
}
