package hiiragi283.ragium.common.block.entity.device

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTPlanterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(RagiumBlockEntityTypes.PLANTER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank =
            builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTVariableFluidTank.input(listener, getTankCapacity()))
    }

    private lateinit var plantSlot: HTBasicItemSlot
    private lateinit var soilSlot: HTBasicItemSlot
    private lateinit var cropSlot: HTBasicItemSlot
    private lateinit var seedSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        plantSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, limit = 1))
        soilSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener, limit = 1))
        cropSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        seedSlot = builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Processing    //

    override fun createHandler(): HTRecipeHandler<*, *> {
        TODO("Not yet implemented")
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.device.planter
}
