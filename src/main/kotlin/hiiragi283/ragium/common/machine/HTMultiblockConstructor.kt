package hiiragi283.ragium.common.machine

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMultiblockConstructor(private val world: World, private val pos: BlockPos, private val replace: Boolean = false) :
    HTMultiblockBuilder {
    private var isValid: Boolean = true

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        predicate: HTBlockPredicate,
    ): HTMultiblockBuilder = apply {
        val pos1: BlockPos = pos.add(x, y, z)
        if (isValid) {
            if (replace) {
                predicate.getPreviewState(world)?.let {
                    world.setBlockState(pos1, it)
                }
            } else if (!world.isAir(pos1)) {
                isValid = false
            } else {
                predicate.getPreviewState(world)?.let {
                    world.setBlockState(pos1, it)
                }
            }
        } else {
            isValid = false
        }
    }
}
