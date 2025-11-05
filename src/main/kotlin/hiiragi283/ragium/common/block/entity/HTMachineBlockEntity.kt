package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.energy.battery.HTMachineEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storage.item.HTMachineUpgradeItemHandler
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Consumer

/**
 * 電力を扱う設備に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    lateinit var battery: HTMachineEnergyBattery<*>
        private set

    override fun initializeEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        battery = createBattery(builder, listener)
        return builder.build()
    }

    protected abstract fun createBattery(
        builder: HTBasicEnergyBatteryHolder.Builder,
        listener: HTContentListener,
    ): HTMachineEnergyBattery<*>

    val upgradeHandler: HTMachineUpgradeItemHandler get() = getData(RagiumAttachmentTypes.MACHINE_UPGRADE)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.putBoolean("is_active", this.isActive)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        this.isActive = input.getBoolean("is_active", false)
    }

    override fun dropInventory(consumer: Consumer<ImmutableItemStack>) {
        super.dropInventory(consumer)
        upgradeHandler.getItemSlots(upgradeHandler.getItemSideFor()).mapNotNull(HTItemSlot::getStack).forEach(consumer)
    }

    //    Ticking    //

    var isActive: Boolean = false
        protected set

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val result: Boolean = onUpdateMachine(level, pos, state)
        // 以前の結果と異なる場合は強制的に同期させる
        if (result != this.isActive) {
            isActive = result
            return true
        }
        return result
    }

    protected abstract fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean

    //    Energy Storage    //

    protected fun getModifiedEnergy(base: Int): Int = upgradeHandler.getTier()?.modifyProcessorRate(base) ?: base
}
