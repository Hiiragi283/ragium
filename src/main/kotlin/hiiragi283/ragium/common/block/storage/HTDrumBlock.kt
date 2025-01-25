package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.getItemData
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTDrumBlock(properties: Properties) : HTEntityBlock(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTDrumBlockEntity =
        HTDrumBlockEntity(pos, state, state.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER) ?: HTMachineTier.BASIC)
}
