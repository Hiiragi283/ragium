package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTMagmaticDynamoBlockEntity(pos: BlockPos, state: BlockState) :
    HTDynamoBlockEntity.Progressed(RagiumBlockEntityTypes.MAGMATIC_DYNAMO.get(), pos, state) {
    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.dynamo.magmaticDynamo

    override fun getMaxProgress(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: Transaction): Int {
        val resourceIn: FluidResource = fuelTank.resource
        if (resourceIn.isEmpty) return 0
        val tickPerMb: Int = resourceIn.getData(RagiumDataMaps.MAGMATIC_FUEL)?.tickPerMb ?: return 0
        return fuelTank.extract(resourceIn, FluidType.BUCKET_VOLUME, transaction, HTTransferAccess.INTERNAL) * tickPerMb
    }

    private lateinit var fuelTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        fuelTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(getTankCapacity(), listener),
        )
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTFluidWidget.Tank(
            fuelTank,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(fuelTank)
        // output
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
    }
}
