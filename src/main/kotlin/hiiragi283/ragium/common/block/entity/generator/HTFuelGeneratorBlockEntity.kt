package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.attribute.getAttributeFront
import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidFuelItemStackSlot
import hiiragi283.ragium.common.util.HTEnergyHelper
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid

abstract class HTFuelGeneratorBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntity(blockHolder, pos, state) {
    companion object {
        @JvmStatic
        fun createSimple(
            itemValueGetter: (ImmutableItemStack) -> Int,
            fuelContent: HTFluidHolderLike,
            fluidAmountGetter: (RegistryAccess, Holder<Fluid>) -> Int,
            blockHolder: Holder<Block>,
        ): HTBlockEntityFactory<HTFuelGeneratorBlockEntity> = HTBlockEntityFactory { pos: BlockPos, state: BlockState ->
            Simple(itemValueGetter, fuelContent, fluidAmountGetter, blockHolder, pos, state)
        }
    }

    lateinit var tank: HTVariableFluidStackTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        tank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener,
                RagiumConfig.COMMON.generatorInputTankCapacity,
                filter = { stack: ImmutableFluidStack ->
                    val access: RegistryAccess = this.getRegistryAccess() ?: return@input false
                    getRequiredAmount(access, stack) > 0
                },
            ),
        )
    }

    protected lateinit var fuelSlot: HTFluidFuelItemStackSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // fuel
        fuelSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTFluidFuelItemStackSlot.create(
                tank,
                ::getFuelValue,
                ::getFuelStack,
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
            ),
        )
    }

    //    Ticking    //

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // バッテリー内の電力を正面に自動搬出させる
        val frontBattery: HTEnergyBattery? = energyCache.getBattery(level, pos, state.getAttributeFront())
        HTEnergyHelper.moveEnergy(this.battery, frontBattery, this.battery::onContentsChanged)
        // スロット内のアイテムを液体に変換する
        fuelSlot.fillOrBurn()
        // 燃料を消費して発電する
        val required: Int = getRequiredAmount(level.registryAccess(), tank.getStack())
        if (required <= 0) return false
        if (tank.extract(required, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null) return false
        battery.currentEnergyPerTick = modifyValue(HTMachineUpgrade.Key.ENERGY_GENERATION) { battery.baseEnergyPerTick * it }
        return if (battery.generate() > 0) {
            tank.extract(required, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            true
        } else {
            false
        }
    }

    protected abstract fun getFuelValue(stack: ImmutableItemStack): Int

    protected abstract fun getFuelStack(value: Int): ImmutableFluidStack?

    protected abstract fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack?): Int

    //    Simple    //

    private class Simple(
        private val itemValueGetter: (ImmutableItemStack) -> Int,
        private val fluid: HTFluidHolderLike,
        private val fluidAmountGetter: (RegistryAccess, Holder<Fluid>) -> Int,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTFuelGeneratorBlockEntity(blockHolder, pos, state) {
        override fun getFuelValue(stack: ImmutableItemStack): Int = itemValueGetter(stack)

        override fun getFuelStack(value: Int): ImmutableFluidStack? = fluid.toImmutableStack(value)

        override fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack?): Int = when (stack) {
            null -> 0
            else -> fluidAmountGetter(access, stack.holder())
        }
    }
}
