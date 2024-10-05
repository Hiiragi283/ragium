package hiiragi283.ragium.api.machine.multiblock

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMultiblockConstructor(private val world: World, private val pos: BlockPos, private val replace: Boolean = false) :
    HTMultiblockBuilder {
    private var isValid: Boolean = true

    override fun add(
        x: Int,
        y: Int,
        z: Int,
        component: HTMultiblockComponent,
    ): HTMultiblockBuilder = apply {
        val pos1: BlockPos = pos.add(x, y, z)
        if (isValid) {
            if (replace) {
                component.getPreviewState(world)?.let {
                    world.setBlockState(pos1, it)
                }
            } else if (!world.isAir(pos1)) {
                isValid = false
            } else {
                component.getPreviewState(world)?.let {
                    world.setBlockState(pos1, it)
                }
            }
        } else {
            isValid = false
        }
    }
}
