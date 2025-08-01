package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTEnergyNetworkAccessMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

sealed class HTEnergyNetworkAccessBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(type, pos, state) {
    private val inventory: HTItemStackHandler = object : HTItemStackHandler(2, this::setChanged) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            val energyStorage: IEnergyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return false
            return when (slot) {
                0 -> energyStorage.energyStored > 0 && energyStorage.canExtract()
                1 -> energyStorage.energyStored < energyStorage.maxEnergyStored && energyStorage.canReceive()
                else -> false
            }
        }
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

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 左のスロットから電力を吸い取る
        val extractResult: TriState = extractFromItem()
        // 右のスロットに電力を渡す
        val receiveResult: TriState = receiveToItem()
        // どちらかが行えればtrue
        return when {
            !extractResult.isFalse || !receiveResult.isFalse -> TriState.TRUE
            else -> TriState.FALSE
        }
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

    //    Menu    //

    override val containerData: ContainerData = SimpleContainerData(2)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTEnergyNetworkAccessMenu =
        HTEnergyNetworkAccessMenu(
            containerId,
            playerInventory,
            blockPos,
            createDefinition(inventory),
        )

    //    Creative    //

    class Creative(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlockEntityTypes.CEU, pos, state) {
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

    class Simple(pos: BlockPos, state: BlockState) : HTEnergyNetworkAccessBlockEntity(RagiumBlockEntityTypes.ENI, pos, state) {
        override var network: IEnergyStorage? = null

        override val transferRate: Int = 1000

        override fun afterLevelInit(level: Level) {
            network = RagiumAPI.getInstance().getEnergyNetworkManager().getNetwork(level)
        }
    }
}
