package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.block.attribute.getAttributeFront
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.event.EventHooks

class HTBlockBreakerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<BlockPos, HTBlockBreakerBlockEntity.MiningRecipe>(RagiumBlocks.BLOCK_BREAKER, pos, state) {
    lateinit var toolSlot: HTItemStackSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        toolSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.create(
                listener,
                HTSlotHelper.getSlotPosX(2),
                HTSlotHelper.getSlotPosY(1),
                canExtract = HTPredicates.manualOnly(),
                canInsert = HTPredicates.manualOnly(),
            ),
        )
    }

    private var fakePlayer: ServerPlayer? = null

    override fun onUpdateLevel(level: Level, pos: BlockPos) {
        super.onUpdateLevel(level, pos)
        if (level is ServerLevel) {
            fakePlayer = FakePlayerFactory.get(level, getOwnerProfile())
        }
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = true

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): BlockPos? = blockState.getAttributeFront()?.let(pos::relative)

    override fun getMatchedRecipe(input: BlockPos, level: ServerLevel): MiningRecipe? {
        val state: BlockState = level.getBlockState(input)
        val player: ServerPlayer = this.fakePlayer ?: return null
        player.setItemInHand(InteractionHand.MAIN_HAND, this.toolSlot.getItemStack())
        val tool: ImmutableItemStack = this.toolSlot.getStack() ?: return null
        return when {
            // 採掘速度が0未満の場合はスキップ
            state.getDestroySpeed(level, input) < 0 -> null
            // 採掘できない場合はスキップ
            !state.canHarvestBlock(level, input, player) -> null
            else -> MiningRecipe(state, player, tool)
        }
    }

    override fun getRecipeTime(recipe: MiningRecipe): Int = 20 * 3

    // イベントがキャンセルされた場合はスキップ
    override fun canProgressRecipe(level: ServerLevel, input: BlockPos, recipe: MiningRecipe): Boolean {
        val (state: BlockState, player: ServerPlayer) = recipe
        return !CommonHooks.fireBlockBreak(level, GameType.SURVIVAL, player, input, state).isCanceled
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: BlockPos,
        recipe: MiningRecipe,
    ) {
        val (state: BlockState, player: ServerPlayer, tool: ImmutableItemStack) = recipe
        // ブロックを採掘する
        val blockTo: Block = state.block
        val newStateTo: BlockState = blockTo.playerWillDestroy(level, input, state, player)
        val toolStack: ItemStack = tool.unwrap()
        val toolStack1: ItemStack = toolStack.copy()
        val canHarvest: Boolean = newStateTo.canHarvestBlock(level, input, player)
        toolStack.mineBlock(level, newStateTo, input, player)
        val removed: Boolean =
            newStateTo.onDestroyedByPlayer(level, input, player, canHarvest, level.getFluidState(input))
        if (removed) {
            newStateTo.block.destroy(level, input, newStateTo)
        }
        if (canHarvest && removed) {
            blockTo.playerDestroy(level, player, input, newStateTo, level.getBlockEntity(input), toolStack1)
        }

        toolSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        if (toolStack.isEmpty && !toolStack1.isEmpty) {
            EventHooks.onPlayerDestroyItem(player, toolStack1, InteractionHand.MAIN_HAND)
        } else {
            toolSlot.insert(toolStack.toImmutable(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    data class MiningRecipe(val state: BlockState, val player: ServerPlayer, val tool: ImmutableItemStack)
}
