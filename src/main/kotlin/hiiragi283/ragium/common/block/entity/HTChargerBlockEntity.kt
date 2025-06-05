package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
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

class HTChargerBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.CHARGER, pos, state),
    HTItemSlotHandler {
    private val itemSlot: HTItemSlot = HTItemSlot.create(RagiumConstantValues.INPUT_SLOT, this) {
        capacity = 1
    }

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        itemSlot.writeNbt(writer)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        itemSlot.readNbt(reader)
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
        itemSlot.dropStack(player)
        itemSlot.replace(stack.copyWithCount(1), true)
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
        itemSlot.dropStack(player)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemSlot.dropStack(level, pos)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess(20)) return TriState.DEFAULT
        // エネルギーネットワークを取得
        val network: IEnergyStorage =
            RagiumAPI.getInstance().getEnergyNetworkManager().getNetworkFromServer(level)
        if (itemSlot.isEmpty) return TriState.FALSE
        val stack: ItemStack = itemSlot.stack
        val energyStorage: IEnergyStorage =
            stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return TriState.FALSE
        val mayReceive: Int = energyStorage.receiveEnergy(1600, true)
        if (mayReceive > 0) {
            val actualReceive: Int = network.extractEnergy(mayReceive, true)
            if (actualReceive > 0) {
                energyStorage.receiveEnergy(actualReceive, false)
                network.extractEnergy(actualReceive, false)
                itemSlot.replace(stack, true)
                return TriState.TRUE
            }
        }
        return TriState.FALSE
    }

    fun canCharge(): Boolean {
        if (itemSlot.isEmpty) return false
        val stack: ItemStack = itemSlot.stack
        val energyStorage: IEnergyStorage =
            stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return false
        return energyStorage.receiveEnergy(1600, true) > 0
    }

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO {
        if (itemSlot.resource.isEmpty) {
            return HTStorageIO.INPUT
        } else {
            val stack: ItemStack = itemSlot.stack
            val energyStorage: IEnergyStorage =
                stack.getCapability(Capabilities.EnergyStorage.ITEM) ?: return HTStorageIO.EMPTY
            return when (energyStorage.energyStored) {
                energyStorage.maxEnergyStored -> HTStorageIO.OUTPUT
                else -> HTStorageIO.EMPTY
            }
        }
    }

    override fun getItemSlot(slot: Int): HTItemSlot = itemSlot

    override fun getSlots(): Int = 1
}
