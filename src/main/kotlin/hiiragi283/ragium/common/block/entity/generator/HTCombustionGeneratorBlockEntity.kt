package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.generator.base.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFuelGeneratorBlockEntity(RagiumBlocks.COMBUSTION_GENERATOR, pos, state) {
    lateinit var coolantTank: HTBasicFluidTank
        private set
    lateinit var fuelTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // inputs
        coolantTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                blockHolder.getFluidAttribute().getFirstInputTank(),
                canInsert = RagiumDataMapTypes::getCoolantAmount.andThen { it > 0 },
            ),
        )
        fuelTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                blockHolder.getFluidAttribute().getSecondInputTank(),
                canInsert = RagiumDataMapTypes::getTimeFromCombustion.andThen { it > 0 },
            ),
        )
    }

    override fun getNewBurnTime(level: ServerLevel, pos: BlockPos): Int {
        // 冷却液が必要量あるか判定する
        if (!HTStackSlotHelper.canShrinkStack(coolantTank, RagiumDataMapTypes::getCoolantAmount, true)) return 0
        // 燃料が必要量あるか判定する
        if (!HTStackSlotHelper.canShrinkStack(fuelTank, 100, true)) return 0
        return fuelTank.getStack()?.let(RagiumDataMapTypes::getTimeFromCombustion) ?: 0
    }

    override fun onFuelUpdated(level: ServerLevel, pos: BlockPos, isSucceeded: Boolean) {
        if (isSucceeded) {
            // インプットを減らす
            HTStackSlotHelper.shrinkStack(coolantTank, RagiumDataMapTypes::getCoolantAmount, HTStorageAction.EXECUTE)
            fuelTank.extract(100, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            // SEを鳴らす
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f)
        }
    }
}
