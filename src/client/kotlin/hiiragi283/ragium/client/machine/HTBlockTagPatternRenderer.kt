package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTBlockTagPattern
import net.minecraft.block.BlockState
import net.minecraft.world.World

object HTBlockTagPatternRenderer : HTMultiblockPatternRenderer.BlockRender<HTBlockTagPattern> {
    override fun getBlockState(pattern: HTBlockTagPattern, world: World, provider: HTMultiblockProvider): BlockState? =
        pattern.getCurrentState(world)
}
