package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.extension.isMaxCount
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction
import java.util.*

open class HTMachineInventory private constructor(
    size: Int,
    val inputs: IntArray,
    val catalyst: OptionalInt,
    val outputs: IntArray,
) : SidedInventory {
    companion object {
        @JvmStatic
        fun ofSmall(): HTMachineInventory = HTMachineInventory(2, intArrayOf(0), intArrayOf(1))

        @JvmStatic
        fun ofSimple(): HTMachineInventory = HTMachineInventory(5, intArrayOf(0, 1), 2, intArrayOf(3, 4))

        @JvmStatic
        fun ofLarge(): HTMachineInventory = HTMachineInventory(7, intArrayOf(0, 1, 2), 3, intArrayOf(4, 5, 6))
    }

    constructor(size: Int, inputs: IntArray, catalyst: Int, outputs: IntArray) : this(
        size,
        inputs,
        OptionalInt.of(catalyst),
        outputs,
    )

    constructor(size: Int, inputs: IntArray, outputs: IntArray) : this(
        size,
        inputs,
        OptionalInt.empty(),
        outputs,
    )

    protected val delegated = SimpleInventory(size)

    //    SidedInventory    //

    override fun getAvailableSlots(side: Direction): IntArray = IntArray(delegated.size()) { it }

    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean = slot in inputs

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction): Boolean = slot in outputs

    override fun size(): Int = delegated.size()

    override fun isEmpty(): Boolean = delegated.isEmpty()

    override fun getStack(slot: Int): ItemStack = delegated.getStack(slot)

    override fun removeStack(slot: Int, amount: Int): ItemStack = delegated.removeStack(slot, amount)

    override fun removeStack(slot: Int): ItemStack = delegated.removeStack(slot)

    override fun setStack(slot: Int, stack: ItemStack) {
        delegated.setStack(slot, stack)
    }

    override fun markDirty() {
        delegated.markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean = delegated.canPlayerUse(player)

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        if (slot in inputs) {
            inputs.map(::getStack).forEach { stackIn: ItemStack ->
                if (ItemStack.areItemsEqual(stack, stackIn) && stackIn.isMaxCount) {
                    return false
                }
            }
        }
        return super.isValid(slot, stack)
    }

    override fun clear() {
        delegated.clear()
    }
}
