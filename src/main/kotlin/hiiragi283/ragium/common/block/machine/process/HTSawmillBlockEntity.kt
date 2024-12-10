package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos

class HTSawmillBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase.Large(RagiumBlockEntityTypes.SAW_MILL, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.SAW_MILL

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.addLayer(-1..1, 0, 1..1, HTMultiblockPattern.of(tier.getHull()))
        builder.add(-1, 0, 2, HTMultiblockPattern.of(Blocks.STONE_SLAB))
        builder.add(0, 0, 2, HTMultiblockPattern.of(Blocks.STONECUTTER))
        builder.add(1, 0, 2, HTMultiblockPattern.of(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 3..3, HTMultiblockPattern.of(tier.getHull()))
    }
}
