package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.capability.getStorage
import hiiragi283.ragium.api.storage.energy.HTEnergyStorage
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.energy.battery.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import kotlin.math.min

sealed class HTEnergyNetworkAccessBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity.Tickable(blockHolder, pos, state) {
    private val energyStorage: HTEnergyStorage = createEnergyStorage(::setOnlySave)

    protected abstract fun createEnergyStorage(listener: HTContentListener): HTEnergyStorage

    final override fun getEnergyStorage(direction: Direction?): HTEnergyStorage = energyStorage

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
                    val storage: HTEnergyStorage = RagiumCapabilities.ENERGY.getStorage(stack) ?: return@create false
                    !storage.isEmpty()
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
                    val storage: HTEnergyStorage = RagiumCapabilities.ENERGY.getStorage(stack) ?: return@create false
                    storage.getNeededAsInt() > 0
                },
            ),
        )
        return builder.build()
    }

    override fun onRightClicked(context: HTBlockInteractContext): InteractionResult =
        RagiumMenuTypes.ENERGY_NETWORK_ACCESS.openMenu(context.player, name, this, ::writeExtraContainerData)

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 左のスロットから電力を吸い取る
        extractFromItem()
        // 右のスロットに電力を渡す
        receiveToItem()
        return false
    }

    private fun extractFromItem(): TriState {
        val stackIn: ImmutableItemStack = extractSlot.getStack()
        val energyIn: HTEnergyStorage = RagiumCapabilities.ENERGY.getStorage(stackIn)
            ?: return TriState.FALSE
        var toExtract: Int = transferRate
        toExtract = energyIn.extractEnergy(toExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (toExtract > 0) {
            var mayReceive: Int = energyStorage.insertEnergy(toExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            mayReceive = min(toExtract, mayReceive)
            if (mayReceive > 0) {
                energyIn.extractEnergy(mayReceive, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                energyStorage.insertEnergy(mayReceive, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    private fun receiveToItem(): TriState {
        val stackIn: ImmutableItemStack = insertSlot.getStack()
        val energyIn: HTEnergyStorage = RagiumCapabilities.ENERGY.getStorage(stackIn)
            ?: return TriState.FALSE
        var toReceive: Int = transferRate
        toReceive = energyIn.insertEnergy(toReceive, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (toReceive > 0) {
            var mayExtract: Int = energyStorage.extractEnergy(toReceive, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            mayExtract = min(toReceive, mayExtract)
            if (mayExtract > 0) {
                energyIn.insertEnergy(mayExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                energyStorage.extractEnergy(mayExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
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
        override fun createEnergyStorage(listener: HTContentListener): HTEnergyStorage =
            object : HTEnergyStorage, HTContentListener.Empty, HTValueSerializable.Empty {
                override fun getAmountAsInt(): Int = 0

                override fun getCapacityAsInt(): Int = Int.MAX_VALUE

                override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

                override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount
            }

        override val transferRate: Int = Int.MAX_VALUE
    }

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlocks.ENI, pos, state) {
        override fun createEnergyStorage(listener: HTContentListener): HTEnergyStorage =
            HTEnergyBatteryWrapper { RagiumPlatform.INSTANCE.getEnergyNetwork(level) }

        override val transferRate: Int = 1000
    }
}
