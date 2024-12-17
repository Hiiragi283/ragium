package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase.Large(RagiumBlockEntityTypes.CUTTING_MACHINE, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.CUTTING_MACHINE

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        // bottom
        builder.addLayer(-1..1, 0, 1..1, HTMultiblockPattern.of(tier.getHull()))
        builder.add(-1, 0, 2, HTMultiblockPattern.of(Blocks.STONE_SLAB))
        builder.add(0, 0, 2, HTMultiblockPattern.of(Blocks.STONECUTTER))
        builder.add(1, 0, 2, HTMultiblockPattern.of(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 3..3, HTMultiblockPattern.of(tier.getHull()))
        // middle
        builder.addLayer(-1..1, 1, 1..1, HTMultiblockPattern.of(tier.getGlassBlock()))
        builder.addLayer(-1..1, 1, 3..3, HTMultiblockPattern.of(tier.getGlassBlock()))
        // top
        builder.addLayer(-1..1, 2, 1..3, HTMultiblockPattern.of(tier.getStorageBlock()))
    }
}
