package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.lib.HTConstants
import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.util.fixedFraction
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.FuelValues
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTSteamDynamoBlockEntity(pos: BlockPos, state: BlockState) : HTDynamoBlockEntity(RagiumBlockEntityTypes.STEAM_DYNAMO.get(), pos, state) {
    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.dynamo.steamDynamo

    private var maxProgress: Int = 0
    private var progress: Int = 0

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putInt(HTConstants.PROGRESS, progress)
        output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.getInt(HTConstants.MAX_PROGRESS).ifPresent {
            this.maxProgress = it
            input.getInt(HTConstants.PROGRESS).ifPresent(::progress::set)
        }
    }

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        output.putInt(HTConstants.PROGRESS, progress)
        output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        input.getInt(HTConstants.MAX_PROGRESS).ifPresent {
            this.maxProgress = it
            input.getInt(HTConstants.PROGRESS).ifPresent(::progress::set)
        }
    }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 周囲にエネルギーを自動搬出 TODO
        for (direction: Direction in Direction.entries) {
            EnergyHandlerUtil.move(
                handler,
                HTEnergyCapabilities.getCapability(level, pos.relative(direction), direction.opposite),
                Int.MAX_VALUE,
                null,
            )
        }
        // 進捗が0の場合，燃料スロットから燃焼時間を取得する
        if (progress <= 0) {
            val burnTime: Int = fuelSlot.getStackCopy().getBurnTime(null, level.fuelValues())
            if (burnTime <= 0) return false
            useTransaction { transaction: Transaction ->
                val extracted: Int = fuelSlot.extractSelf(1, transaction, HTTransferAccess.INTERNAL)
                if (extracted > 0) {
                    maxProgress = burnTime
                    progress = burnTime
                    transaction.commit()
                }
            }
        }
        // 進捗が0より大きい場合，発電を試みる
        if (progress > 0 && maxProgress > 0) {
            useTransaction { transaction: Transaction ->
                val waterPerTick: Int = RagiumConfig.SERVER.dynamo.steamDynamoWaterPerTick.get()
                val waterExtracted: Int = waterTank.extractSelf(waterPerTick, transaction, HTTransferAccess.INTERNAL)
                if (waterExtracted != waterPerTick) return false
                val energyGenerated: Int = generateEnergy(transaction)
                if (energyGenerated > 0) {
                    progress--
                    transaction.commit()
                    return true
                }
            }
        }
        return false
    }

    private lateinit var waterTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        waterTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(getTankCapacity(), listener, filter = { it.`is`(Tags.Fluids.WATER) }),
        )
    }

    private lateinit var fuelSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        fuelSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { resource: ItemResource ->
                    if (resource.isEmpty) return@input true
                    val fuelValue: FuelValues = this.level?.fuelValues() ?: return@input false
                    resource.toStack().getBurnTime(null, fuelValue) > 0
                },
            ),
        )
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        widgetHolder += HTProgressWidget(
            { fixedFraction(maxProgress - progress, maxProgress) },
            HTSlotHelper.getSlotPosX(3.75),
            HTSlotHelper.getSlotPosY(1),
        )
        // inputs
        widgetHolder += HTItemWidget.Container(
            fuelSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(fuelSlot)
        widgetHolder += HTFluidWidget.Tank(
            waterTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(waterTank)
        // output
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
    }
}
