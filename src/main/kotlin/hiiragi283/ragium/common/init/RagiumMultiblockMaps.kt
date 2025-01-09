package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.machine.HTMachineMultiblockComponent
import hiiragi283.ragium.common.machine.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.machine.HTTieredMultiblockComponent
import net.minecraft.block.Blocks

object RagiumMultiblockMaps {
    private val SHAFT = HTSimpleMultiblockComponent(RagiumBlocks.SHAFT)
    private val CASING = HTTieredMultiblockComponent(HTMachineTier::getCasing)
    private val HULL = HTTieredMultiblockComponent(HTMachineTier::getHull)
    private val GRATE = HTTieredMultiblockComponent(HTMachineTier::getGrate)
    private val STORAGE_BLOCK = HTTieredMultiblockComponent(HTMachineTier::getStorageBlock)

    @JvmField
    val BEDROCK_MINER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        // drill
        .add(0, -3, 0, HTSimpleMultiblockComponent(Blocks.BEDROCK))
        // .add(0, -2, 0, shaft)
        // .add(0, -1, 0, shaft)
        .add(-1, 0, 0, SHAFT)
        .add(0, 0, -1, SHAFT)
        .add(0, 0, 1, SHAFT)
        .add(1, 0, 0, SHAFT)
        // frame
        .add(-2, -1, 0, CASING)
        .add(0, -1, -2, CASING)
        .add(0, -1, 2, CASING)
        .add(2, -1, 0, CASING)
        .add(-2, 0, 0, HULL)
        .add(0, 0, -2, HULL)
        .add(0, 0, 2, HULL)
        .add(2, 0, 0, HULL)
        .add(-2, 0, -2, GRATE)
        .add(-2, 0, -1, GRATE)
        .add(-2, 0, 1, GRATE)
        .add(-2, 0, -2, GRATE)
        .add(-1, 0, -2, GRATE)
        .add(-1, 0, 2, GRATE)
        .add(1, 0, -2, GRATE)
        .add(1, 0, 2, GRATE)
        .add(2, 0, -2, GRATE)
        .add(2, 0, -1, GRATE)
        .add(2, 0, 1, GRATE)
        .add(2, 0, -2, GRATE)
        .add(-2, 1, 0, STORAGE_BLOCK)
        .add(0, 1, -2, STORAGE_BLOCK)
        .add(0, 1, 2, STORAGE_BLOCK)
        .add(2, 1, 0, STORAGE_BLOCK)
        .build()

    @JvmField
    val BLAST_FURNACE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, 1..3, HULL)
        .addHollow(-1..1, 1, 1..3, HTTieredMultiblockComponent(HTMachineTier::getCoil))
        .addHollow(-1..1, 2, 1..3, HTTieredMultiblockComponent(HTMachineTier::getCoil))
        .addLayer(-1..1, 3, 1..3, CASING)
        .build()

    @JvmField
    val CUTTING_MACHINE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        // bottom
        .addLayer(-1..1, 0, 1..1, HULL)
        .add(-1, 0, 2, HTSimpleMultiblockComponent(Blocks.STONE_SLAB))
        .add(0, 0, 2, HTSimpleMultiblockComponent(Blocks.STONECUTTER))
        .add(1, 0, 2, HTSimpleMultiblockComponent(Blocks.STONE_SLAB))
        .addLayer(-1..1, 0, 3..3, HULL)
        // middle
        .addLayer(-1..1, 1, 1..1, HTTieredMultiblockComponent(HTMachineTier::getGlassBlock))
        .addLayer(-1..1, 1, 3..3, HTTieredMultiblockComponent(HTMachineTier::getGlassBlock))
        // top
        .addLayer(-1..1, 2, 1..3, STORAGE_BLOCK)
        .build()

    @JvmField
    val DISTILLATION_TOWER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, HULL)
        .addCross4(-1..1, 1, 1..3, HTSimpleMultiblockComponent(Blocks.RED_CONCRETE))
        .addCross4(-1..1, 2, 1..3, HTSimpleMultiblockComponent(Blocks.WHITE_CONCRETE))
        .addCross4(-1..1, 3, 1..3, HTSimpleMultiblockComponent(Blocks.RED_CONCRETE))
        .add(0, 4, 2, HTSimpleMultiblockComponent(Blocks.WHITE_CONCRETE))
        .build()

    @JvmField
    val EXTENDED_PROCESSOR: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, HULL)
        .addLayer(-1..1, 1, 1..3, STORAGE_BLOCK)
        .add(0, 0, 2, HTMachineMultiblockComponent)
        .build()

    @JvmField
    val FLUID_DRILL: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, 1..3, HULL)
        .addCross4(-1..1, 1, 1..3, GRATE)
        .addCross4(-1..1, 2, 1..3, GRATE)
        .add(0, 3, 2, GRATE)
        .add(0, 4, 2, GRATE)
        .build()

    @JvmField
    val MULTI_SMELTER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, HTTieredMultiblockComponent(HTMachineTier::getCoil))
        .addLayer(-1..1, 1, 1..3, STORAGE_BLOCK)
        .build()

    @JvmField
    val LARGE_MACHINE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, HULL)
        .addLayer(-1..1, 1, 1..3, STORAGE_BLOCK)
        .build()
}
