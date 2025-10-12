package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.common.storage.energy.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import kotlin.math.min

sealed class HTEnergyNetworkAccessBlockEntity(variant: HTDeviceVariant, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(variant, pos, state) {
    private lateinit var battery: HTEnergyBattery

    override fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder? {
        battery = createEnergyStorage(listener)
        return HTSimpleEnergyStorageHolder.generic(null, battery)
    }

    protected abstract fun createEnergyStorage(listener: HTContentListener): HTEnergyBattery

    private lateinit var extractSlot: HTItemSlot
    private lateinit var insertSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        // extract
        extractSlot = HTItemStackSlot.create(
            listener,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
            filter = { stack: HTItemStorageStack ->
                val battery: HTEnergyBattery = RagiumCapabilities.ENERGY.getCapabilitySlot(stack, 0) ?: return@create false
                !battery.isEmpty()
            },
        )
        // insert
        insertSlot = HTItemStackSlot.create(
            listener,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
            filter = { stack: HTItemStorageStack ->
                val battery: HTEnergyBattery = RagiumCapabilities.ENERGY.getCapabilitySlot(stack, 0) ?: return@create false
                battery.getNeededAsInt() > 0
            },
        )
        return HTSimpleItemSlotHolder(null, listOf(extractSlot), listOf(extractSlot))
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = RagiumMenuTypes.ENERGY_NETWORK_ACCESS.openMenu(player, name, this, ::writeExtraContainerData)

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 左のスロットから電力を吸い取る
        extractFromItem()
        // 右のスロットに電力を渡す
        receiveToItem()
        return false
    }

    private fun extractFromItem(): TriState {
        val stackIn: HTItemStorageStack = extractSlot.getStack()
        val energyIn: HTEnergyBattery = RagiumCapabilities.ENERGY.getCapabilitySlot(stackIn, 0)
            ?: return TriState.FALSE
        var toExtract: Int = transferRate
        toExtract = energyIn.extractEnergy(toExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (toExtract > 0) {
            var mayReceive: Int = battery.insertEnergy(toExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            mayReceive = min(toExtract, mayReceive)
            if (mayReceive > 0) {
                energyIn.extractEnergy(mayReceive, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                battery.insertEnergy(mayReceive, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    private fun receiveToItem(): TriState {
        val stackIn: HTItemStorageStack = insertSlot.getStack()
        val energyIn: HTEnergyBattery = RagiumCapabilities.ENERGY.getCapabilitySlot(stackIn, 0)
            ?: return TriState.FALSE
        var toReceive: Int = transferRate
        toReceive = energyIn.insertEnergy(toReceive, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
        if (toReceive > 0) {
            var mayExtract: Int = battery.extractEnergy(toReceive, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
            mayExtract = min(toReceive, mayExtract)
            if (mayExtract > 0) {
                energyIn.insertEnergy(mayExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                battery.extractEnergy(mayExtract, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
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

    class Creative(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(HTDeviceVariant.CEU, pos, state) {
        override fun createEnergyStorage(listener: HTContentListener): HTEnergyBattery =
            object : HTEnergyBattery, HTValueSerializable.Empty {
                override fun getAmountAsLong(): Long = 0

                override fun getCapacityAsLong(): Long = Long.MAX_VALUE

                override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

                override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int = amount

                override fun onContentsChanged() {}
            }

        override val transferRate: Int = Int.MAX_VALUE
    }

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(HTDeviceVariant.ENI, pos, state) {
        override fun createEnergyStorage(listener: HTContentListener): HTEnergyBattery =
            HTEnergyBatteryWrapper { RagiumPlatform.INSTANCE.getEnergyNetwork(level) }

        override val transferRate: Int = 1000
    }
}
