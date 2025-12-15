package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 */
open class HTTankBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(blockHolder, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlocks.TANK, pos, state)

    lateinit var tank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        tank = builder.addSlot(HTSlotInfo.BOTH, TankFluidTank(listener))
    }

    protected fun getCapacity(): Int = HTUpgradeHelper.getFluidCapacity(this, RagiumConfig.COMMON.tankCapacity.asInt)

    lateinit var slot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        slot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTBasicItemSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
                canExtract = HTPredicates.manualOnly(),
                canInsert = HTPredicates.manualOnly(),
            ),
        )
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(tank)

    //    Save & Load    //

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.store(RagiumConst.FLUID, ImmutableFluidStack.CODEC, tank.getStack())
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        input.readAndSet(RagiumConst.FLUID, ImmutableFluidStack.CODEC, tank::setStackUnchecked)
    }

    //    Ticking    //

    var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (HTStackSlotHelper.moveFluid(slot, slot::setStackUnchecked, tank)) return true
        val scale: Fraction = tank.getStoredLevel()
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }

    //    TankFluidTank    //

    /**
     * @see mekanism.common.capabilities.fluid.FluidTankFluidTank
     */
    protected inner class TankFluidTank(listener: HTContentListener) :
        HTBasicFluidTank(
            getCapacity(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.alwaysTrue(),
            listener,
        ) {
        private val isCreative: Boolean get() = this@HTTankBlockEntity.isCreative()

        override fun getStack(): ImmutableFluidStack? = when (isCreative) {
            true -> super.getStack()?.copyWithAmount(Int.MAX_VALUE)
            false -> super.getStack()
        }

        override fun insert(stack: ImmutableFluidStack?, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? {
            val remainder: ImmutableFluidStack?
            if (isCreative && this.getStack() == null && action.execute && access != HTStorageAccess.EXTERNAL) {
                remainder = super.insert(stack, HTStorageAction.SIMULATE, access)
                if (remainder == null) {
                    setStackUnchecked(stack?.copyWithAmount(getCapacity()))
                }
            } else {
                remainder = super.insert(stack, action.combine(!isCreative), access)
            }
            return remainder
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? =
            super.extract(amount, action.combine(!isCreative), access)

        override fun getCapacity(stack: ImmutableFluidStack?): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> this@HTTankBlockEntity.getCapacity()
        }
    }
}
