package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTTieredBlockPattern
import net.minecraft.block.BlockState
import net.minecraft.world.World

object HTTieredBlockPatternRenderer : HTMultiblockPatternRenderer.BlockRender<HTTieredBlockPattern> {
    override fun getBlockState(pattern: HTTieredBlockPattern, world: World, provider: HTMultiblockProvider): BlockState? =
        pattern.getBlock(world, provider)?.defaultState
}
