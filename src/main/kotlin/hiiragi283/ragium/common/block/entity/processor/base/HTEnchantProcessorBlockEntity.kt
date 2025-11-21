package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.HTEnergizedProcessorBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTEnchantProcessorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTEnergizedProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
    lateinit var inputTank: HTVariableFluidStackTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener,
                RagiumConfig.COMMON.extractorTankCapacity,
                RagiumFluidContents.EXPERIENCE::isOf,
            ),
        )
    }

    lateinit var outputSlot: HTItemStackSlot
        protected set
}
