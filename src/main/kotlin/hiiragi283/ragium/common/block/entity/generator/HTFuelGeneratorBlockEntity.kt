package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTFluidFuelItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
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
            fuelContent: HTFluidContent<*, *, *>,
            fluidAmountGetter: (RegistryAccess, Holder<Fluid>) -> Int,
            blockHolder: Holder<Block>,
        ): HTBlockEntityFactory<HTFuelGeneratorBlockEntity> = HTBlockEntityFactory { pos: BlockPos, state: BlockState ->
            Simple(itemValueGetter, fuelContent, fluidAmountGetter, blockHolder, pos, state)
        }
    }

    protected lateinit var tank: HTVariableFluidStackTank
        private set

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        tank = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTVariableFluidStackTank.input(
                listener,
                RagiumConfig.COMMON.generatorInputTankCapacity,
                filter = { stack: ImmutableFluidStack ->
                    val access: RegistryAccess = level?.registryAccess() ?: return@input false
                    getRequiredAmount(access, stack) > 0
                },
            ),
        )
        return builder.build()
    }

    protected lateinit var fuelSlot: HTFluidFuelItemStackSlot
        private set
    private lateinit var outputSlot: HTItemSlot.Mutable

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        // fuel
        fuelSlot = builder.addSlot(
            HTAccessConfig.INPUT_ONLY,
            HTFluidFuelItemStackSlot.create(
                tank,
                ::getFuelValue,
                ::getFuelStack,
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(0),
            ),
        )
        outputSlot = builder.addSlot(
            HTAccessConfig.OUTPUT_ONLY,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
        return builder.build()
    }

    //    Ticking    //

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // スロット内のアイテムを液体に変換する
        fuelSlot.fillOrBurn(outputSlot)
        // 燃料を消費して発電する
        val required: Int = getRequiredAmount(level.registryAccess(), tank.getStack())
        if (required <= 0) return false
        if (tank.extract(required, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null) return false
        val usage: Int = getModifiedEnergy(energyUsage)
        return if (energyStorage.insertEnergy(usage, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) > 0) {
            tank.extract(required, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            energyStorage.insertEnergy(usage, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
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
        private val fuelContent: HTFluidContent<*, *, *>,
        private val fluidAmountGetter: (RegistryAccess, Holder<Fluid>) -> Int,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTFuelGeneratorBlockEntity(blockHolder, pos, state) {
        override fun getFuelValue(stack: ImmutableItemStack): Int = itemValueGetter(stack)

        override fun getFuelStack(value: Int): ImmutableFluidStack? = fuelContent.toStorageStack(value)

        override fun getRequiredAmount(access: RegistryAccess, stack: ImmutableFluidStack?): Int = when (stack) {
            null -> 0
            else -> fluidAmountGetter(access, stack.holder())
        }
    }
}
