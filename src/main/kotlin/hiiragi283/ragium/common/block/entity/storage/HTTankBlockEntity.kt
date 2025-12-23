package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

/**
 * @see mekanism.common.tile.TileEntityFluidTank
 */
open class HTTankBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.TANK, pos, state)

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
            HTSlotInfo.NONE,
            HTBasicItemSlot.create(
                listener,
                canExtract = HTStoragePredicates.manualOnly(),
                canInsert = HTStoragePredicates.manualOnly(),
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
        output.store(HTConst.FLUID, ImmutableFluidStack.CODEC, tank.getStack())
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        input.readAndSet(HTConst.FLUID, ImmutableFluidStack.CODEC, tank::setStackUnchecked)
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
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrueBi(),
            HTStoragePredicates.alwaysTrue(),
            listener,
        ) {
        private val isCreative: Boolean get() = this@HTTankBlockEntity.isCreative()

        override fun getStack(): ImmutableFluidStack? = when (isCreative) {
            true -> super.getStack()?.copyWithAmount(Int.MAX_VALUE)
            false -> super.getStack()
        }

        override fun insert(stack: ImmutableFluidStack?, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack? {
            val remainder: ImmutableFluidStack?
            if (isCreative && this.getStack() == null && action.execute() && access != HTStorageAccess.EXTERNAL) {
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
