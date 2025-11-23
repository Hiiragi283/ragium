package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.energy.HTEnergyCache
import hiiragi283.ragium.common.storage.energy.battery.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTEnergyItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

sealed class HTEnergyNetworkAccessBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(blockHolder, pos, state) {
    lateinit var battery: HTEnergyBattery
        private set

    final override fun initializeEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
        battery = builder.addSlot(HTSlotInfo.BOTH, createBattery(listener))
    }

    protected abstract fun createBattery(listener: HTContentListener): HTEnergyBattery

    private lateinit var fillSlot: HTEnergyItemStackSlot
    private lateinit var drainSlot: HTEnergyItemStackSlot

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // extract
        fillSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTEnergyItemStackSlot.fill(this.battery, listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1)),
        )
        // insert
        drainSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTEnergyItemStackSlot.drain(this.battery, listener, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1)),
        )
    }

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 左のスロットから電力を吸い取る
        val doInserted: Boolean = fillSlot.fillBattery()
        // 右のスロットに電力を渡す
        val doDrained: Boolean = drainSlot.drainBattery()
        return doInserted || doDrained
    }

    override fun getTickRate(): Int = 1

    protected abstract val transferRate: Int

    //    Creative    //

    class Creative(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlocks.CEU, pos, state) {
        private val energyCache: HTEnergyCache = HTEnergyCache()

        override fun createBattery(listener: HTContentListener): HTEnergyBattery =
            object : HTEnergyBattery, HTContentListener.Empty, HTValueSerializable.Empty {
                override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

                override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

                override fun getAmount(): Int = 0

                override fun getCapacity(): Int = Int.MAX_VALUE
            }

        override val transferRate: Int = Int.MAX_VALUE

        override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
            for (direction: Direction in Direction.entries) {
                energyCache.getBattery(level, pos, direction)?.insert(Int.MAX_VALUE, HTStorageAction.EXECUTE, HTStorageAccess.EXTERNAL)
            }
            return super.onUpdateMachine(level, pos, state)
        }
    }

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlocks.ENI, pos, state) {
        override fun createBattery(listener: HTContentListener): HTEnergyBattery =
            HTEnergyBatteryWrapper { RagiumPlatform.INSTANCE.getEnergyNetwork(this.getLevel()) }

        override val transferRate: Int = 1000

        override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
            HTStackSlotHelper.calculateRedstoneLevel(battery)
    }
}
