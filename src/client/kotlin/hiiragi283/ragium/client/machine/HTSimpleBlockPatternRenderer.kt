package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTSimpleBlockPattern
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTSimpleBlockPatternRenderer : HTMultiblockPatternRenderer.BlockRender<HTSimpleBlockPattern> {
    override fun getBlockState(pattern: HTSimpleBlockPattern, world: World, provider: HTMultiblockProvider): BlockState? =
        pattern.getPlacementState(world, BlockPos.ORIGIN, provider)
}
