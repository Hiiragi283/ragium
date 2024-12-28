package hiiragi283.ragium.client.machine

import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.render.HTMultiblockPatternRenderer
import hiiragi283.ragium.common.machine.HTMachineBlockPattern
import net.minecraft.block.BlockState
import net.minecraft.world.World

object HTMachineBlockPatternRenderer : HTMultiblockPatternRenderer.BlockRender<HTMachineBlockPattern> {
    override fun getBlockState(pattern: HTMachineBlockPattern, world: World, provider: HTMultiblockProvider): BlockState? = null
}
