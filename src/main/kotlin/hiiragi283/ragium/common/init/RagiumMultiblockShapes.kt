package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.common.machine.HTSimpleBlockPattern
import hiiragi283.ragium.common.machine.HTTieredBlockPattern
import net.minecraft.block.Blocks

object RagiumMultiblockShapes {
    private val SHAFT = HTSimpleBlockPattern(RagiumBlocks.SHAFT)
    private val CASING: HTTieredBlockPattern = HTTieredBlockPattern.ofContent(HTMachineTier::getCasing)
    private val HULL: HTTieredBlockPattern = HTTieredBlockPattern.ofContent(HTMachineTier::getHull)
    private val GRATE: HTTieredBlockPattern = HTTieredBlockPattern.ofContent(HTMachineTier::getGrate)
    private val GLASS: HTTieredBlockPattern = HTTieredBlockPattern.ofContent(HTMachineTier::getGlassBlock)

    @JvmField
    val ASSEMBLY_LINE: HTMultiblockBuilder.Consumer = HTMultiblockBuilder.Consumer { builder: HTMultiblockBuilder ->
        // base
        builder.addLayer(-7..1, 0, 1..3, CASING)
        // glass
        builder.addLayer(-7..-7, 1, 1..3, CASING)
        builder.addLayer(1..1, 1, 1..3, CASING)
        builder.addLayer(-6..0, 1, 1..1, GLASS)
        builder.addLayer(-6..0, 1, 3..3, GLASS)
        // hull
        builder.addHollow(-7..1, 2, 1..3, HULL)
        // top
        builder.addLayer(
            -7..1,
            3,
            2..2,
            HTTieredBlockPattern.ofBlock { tier: HTMachineTier ->
                when (tier) {
                    HTMachineTier.PRIMITIVE -> Blocks.STONE_SLAB
                    HTMachineTier.BASIC -> Blocks.QUARTZ_SLAB
                    HTMachineTier.ADVANCED -> Blocks.POLISHED_DEEPSLATE_SLAB
                }
            },
        )
    }

    @JvmField
    val BEDROCK_MINER: HTMultiblockBuilder.Consumer = HTMultiblockBuilder.Consumer { builder: HTMultiblockBuilder ->
        // drill
        builder.add(0, -3, 0, HTSimpleBlockPattern(Blocks.BEDROCK))
        // builder.add(0, -2, 0, shaft)
        // builder.add(0, -1, 0, shaft)
        builder.add(-1, 0, 0, SHAFT)
        builder.add(0, 0, -1, SHAFT)
        builder.add(0, 0, 1, SHAFT)
        builder.add(1, 0, 0, SHAFT)
        // frame
        builder.add(-2, -1, 0, CASING)
        builder.add(0, -1, -2, CASING)
        builder.add(0, -1, 2, CASING)
        builder.add(2, -1, 0, CASING)

        builder.add(-2, 0, 0, HULL)
        builder.add(0, 0, -2, HULL)
        builder.add(0, 0, 2, HULL)
        builder.add(2, 0, 0, HULL)

        builder.add(-2, 0, -2, GRATE)
        builder.add(-2, 0, -1, GRATE)
        builder.add(-2, 0, 1, GRATE)
        builder.add(-2, 0, -2, GRATE)
        builder.add(-1, 0, -2, GRATE)
        builder.add(-1, 0, 2, GRATE)
        builder.add(1, 0, -2, GRATE)
        builder.add(1, 0, 2, GRATE)
        builder.add(2, 0, -2, GRATE)
        builder.add(2, 0, -1, GRATE)
        builder.add(2, 0, 1, GRATE)
        builder.add(2, 0, -2, GRATE)

        builder.add(-2, 1, 0, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
        builder.add(0, 1, -2, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
        builder.add(0, 1, 2, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
        builder.add(2, 1, 0, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
    }

    @JvmField
    val BLAST_FURNACE: HTMultiblockBuilder.Consumer = HTMultiblockBuilder.Consumer { builder: HTMultiblockBuilder ->
        builder.addLayer(-1..1, 0, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getHull))
        builder.addHollow(-1..1, 1, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCoil))
        builder.addHollow(-1..1, 2, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCoil))
        builder.addLayer(-1..1, 3, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCasing))
    }

    @JvmField
    val CUTTING_MACHINE: HTMultiblockBuilder.Consumer = HTMultiblockBuilder.Consumer { builder: HTMultiblockBuilder ->
        // bottom
        builder.addLayer(-1..1, 0, 1..1, HTTieredBlockPattern.ofContent(HTMachineTier::getHull))
        builder.add(-1, 0, 2, HTSimpleBlockPattern(Blocks.STONE_SLAB))
        builder.add(0, 0, 2, HTSimpleBlockPattern(Blocks.STONECUTTER))
        builder.add(1, 0, 2, HTSimpleBlockPattern(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 3..3, HTTieredBlockPattern.ofContent(HTMachineTier::getHull))
        // middle
        builder.addLayer(-1..1, 1, 1..1, HTTieredBlockPattern.ofContent(HTMachineTier::getGlassBlock))
        builder.addLayer(-1..1, 1, 3..3, HTTieredBlockPattern.ofContent(HTMachineTier::getGlassBlock))
        // top
        builder.addLayer(-1..1, 2, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
    }

    @JvmField
    val DISTILLATION_TOWER: HTMultiblockBuilder.Consumer =
        HTMultiblockBuilder.Consumer { builder: HTMultiblockBuilder ->
            builder.addLayer(-1..1, -1, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCasing))
            builder.addHollow(-1..1, 0, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getHull))
            builder.addCross4(-1..1, 1, 1..3, HTSimpleBlockPattern(Blocks.RED_CONCRETE))
            builder.addCross4(-1..1, 2, 1..3, HTSimpleBlockPattern(Blocks.WHITE_CONCRETE))
            builder.addCross4(-1..1, 3, 1..3, HTSimpleBlockPattern(Blocks.RED_CONCRETE))
            builder.add(0, 4, 2, HTSimpleBlockPattern(Blocks.WHITE_CONCRETE))
        }

    @JvmField
    val MULTI_SMELTER: HTMultiblockBuilder.Consumer = HTMultiblockBuilder.Consumer { builder: HTMultiblockBuilder ->
        builder.addLayer(-1..1, -1, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCasing))
        builder.addHollow(-1..1, 0, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCoil))
        builder.addLayer(-1..1, 1, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
    }
}
