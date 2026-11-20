package hiiragi283.ragium.common.block.entity.misc

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.sync.HTIntSyncSlot
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.lib.util.fixedFraction
import hiiragi283.ragium.common.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.gui.widget.HTArrowWidget
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.gui.widget.HTSolarWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.entity.FuelValues
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTBoilerBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTBaseMachineBlockEntity(type, pos, state) {
    companion object {
        @JvmStatic
        protected val WATER_RESOURCE: FluidResource by lazy { FluidResource.of(Fluids.WATER) }

        @JvmStatic
        protected val STEAM_RESOURCE: FluidResource by lazy(RagiumFluids.STEAM::asResourceOrEmpty)
    }

    protected lateinit var waterTank: HTVariableFluidTank
        private set
    protected lateinit var steamTank: HTVariableFluidTank
        private set

    protected fun createTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        waterTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(::getTankCapacity, listener, filter = { it.`is`(Tags.Fluids.WATER) }))
        steamTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(::getTankCapacity, listener))
    }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = useTransaction { transaction: Transaction ->
        useTransaction(transaction) { sub: Transaction -> if (!isBurning(level, sub)) return false }

        val waterToConsume: Int = RagiumConfig.SERVER.generator.boilerWaterPerTick.get()
        val waterExtracted: Int = waterTank.extract(WATER_RESOURCE, waterToConsume, transaction, HTTransferAccess.INTERNAL)
        if (waterExtracted <= 0) return false

        val steamToGenerate: Int = RagiumConfig.SERVER.generator.boilerSteamRate.get() * waterExtracted
        if (steamToGenerate <= 0) return false
        val steamInserted: Int = steamTank.insert(STEAM_RESOURCE, steamToGenerate, transaction, HTTransferAccess.INTERNAL)
        if (steamInserted > 0) {
            transaction.commit()
            true
        } else {
            false
        }
    }

    protected abstract fun isBurning(level: ServerLevel, transaction: Transaction): Boolean

    //    Solid    //

    class Solid(pos: BlockPos, state: BlockState) : HTBoilerBlockEntity(RagiumBlockEntityTypes.SOLID_BOILER.get(), pos, state) {
        override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
            createTanks(builder, listener)
        }

        private lateinit var fuelSlot: HTBasicItemSlot

        override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
            fuelSlot = builder.addSlot(
                HTSlotInfo.INPUT,
                HTBasicItemSlot.input(
                    listener,
                    filter = { resource: ItemResource ->
                        val fuelValue: FuelValues = this.level?.fuelValues() ?: return@input false
                        resource.toStack().getBurnTime(null, fuelValue) > 0
                    },
                ),
            )
        }

        override fun setupMenu(widgetHolder: HTWidgetHolder) {
            // progress
            widgetHolder += HTArrowWidget.Right(::progression, HTSlotHelper.getSlotPosX(3.75), HTSlotHelper.getSlotPosY(1))
            widgetHolder += HTIntSyncSlot.create(::progress)
            widgetHolder += HTIntSyncSlot.create(::maxProgress)
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
            )
            widgetHolder.track(waterTank)
            // output
            widgetHolder += HTFluidWidget.Tank(
                steamTank,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(0),
                HTBackgroundType.OUTPUT,
            )
            widgetHolder.track(steamTank)
        }

        // Boiler
        private var maxProgress: Int = 0
        private var progress: Int = 0
        private val progression: Float get() = fixedFraction(maxProgress - progress, maxProgress)

        override fun writeValue(output: ValueOutput) {
            super.writeValue(output)
            output.putInt(HTConstants.PROGRESS, progress)
            output.putInt(HTConstants.MAX_PROGRESS, maxProgress)
        }

        override fun readValue(input: ValueInput) {
            super.readValue(input)
            input.getInt(HTConstants.MAX_PROGRESS).ifPresent(::maxProgress::set)
            input.getInt(HTConstants.PROGRESS).ifPresent(::progress::set)
        }

        override fun isBurning(level: ServerLevel, transaction: Transaction): Boolean {
            if (progress <= 0) {
                val burnTime: Int = fuelSlot.resource.toStack(1).getBurnTime(null, level.fuelValues())
                if (burnTime <= 0) return false
                val extracted: Int = fuelSlot.extractSelf(1, transaction, HTTransferAccess.INTERNAL)
                if (extracted == 1) {
                    this.maxProgress = burnTime
                    this.progress = maxProgress
                    transaction.commit()
                } else {
                    return false
                }
            }
            if (maxProgress > 0) {
                progress--
                return true
            } else {
                return false
            }
        }
    }

    //    Solar    //

    class Solar(pos: BlockPos, state: BlockState) : HTBoilerBlockEntity(RagiumBlockEntityTypes.SOLAR_BOILER.get(), pos, state) {
        override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
            createTanks(builder, listener)
        }

        override fun setupMenu(widgetHolder: HTWidgetHolder) {
            // progress
            widgetHolder += HTSolarWidget(::getSunAngle, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(1))
            // inputs
            widgetHolder += HTFluidWidget.Tank(
                waterTank,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(0),
                HTBackgroundType.INPUT,
            )
            widgetHolder.track(waterTank)
            // output
            widgetHolder += HTFluidWidget.Tank(
                steamTank,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(0),
                HTBackgroundType.OUTPUT,
            )
            widgetHolder.track(steamTank)
        }

        override fun isBurning(level: ServerLevel, transaction: Transaction): Boolean = getSunAngle() > 0
    }
}
