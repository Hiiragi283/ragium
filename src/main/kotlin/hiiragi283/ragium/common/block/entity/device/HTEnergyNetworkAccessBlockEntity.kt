package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import kotlin.math.min

sealed class HTEnergyNetworkAccessBlockEntity(variant: HTDeviceVariant, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(variant, pos, state) {
    private val inventory: HTItemStackHandler = object : HTItemStackHandler(2) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            val energyStorage: IEnergyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return false
            return when (slot) {
                0 -> energyStorage.energyStored > 0 && energyStorage.canExtract()
                1 -> energyStorage.energyStored < energyStorage.maxEnergyStored && energyStorage.canReceive()
                else -> false
            }
        }

        override fun onContentsChanged() {
            this@HTEnergyNetworkAccessBlockEntity.onContentsChanged()
        }

        override val inputSlots: IntArray = intArrayOf(0)
        override val outputSlots: IntArray = intArrayOf(1)
    }
    protected abstract val network: IEnergyStorage?

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.INVENTORY, inventory)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.INVENTORY, inventory)
    }

    override fun dropInventory(consumer: (ItemStack) -> Unit) {
        super.dropInventory(consumer)
        inventory.getStackView().forEach(consumer)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = RagiumMenuTypes.ENERGY_NETWORK_ACCESS.openMenu(player, name, this, ::writeExtraContainerData)

    /*override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val capacityAdd: Int = when {
            stack.`is`(RagiumItemTags.ENI_UPGRADES_BASIC) -> 1_000_000 // 1M
            stack.`is`(RagiumItemTags.ENI_UPGRADES_ADVANCED) -> 10_000_000 // 10M
            stack.`is`(RagiumItemTags.ENI_UPGRADES_ELITE) -> 100_000_000 // 100M
            stack.`is`(RagiumItemTags.ENI_UPGRADES_ULTIMATE) -> 1_000_000_000 // 1G
            else -> return super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
        }
        (network as? HTEnergyNetwork)?.let { network: HTEnergyNetwork ->
            network.capacity = min(network.capacity + capacityAdd, Int.MAX_VALUE)
            network.setDirty()
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }*/

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 左のスロットから電力を吸い取る
        extractFromItem()
        // 右のスロットに電力を渡す
        receiveToItem()
        return false
    }

    private fun extractFromItem(): TriState {
        val stackIn: ItemStack = inventory.getStackInSlot(0)
        val energyIn: IEnergyStorage = stackIn.getCapability(Capabilities.EnergyStorage.ITEM) ?: return TriState.FALSE
        var toExtract: Int = transferRate
        toExtract = energyIn.extractEnergy(toExtract, true)
        if (toExtract > 0) {
            var mayReceive: Int = network?.receiveEnergy(toExtract, true) ?: 0
            mayReceive = min(toExtract, mayReceive)
            if (mayReceive > 0) {
                energyIn.extractEnergy(mayReceive, false)
                network?.receiveEnergy(mayReceive, false)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    private fun receiveToItem(): TriState {
        val stackIn: ItemStack = inventory.getStackInSlot(1)
        val energyIn: IEnergyStorage = stackIn.getCapability(Capabilities.EnergyStorage.ITEM) ?: return TriState.FALSE
        var toReceive: Int = transferRate
        toReceive = energyIn.receiveEnergy(toReceive, true)
        if (toReceive > 0) {
            var mayExtract: Int = network?.extractEnergy(toReceive, true) ?: 0
            mayExtract = min(toReceive, mayExtract)
            if (mayExtract > 0) {
                energyIn.receiveEnergy(mayExtract, false)
                network?.extractEnergy(mayExtract, false)
                return TriState.TRUE
            } else {
                return TriState.DEFAULT
            }
        } else {
            return TriState.FALSE
        }
    }

    protected abstract val transferRate: Int

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = network

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1))
    }

    //    Creative    //

    class Creative(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(HTDeviceVariant.CEU, pos, state) {
        override val network: IEnergyStorage = object : IEnergyStorage {
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
        override var network: IEnergyStorage? = null

        override val transferRate: Int = 1000

        override fun afterLevelInit(level: Level) {
            network = level.getData(RagiumAttachmentTypes.ENERGY_NETWORK)
        }
    }
}
