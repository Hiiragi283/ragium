package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import net.minecraft.block.Blocks

object HTTempMultiblocks : HTMultiblockController {
    override var showPreview: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        val tier = HTMachineTier.PRIMITIVE
        // blast furnace
        builder
            .addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addHollow(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addHollow(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(tier.getCoil()),
            ).addLayer(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            )
        // distillation tower
        builder
            .addLayer(
                -1..1,
                -1,
                1..3,
                HTMultiblockComponent.of(tier.getBaseBlock()),
            ).addHollow(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
            ).addCross4(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.of(Blocks.RED_CONCRETE),
            )
        builder.add(
            0,
            4,
            2,
            HTMultiblockComponent.of(Blocks.WHITE_CONCRETE),
        )
        // saw mill
        builder.add(-1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
        builder.add(1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
        builder.add(-1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
        builder.add(0, 0, 1, HTMultiblockComponent.of(Blocks.STONECUTTER))
        builder.add(1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 2..2, HTMultiblockComponent.of(tier.getHull()))
    }
}
