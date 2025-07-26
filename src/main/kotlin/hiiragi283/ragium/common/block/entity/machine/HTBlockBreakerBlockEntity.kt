package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.block.HTHorizontalEntityBlock
import hiiragi283.ragium.common.inventory.HTBlockBreakerMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.event.EventHooks

class HTBlockBreakerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.BLOCK_BREAKER, pos, state) {
    override val inventory: HTItemHandler = HTItemStackHandler(1, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.basicMachineEnergyUsage.get()

    /**
     * @see [com.hollingsworth.arsnouveau.api.util.BlockUtil.breakExtraBlock]
     */
    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(energyUsage, true) != energyUsage) return TriState.DEFAULT
        // 採掘用のFake Playerを用意する
        val player: FakePlayer = RagiumAPI.getInstance().getFakePlayer(level)
        val inventory: Inventory = player.inventory
        val toolStack: ItemStack = this.inventory.getStackInSlot(0)
        inventory.items[inventory.selected] = toolStack
        // 採掘対象のブロックを取得する
        val front: Direction = state.getValue(HTHorizontalEntityBlock.HORIZONTAL)
        val posTo: BlockPos = pos.relative(front)
        val stateTo: BlockState = level.getBlockState(posTo)
        // 採掘速度が0未満の場合はスキップ
        if (stateTo.getDestroySpeed(level, posTo) < 0) {
            return TriState.DEFAULT
        }
        // 採掘できない場合はスキップ
        if (!stateTo.canHarvestBlock(level, posTo, player)) {
            return TriState.DEFAULT
        }
        // イベントがキャンセルされた場合はスキップ
        if (CommonHooks.fireBlockBreak(level, GameType.SURVIVAL, player, posTo, stateTo).isCanceled) {
            return TriState.DEFAULT
        }
        // 進行度が最大でなければスキップ
        currentTicks++
        if (currentTicks < maxTicks) return TriState.DEFAULT
        currentTicks = 0
        // ブロックを採掘する
        val blockTo: Block = stateTo.block
        val newStateTo: BlockState = blockTo.playerWillDestroy(level, posTo, stateTo, player)
        val toolStack1: ItemStack = toolStack.copy()
        val canHarvest: Boolean = newStateTo.canHarvestBlock(level, posTo, player)
        toolStack.mineBlock(level, newStateTo, posTo, player)
        val removed: Boolean =
            newStateTo.onDestroyedByPlayer(level, posTo, player, canHarvest, level.getFluidState(posTo))
        if (removed) {
            newStateTo.block.destroy(level, posTo, newStateTo)
        }
        if (canHarvest && removed) {
            blockTo.playerDestroy(level, player, posTo, newStateTo, level.getBlockEntity(posTo), toolStack1)
        }
        if (toolStack.isEmpty && !toolStack1.isEmpty) {
            EventHooks.onPlayerDestroyItem(player, toolStack1, InteractionHand.MAIN_HAND)
        }
        // エネルギーを減らす
        network.extractEnergy(energyUsage, false)
        return TriState.DEFAULT
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(inventory, HTItemFilter.INSERT_ONLY)

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTBlockBreakerMenu = HTBlockBreakerMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
