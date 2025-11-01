package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.energy.battery.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import kotlin.math.min

sealed class HTEnergyNetworkAccessBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(blockHolder, pos, state) {
    lateinit var battery: HTEnergyBattery
        private set

    override fun initializeEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? {
        val builder = HTBasicEnergyBatteryHolder.builder(this)
        battery = builder.addSlot(HTAccessConfig.BOTH, createBattery(listener))
        return builder.build()
    }

    protected abstract fun createBattery(listener: HTContentListener): HTEnergyBattery

    private lateinit var extractSlot: HTItemSlot
    private lateinit var insertSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(null)
        // extract
        extractSlot = builder.addSlot(
            HTAccessConfig.NONE,
            HTItemStackSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
                filter = { stack: ImmutableItemStack ->
                    val battery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return@create false
                    !battery.isEmpty()
                },
            ),
        )
        // insert
        insertSlot = builder.addSlot(
            HTAccessConfig.NONE,
            HTItemStackSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(1),
                filter = { stack: ImmutableItemStack ->
                    val battery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return@create false
                    battery.getNeeded() > 0
                },
            ),
        )
        return builder.build()
    }

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 左のスロットから電力を吸い取る
        extractFromItem()
        // 右のスロットに電力を渡す
        receiveToItem()
        return false
    }

    private fun extractFromItem(): TriState {
        val stackIn: ImmutableItemStack = extractSlot.getStack() ?: return TriState.FALSE
        val energyIn: HTEnergyBattery = HTEnergyCapabilities.getBattery(stackIn)
            ?: return TriState.FALSE
        var toExtract: Int = transferRate
        toExtract = energyIn.extract(toExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (toExtract > 0) {
            var mayReceive: Int = battery.insert(toExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            mayReceive = min(toExtract, mayReceive)
            if (mayReceive > 0) {
                energyIn.extract(mayReceive, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                battery.insert(mayReceive, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    private fun receiveToItem(): TriState {
        val stackIn: ImmutableItemStack = insertSlot.getStack() ?: return TriState.FALSE
        val energyIn: HTEnergyBattery = HTEnergyCapabilities.getBattery(stackIn)
            ?: return TriState.FALSE
        var toReceive: Int = transferRate
        toReceive = energyIn.insert(toReceive, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (toReceive > 0) {
            var mayExtract: Int = battery.extract(toReceive, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            mayExtract = min(toReceive, mayExtract)
            if (mayExtract > 0) {
                energyIn.insert(mayExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                battery.extract(mayExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    protected abstract val transferRate: Int

    //    Creative    //

    class Creative(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlocks.CEU, pos, state) {
        override fun createBattery(listener: HTContentListener): HTEnergyBattery =
            object : HTEnergyBattery, HTContentListener.Empty, HTValueSerializable.Empty {
                override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = 0

                override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

                override fun getAmount(): Int = 0

                override fun getCapacity(): Int = Int.MAX_VALUE
            }

        override val transferRate: Int = Int.MAX_VALUE
    }

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlocks.ENI, pos, state) {
        override fun createBattery(listener: HTContentListener): HTEnergyBattery =
            HTEnergyBatteryWrapper { RagiumPlatform.INSTANCE.getEnergyNetwork(this.getLevel()) }

        override val transferRate: Int = 1000
    }
}
