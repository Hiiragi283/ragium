package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * Multiblock validation manager
 */
class HTMultiblockManager(private val world: () -> World?, private val pos: BlockPos, private val provider: HTMultiblockProvider) :
    HTMultiblockBuilder {
    var showPreview: Boolean = false

    private val stateCache: MutableMap<BlockPos, Pair<BlockPos, BlockState>> = mutableMapOf()

    /**
     * @see [hiiragi283.ragium.api.block.HTMachineBlockEntityBase.onUse]
     */
    fun onUse(state: BlockState, player: PlayerEntity): Boolean = if (player.isSneaking) {
        showPreview = !showPreview
        false
    } else {
        val world: World = world() ?: return false
        if (!world.isClient) {
            provider.beforeBuild(world, pos, player)
            updateValidation(state)
                .ifSuccess {
                    val front: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
                    player.sendMessage(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS), true)
                    provider.buildMultiblock(
                        HTMultiblockBuilder { x: Int, y: Int, z: Int, pattern: HTMultiblockPattern ->
                            val pos1: BlockPos = pos.add(x, y, z)
                            stateCache[BlockPos(x, y, z)] = pos1 to world.getBlockState(pos1)
                        }.rotate(front),
                    )
                }.ifSuccess { provider.afterBuild(world(), pos, player, stateCache) }
                .ifError {
                    stateCache.clear()
                    player.sendMessage(it, false)
                }.toBoolean()
        } else {
            false
        }
    }

    fun updateValidation(state: BlockState): HTUnitResult {
        val front: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        provider.buildMultiblock(this.rotate(front))
        return patternResult
    }

    //    HTMultiblockBuilder    //

    val isValid: Boolean
        get() = patternResult.isSuccess

    private var patternResult: HTUnitResult = HTUnitResult.success()

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        pattern: HTMultiblockPattern,
    ) {
        val pos1: BlockPos = pos.add(x, y, z)
        if (patternResult.isSuccess) {
            patternResult = HTUnitResult.fromBool(world()?.let { pattern.test(it, pos1) } == true) {
                Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_ERROR, pattern.text, blockPosText(pos1))
            }
        }
    }
}
