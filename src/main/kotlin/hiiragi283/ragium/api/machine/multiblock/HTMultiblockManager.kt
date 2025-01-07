package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * マルチブロックの判定を行うマネージャー
 * @param world [provider]が存在するワールド
 * @param pos [provider]が存在する座標
 */
class HTMultiblockManager(private val world: () -> World?, val pos: BlockPos, private val provider: HTMultiblockProvider) {
    var showPreview: Boolean = false

    private val stateCache: MutableMap<BlockPos, Pair<BlockPos, BlockState>> = mutableMapOf()

    fun onUse(state: BlockState, player: PlayerEntity): Boolean = if (player.isSneaking) {
        showPreview = !showPreview
        false
    } else {
        val world: World = world() ?: return false
        if (!world.isClient) {
            provider.beforeBuild(world, pos, player)
            runCatching { updateValidation(state) }
                .onSuccess {
                    val front: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
                    player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), true)
                    provider.buildMultiblock(
                        HTMultiblockBuilder { x: Int, y: Int, z: Int, pattern: HTMultiblockPattern ->
                            val pos1: BlockPos = pos.add(x, y, z)
                            stateCache[BlockPos(x, y, z)] = pos1 to world.getBlockState(pos1)
                        }.rotate(front),
                    )
                }.onSuccess { provider.afterBuild(world(), pos, player, stateCache) }
                .onFailure { throwable: Throwable ->
                    stateCache.clear()
                    val text: Text = (throwable as? HTMachineException.Multiblock)?.text ?: return@onFailure
                    player.sendMessage(text, false)
                }.isSuccess
        } else {
            false
        }
    }

    /**
     * マルチブロックの判定を更新します。
     */
    @Throws(HTMachineException::class)
    fun updateValidation(state: BlockState) {
        val front: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        val builder = Builder()
        provider.buildMultiblock(builder.rotate(front))
    }

    //    HTMultiblockBuilder    //

    private inner class Builder : HTMultiblockBuilder {
        override fun add(
            x: Int,
            y: Int,
            z: Int,
            pattern: HTMultiblockPattern,
        ) {
            val pos1: BlockPos = pos.add(x, y, z)
            val world: World = world() ?: return
            if (!pattern.checkState(world, pos1, provider)) {
                throw HTMachineException.Multiblock(pattern, pos1)
            }
        }
    }
}
