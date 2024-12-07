package hiiragi283.ragium.api.machine.multiblock

import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.extension.mapIfServer
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTMultiblockManager(private val world: () -> World?, private val pos: BlockPos, private val provider: HTMultiblockPatternProvider) :
    HTMultiblockBuilder {
    var showPreview: Boolean = false

    val stateMap: Map<BlockPos, Pair<BlockPos, BlockState>>
        get() = stateCache

    private val stateCache: MutableMap<BlockPos, Pair<BlockPos, BlockState>> = mutableMapOf()

    fun onUse(state: BlockState, player: PlayerEntity): Boolean = when {
        player.isSneaking -> {
            showPreview = !showPreview
            true
        }

        else -> world()
            .mapIfServer { serverWorld: ServerWorld ->
                val front: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
                if (updateValidation(state, player)) {
                    provider.buildMultiblock(
                        HTMultiblockBuilder { x: Int, y: Int, z: Int, pattern: HTMultiblockPattern ->
                            val pos1: BlockPos = pos.add(x, y, z)
                            stateCache[BlockPos(x, y, z)] = pos1 to serverWorld.getBlockState(pos1)
                            this
                        }.rotate(front),
                    )
                }
                isValid
            }.result()
            .orElse(false)
    }

    fun updateValidation(state: BlockState, player: PlayerEntity? = null): Boolean {
        this.player = player
        val front: Direction = state.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
        provider.buildMultiblock(this.rotate(front))
        return isValid
    }

    //    HTMultiblockBuilder    //

    private var player: PlayerEntity? = null

    var isValid: Boolean = true
        private set

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        pattern: HTMultiblockPattern,
    ): HTMultiblockBuilder = apply {
        val pos1: BlockPos = pos.add(x, y, z)
        if (isValid) {
            isValid = world()?.let { pattern.test(it, pos1) } == true
            if (!isValid) {
                player?.sendMessage(
                    Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_ERROR, pattern.text, blockPosText(pos1)),
                    false,
                )
            }
        }
    }
}
