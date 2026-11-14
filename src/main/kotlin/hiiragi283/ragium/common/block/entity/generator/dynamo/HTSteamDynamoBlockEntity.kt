package hiiragi283.ragium.common.block.entity.generator.dynamo

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.fluid.RagiumFluids
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
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTSteamDynamoBlockEntity(pos: BlockPos, state: BlockState) : HTDynamoBlockEntity(RagiumBlockEntityTypes.STEAM_DYNAMO.get(), pos, state) {
    override fun getMaxProgress(level: ServerLevel, pos: BlockPos, state: BlockState, transaction: TransactionContext): Int {
        val resourceIn: FluidResource = fuelTank.resource
        if (!RagiumFluids.STEAM.isOf(resourceIn)) return 0
        val tickPerMb: Int = RagiumConfig.SERVER.generator.steamDynamoTickPerMb.get()
        return fuelTank.extract(resourceIn, FluidType.BUCKET_VOLUME, transaction, HTTransferAccess.INTERNAL) * tickPerMb
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.generator.steamDynamo

    private lateinit var fuelTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        fuelTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(getTankCapacity(), listener, filter = RagiumFluids.STEAM::isOf),
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
        )
        widgetHolder.track(fuelTank)
        // output
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
    }
}
