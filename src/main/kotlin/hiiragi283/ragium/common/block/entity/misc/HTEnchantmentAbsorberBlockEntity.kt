package hiiragi283.ragium.common.block.entity.misc

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.HTBlockEntityWithMenu
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.gui.widget.HTEnchantmentWidget
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.ModConfigSpec
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTEnchantmentAbsorberBlockEntity(pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(RagiumBlockEntityTypes.ENCHANTMENT_ABSORBER.get(), pos, state),
    HTBlockEntityWithMenu {
    companion object {
        @JvmStatic
        private val resource: FluidResource by lazy { RagiumFluids.ENCHANTED_EXPERIENCE.asResourceOrEmpty() }
    }
    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (this.ticks % 20 == 0) {
            val power: Int = HTEnchantmentWidget.collectPower(level, pos).toInt()
            return power >= 0 && useTransaction { transaction: Transaction ->
                val extracted: Int = inputTank.extractSelf(power, transaction, HTTransferAccess.INTERNAL)
                if (extracted != power) return@useTransaction false
                val inserted = outputTank.insert(resource, power, transaction, HTTransferAccess.INTERNAL)
                if (inserted == power) {
                    transaction.commit()
                    true
                } else {
                    false
                }
            }
        } else {
            return false
        }
    }

    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(getTankCapacity(), listener, filter = RagiumFluids.EXPERIENCE::isOfTag),
        )
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
        return builder.build()
    }

    private fun getTankCapacity(): ModConfigSpec.IntValue = RagiumConfig.SERVER.enchantmentAbsorberTankCapacity

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        widgetHolder += HTEnchantmentWidget(
            { HTEnchantmentWidget.collectPower(level, blockPos) },
            HTSlotHelper.getSlotPosX(3.5),
            HTSlotHelper.getSlotPosY(2),
        )
        // progress
        widgetHolder += HTProgressWidget({ 0f }, HTSlotHelper.getSlotPosX(3.75), HTSlotHelper.getSlotPosY(1))
        // inputs
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }
}
