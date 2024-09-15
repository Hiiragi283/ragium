package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.shape.HTBlockPredicate
import hiiragi283.ragium.common.shape.HTMultiMachineShape
import net.minecraft.block.Blocks

object RagiumMultiMachineShapes {
    @JvmField
    val BRICK_BLAST_FURNACE: HTMultiMachineShape =
        HTMultiMachineShape
            .Builder()
            .addLayer(-1..1, 0, 1..3, HTBlockPredicate.block(Blocks.BRICKS))
            .addHollow(-1..1, 1, 1..3, HTBlockPredicate.block(Blocks.BRICKS))
            .addHollow(-1..1, 2, 1..3, HTBlockPredicate.block(Blocks.BRICKS))
            .addHollow(-1..1, 2, 1..3, HTBlockPredicate.block(Blocks.BRICKS))
            .build()
}
