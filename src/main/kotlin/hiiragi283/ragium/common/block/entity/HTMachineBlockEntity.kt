package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.energy.battery.HTMachineEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を扱う設備に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    companion object {
        @JvmStatic
        fun getComponentTier(stack: ImmutableItemStack): HTComponentTier? {
            for ((tier: HTComponentTier, item: HTDeferredItem<*>) in RagiumItems.COMPONENTS) {
                if (stack.isOf(item)) {
                    return tier
                }
            }
            return null
        }
    }

    // Energy
    lateinit var battery: HTMachineEnergyBattery<*>
        private set

    final override fun initializeEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        battery = createBattery(builder, listener)
        return builder.build()
    }

    protected abstract fun createBattery(
        builder: HTBasicEnergyBatteryHolder.Builder,
        listener: HTContentListener,
    ): HTMachineEnergyBattery<*>

    // Item
    lateinit var upgradeSlots: List<HTItemStackSlot>
        private set

    fun getComponentTier(): HTComponentTier? = upgradeSlots[3].getStack()?.let(::getComponentTier)

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        initializeItemHandler(builder, listener)
        upgradeSlots = (0..3).map { i: Int ->
            val filter: (ImmutableItemStack) -> Boolean = when (i) {
                3 -> { stack -> getComponentTier(stack) != null }
                else -> { stack -> getComponentTier(stack) == null }
            }
            builder.addSlot(
                HTSlotInfo.CATALYST,
                HTItemStackSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(8),
                    HTSlotHelper.getSlotPosY(i - 0.5),
                    canExtract = HTPredicates.manualOnly(),
                    canInsert = HTPredicates.manualOnly(),
                    filter = filter,
                ),
            )
        }
        return builder.build()
    }

    protected abstract fun initializeItemHandler(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.putBoolean("is_active", this.isActive)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        this.isActive = input.getBoolean("is_active", false)
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

    protected fun getModifiedEnergy(base: Int): Int = getComponentTier()?.modifyProcessorRate(base) ?: base
}
