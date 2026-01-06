package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.capability.HTFluidCapabilities
import hiiragi283.core.api.capability.tankRange
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.item.HTItemResourceType
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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
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

    lateinit var emptySlot: HTBasicItemSlot
        private set
    lateinit var fillSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        emptySlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                canInsert = HTFluidCapabilities::hasCapability,
            ),
        )
        fillSlot = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTBasicItemSlot.input(
                listener,
                canInsert = { resource: HTItemResourceType ->
                    val handler: IFluidHandlerItem = HTFluidCapabilities.getCapability(resource) ?: return@input false
                    for (i: Int in handler.tankRange) {
                        if (!handler.getFluidInTank(i).isEmpty) return@input false
                    }
                    true
                },
            ),
        )
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(tank)

    //    Save & Load    //

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.store(HTConst.FLUID, VanillaBiCodecs.FLUID_STACK, tank.getFluidStack())
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        (input.read(HTConst.FLUID, VanillaBiCodecs.FLUID_STACK) ?: FluidStack.EMPTY).let(tank::setStack)
    }

    //    Ticking    //

    var oldScale: Fraction = Fraction.ZERO

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // スロットから液体を搬入する
        if (HTStackSlotHelper.moveFluid(emptySlot, outputSlot::setStack, tank)) return true
        // スロットに液体を搬出する

        // 液体量の変化があれば更新させる
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

        override fun insert(
            resource: HTFluidResourceType?,
            amount: Int,
            action: HTStorageAction,
            access: HTStorageAccess,
        ): Int {
            val remainder: Int
            if (isCreative && this.getResource() == null && action.execute() && access != HTStorageAccess.EXTERNAL) {
                remainder = super.insert(resource, amount, HTStorageAction.SIMULATE, access)
                if (remainder == 0) {
                    setResource(resource)
                    setAmount(getCapacity())
                }
            } else {
                remainder = super.insert(resource, amount, action.combine(!isCreative), access)
            }
            return remainder
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
            super.extract(amount, action.combine(!isCreative), access)

        override fun getCapacity(resource: HTFluidResourceType?): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> this@HTTankBlockEntity.getCapacity()
        }

        override fun getAmount(): Int = when (isCreative) {
            true -> Int.MAX_VALUE
            false -> super.getAmount()
        }
    }
}
