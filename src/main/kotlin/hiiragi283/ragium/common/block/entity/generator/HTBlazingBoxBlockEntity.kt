package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.block.entity.HTKineticProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBlazingBoxBlockEntity(pos: BlockPos, state: BlockState) :
    HTHeatGeneratorBlockEntity(RagiumBlockEntityTypes.BLAZING_BOX, pos, state),
    HTKineticProcessor {
    var isBlazing: Boolean = false
        private set

    //    HTKineticProcessor    //

    override fun onActive(world: World, pos: BlockPos) {
        isBlazing = cachedState.isOf(RagiumBlocks.BLAZING_BOX)
    }

    override fun onInactive(world: World, pos: BlockPos) {
        isBlazing = false
    }

    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = RagiumBlocks.BLAZING_BOX.name
}
