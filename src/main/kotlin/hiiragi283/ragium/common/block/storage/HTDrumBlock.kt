package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTDrumBlock(override val tier: HTMachineTier, properties: Properties) :
    HTEntityBlock(properties),
    HTMachineTierProvider {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTDrumBlockEntity = HTDrumBlockEntity(pos, state, tier)
}
