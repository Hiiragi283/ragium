package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.HTKineticProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
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
        isBlazing = cachedState.isOf(RagiumContents.BLAZING_BOX)
    }

    override fun onInactive(world: World, pos: BlockPos) {
        isBlazing = false
    }

    //    NamedScreenHandlerFactory    //

    override fun getDisplayName(): Text = RagiumContents.BLAZING_BOX.name
}
