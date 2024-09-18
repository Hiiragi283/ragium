package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class HTBurningBoxBlockEntity(pos: BlockPos, state: BlockState) :
    HTHeatGeneratorBlockEntity(RagiumBlockEntityTypes.BURNING_BOX, pos, state) {
    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = RagiumBlocks.BURNING_BOX.name
}
