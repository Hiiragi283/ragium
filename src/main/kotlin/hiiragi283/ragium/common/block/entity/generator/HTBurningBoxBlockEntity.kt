package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class HTBurningBoxBlockEntity(pos: BlockPos, state: BlockState) :
    HTHeatGeneratorBlockEntity(RagiumBlockEntityTypes.BURNING_BOX, pos, state) {
    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = RagiumContents.BURNING_BOX.name
}
