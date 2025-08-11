package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockStateProperties
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTSingleItemMenu
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
    override val inventory: HTItemHandler = HTItemStackHandler.Builder(1).addInput(0).build(::setChanged)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getBasicMachineEnergyUsage()

    /**
     * @see [com.hollingsworth.arsnouveau.api.util.BlockUtil.breakExtraBlock]
     */
    override fun serverTickPre(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 採掘用のFake Playerを用意する
        val player: FakePlayer = RagiumAPI.getInstance().getFakePlayer(level)
        val inventory: Inventory = player.inventory
        val toolStack: ItemStack = this.inventory.getStackInSlot(0)
        inventory.items[inventory.selected] = toolStack
        // 採掘対象のブロックを取得する
        val front: Direction = state.getValue(HTBlockStateProperties.HORIZONTAL)
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
        // エネルギーを消費する
        usedEnergy += network.extractEnergy(energyUsage, false)
        if (usedEnergy < requiredEnergy) return TriState.DEFAULT
        usedEnergy = 0
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
        return TriState.DEFAULT
    }

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTSingleItemMenu = HTSingleItemMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
