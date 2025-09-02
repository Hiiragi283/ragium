package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkWrapper
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

sealed class HTEnergyNetworkAccessBlockEntity(variant: HTDeviceVariant, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(variant, pos, state) {
    private lateinit var energyStorage: IEnergyStorage

    override fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder? {
        energyStorage = createEnergyStorage(listener)
        return HTSimpleEnergyStorageHolder.generic(null, energyStorage)
    }

    protected abstract fun createEnergyStorage(listener: HTContentListener): IEnergyStorage

    private lateinit var extractSlot: HTItemSlot
    private lateinit var insertSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        // extract
        extractSlot = HTItemStackSlot.create(
            listener,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
            filter = { stack: ItemStack ->
                val energyStorage: IEnergyStorage =
                    HTMultiCapability.ENERGY.getCapability(stack) ?: return@create false
                energyStorage.energyStored > 0 && energyStorage.canExtract()
            },
        )
        // insert
        insertSlot = HTItemStackSlot.create(
            listener,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
            filter = { stack: ItemStack ->
                val energyStorage: IEnergyStorage =
                    HTMultiCapability.ENERGY.getCapability(stack) ?: return@create false
                energyStorage.energyStored < energyStorage.maxEnergyStored && energyStorage.canReceive()
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
        val stackIn: ItemStack = extractSlot.getStack()
        val energyIn: IEnergyStorage = HTMultiCapability.ENERGY.getCapability(stackIn) ?: return TriState.FALSE
        var toExtract: Int = transferRate
        toExtract = energyIn.extractEnergy(toExtract, true)
        if (toExtract > 0) {
            var mayReceive: Int = energyStorage.receiveEnergy(toExtract, true)
            mayReceive = min(toExtract, mayReceive)
            if (mayReceive > 0) {
                energyIn.extractEnergy(mayReceive, false)
                energyStorage.receiveEnergy(mayReceive, false)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    private fun receiveToItem(): TriState {
        val stackIn: ItemStack = insertSlot.getStack()
        val energyIn: IEnergyStorage = HTMultiCapability.ENERGY.getCapability(stackIn) ?: return TriState.FALSE
        var toReceive: Int = transferRate
        toReceive = energyIn.receiveEnergy(toReceive, true)
        if (toReceive > 0) {
            var mayExtract: Int = energyStorage.extractEnergy(toReceive, true)
            mayExtract = min(toReceive, mayExtract)
            if (mayExtract > 0) {
                energyIn.receiveEnergy(mayExtract, false)
                energyStorage.extractEnergy(mayExtract, false)
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
        override fun createEnergyStorage(listener: HTContentListener): IEnergyStorage = object : IEnergyStorage {
            override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = toReceive

            override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = toExtract

            override fun getEnergyStored(): Int = 0

            override fun getMaxEnergyStored(): Int = Int.MAX_VALUE

            override fun canExtract(): Boolean = true

            override fun canReceive(): Boolean = true
        }

        override val transferRate: Int = Int.MAX_VALUE
    }

    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(HTDeviceVariant.ENI, pos, state) {
        override fun createEnergyStorage(listener: HTContentListener): IEnergyStorage =
            HTEnergyNetworkWrapper { level?.getData(RagiumAttachmentTypes.ENERGY_NETWORK) }

        override val transferRate: Int = 1000
    }
}
