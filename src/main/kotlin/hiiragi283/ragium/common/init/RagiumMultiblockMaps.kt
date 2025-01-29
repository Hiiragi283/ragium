package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.machine.HTAxisMultiblockComponent
import hiiragi283.ragium.common.machine.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.machine.HTTieredMultiblockComponent
import net.minecraft.world.level.block.Blocks

object RagiumMultiblockMaps {
    private val CASING = HTTieredMultiblockComponent(HTMachineTier::getCasing)
    private val COIL_Y = HTAxisMultiblockComponent.YStatic(HTMachineTier::getCoil)
    private val GRATE = HTTieredMultiblockComponent(HTMachineTier::getGrate)
    private val HULL = HTTieredMultiblockComponent(HTMachineTier::getHull)
    private val SHAFT = HTAxisMultiblockComponent.YStatic { RagiumBlocks.SHAFT }
    private val SHAFT_HORIZONTAL = HTAxisMultiblockComponent.FrontHorizontal { RagiumBlocks.SHAFT }
    private val SHAFT_VERTICAL = HTAxisMultiblockComponent.FrontVertical { RagiumBlocks.SHAFT }
    private val STORAGE_BLOCK = HTTieredMultiblockComponent(HTMachineTier::getStorageBlock)

    @JvmField
    val BEDROCK_MINER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        // drill
        .add(0, -3, 0, HTSimpleMultiblockComponent(Blocks::BEDROCK))
        .add(0, -2, 0, SHAFT)
        .add(0, -1, 0, SHAFT)
        .add(-1, 0, 0, SHAFT_VERTICAL)
        .add(0, 0, -1, SHAFT_HORIZONTAL)
        .add(0, 0, 1, SHAFT_HORIZONTAL)
        .add(1, 0, 0, SHAFT_VERTICAL)
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
        .addHollow(-1..1, 1, 1..3, COIL_Y)
        .addHollow(-1..1, 2, 1..3, COIL_Y)
        .addLayer(-1..1, 3, 1..3, CASING)
        .build()

    @JvmField
    val COKE_OVEN: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, HTSimpleMultiblockComponent(Blocks::MUD_BRICKS))
        .addHollow(-1..1, 0, 1..3, HTSimpleMultiblockComponent(Blocks::MUD_BRICKS))
        .addLayer(-1..1, 1, 1..3, HTSimpleMultiblockComponent(Blocks::MUD_BRICKS))
        .build()

    @JvmField
    val CUTTING_MACHINE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        // bottom
        .addLayer(-1..1, 0, 1..1, HULL)
        .add(-1, 0, 2, HTSimpleMultiblockComponent(Blocks::STONE_SLAB))
        .add(0, 0, 2, HTSimpleMultiblockComponent(Blocks::STONECUTTER))
        .add(1, 0, 2, HTSimpleMultiblockComponent(Blocks::STONE_SLAB))
        .addLayer(-1..1, 0, 3..3, HULL)
        // middle
        .addLayer(-1..1, 1, 1..1, HTSimpleMultiblockComponent(RagiumBlocks.CHEMICAL_GLASS))
        .addLayer(-1..1, 1, 3..3, HTSimpleMultiblockComponent(RagiumBlocks.CHEMICAL_GLASS))
        // top
        .addLayer(-1..1, 2, 1..3, STORAGE_BLOCK)
        .build()

    @JvmField
    val DISTILLATION_TOWER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, HULL)
        .addCross4(-1..1, 1, 1..3, HTSimpleMultiblockComponent(Blocks::RED_CONCRETE))
        .addCross4(-1..1, 2, 1..3, HTSimpleMultiblockComponent(Blocks::WHITE_CONCRETE))
        .addCross4(-1..1, 3, 1..3, HTSimpleMultiblockComponent(Blocks::RED_CONCRETE))
        .add(0, 4, 2, HTSimpleMultiblockComponent(Blocks::WHITE_CONCRETE))
        .build()

    @JvmField
    val MULTI_SMELTER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, COIL_Y)
        .addLayer(-1..1, 1, 1..3, STORAGE_BLOCK)
        .build()

    @JvmField
    val LARGE_MACHINE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, CASING)
        .addHollow(-1..1, 0, 1..3, HULL)
        .addLayer(-1..1, 1, 1..3, STORAGE_BLOCK)
        .build()

    @JvmField
    val RESOURCE_PLANT: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, 1..3, HULL)
        .addCross4(-1..1, 1, 1..3, GRATE)
        .addCross4(-1..1, 2, 1..3, GRATE)
        .add(0, 3, 2, GRATE)
        .add(0, 4, 2, GRATE)
        .build()
}
