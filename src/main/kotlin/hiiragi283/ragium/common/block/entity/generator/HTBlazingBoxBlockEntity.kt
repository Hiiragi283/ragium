package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class HTBlazingBoxBlockEntity(pos: BlockPos, state: BlockState) :
    HTHeatGeneratorBlockEntity(RagiumBlockEntityTypes.BLAZING_BOX, pos, state) {
    var isBlazing: Boolean = false
        private set

    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = RagiumContents.BLAZING_BOX.name
}
