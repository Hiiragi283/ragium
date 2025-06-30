package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler

class HTChargerBlockEntity(pos: BlockPos, state: BlockState) : HTTickAwareBlockEntity(RagiumBlockEntityTypes.CHARGER, pos, state) {
    private val inventory = HTItemStackHandler(1, this::setChanged)
    val stack: ItemStack get() = inventory.getStackInSlot(0)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.INVENTORY, inventory)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.INVENTORY, inventory)
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        inventory.dropStacksAt(player)
        inventory.setStackInSlot(0, stack.copyWithCount(1))
        stack.shrink(1)
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        inventory.dropStacksAt(player)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        inventory.dropStacksAt(level, pos)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // エネルギーネットワークを取得
        val network: IEnergyStorage =
            RagiumAPI.getInstance().getEnergyNetworkManager().getNetworkFromServer(level)
        if (inventory.isEmpty) return TriState.FALSE
        val stack: ItemStack = this.stack
        val energyStorage: IEnergyStorage =
            stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return TriState.FALSE
        val mayReceive: Int = energyStorage.receiveEnergy(1600, true)
        if (mayReceive > 0) {
            val actualReceive: Int = network.extractEnergy(mayReceive, true)
            if (actualReceive > 0) {
                energyStorage.receiveEnergy(actualReceive, false)
                network.extractEnergy(actualReceive, false)
                return TriState.TRUE
            }
        }
        return TriState.FALSE
    }

    override val maxTicks: Int = 20

    fun canCharge(): Boolean {
        if (inventory.isEmpty) return false
        val stack: ItemStack = this.stack
        val energyStorage: IEnergyStorage =
            stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return false
        return energyStorage.receiveEnergy(1600, true) > 0
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean {
                if (slot != 0) return false
                val energyStorage: IEnergyStorage = handler
                    .getStackInSlot(slot)
                    .getCapability(Capabilities.EnergyStorage.ITEM)
                    ?: return false
                return energyStorage.energyStored < energyStorage.maxEnergyStored
            }

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean {
                if (slot != 0) return false
                val energyStorage: IEnergyStorage = handler
                    .getStackInSlot(slot)
                    .getCapability(Capabilities.EnergyStorage.ITEM)
                    ?: return false
                return energyStorage.energyStored == energyStorage.maxEnergyStored
            }
        },
    )
}
