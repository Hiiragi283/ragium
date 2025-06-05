package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.apply

class HTMachineInput private constructor(
    val pos: BlockPos?,
    private val slotTable: HTTable<HTStorageIO, Int, HTItemSlot>,
    private val tankTable: HTTable<HTStorageIO, Int, HTFluidTank>,
) : RecipeInput {
    fun getSlotOrNull(storageIO: HTStorageIO, index: Int): HTItemSlot? = slotTable.get(storageIO, index)

    fun getTankOrNull(storageIO: HTStorageIO, index: Int): HTFluidTank? = tankTable.get(storageIO, index)

    fun getSlot(storageIO: HTStorageIO, index: Int): HTItemSlot =
        getSlotOrNull(storageIO, index) ?: error("Missing ${storageIO.name} slot for index: $index!")

    fun getTank(storageIO: HTStorageIO, index: Int): HTFluidTank =
        getTankOrNull(storageIO, index) ?: error("Missing ${storageIO.name} tank for index: $index!")

    fun getItemStack(storageIO: HTStorageIO, index: Int): ItemStack = getSlotOrNull(storageIO, index)?.stack ?: ItemStack.EMPTY

    fun getFluidStack(storageIO: HTStorageIO, index: Int): FluidStack = getTankOrNull(storageIO, index)?.stack ?: FluidStack.EMPTY

    //    RecipeInput    //

    @Deprecated("Use getItemStack(HTStorageIO, Int) instead of this")
    override fun getItem(index: Int): ItemStack = getSlotOrNull(HTStorageIO.INPUT, index)?.stack ?: ItemStack.EMPTY

    @Deprecated("Not used")
    override fun size(): Int = slotTable.row(HTStorageIO.INPUT).size

    @Deprecated("Not used")
    override fun isEmpty(): Boolean = slotTable.isEmpty() && tankTable.isEmpty()

    //    Builder    //

    companion object {
        @JvmStatic
        fun create(pos: BlockPos?, builderAction: Builder.() -> Unit): HTMachineInput = Builder(pos).apply(builderAction).build()
    }

    class Builder internal constructor(val pos: BlockPos?) {
        private val slotTable: HTTable.Mutable<HTStorageIO, Int, HTItemSlot> = mutableTableOf()
        private val tankTable: HTTable.Mutable<HTStorageIO, Int, HTFluidTank> = mutableTableOf()

        fun addInput(index: Int, slot: HTItemSlot): Builder = apply {
            slotTable.put(HTStorageIO.INPUT, index, slot)
        }

        fun addInput(index: Int, tank: HTFluidTank): Builder = apply {
            tankTable.put(HTStorageIO.INPUT, index, tank)
        }

        fun addCatalyst(slot: HTItemSlot): Builder = apply {
            slotTable.put(HTStorageIO.CATALYST, 0, slot)
        }

        fun addOutput(index: Int, slot: HTItemSlot): Builder = apply {
            slotTable.put(HTStorageIO.OUTPUT, index, slot)
        }

        fun addOutput(index: Int, tank: HTFluidTank): Builder = apply {
            tankTable.put(HTStorageIO.OUTPUT, index, tank)
        }

        fun build(): HTMachineInput = HTMachineInput(pos, slotTable, tankTable)
    }
}
