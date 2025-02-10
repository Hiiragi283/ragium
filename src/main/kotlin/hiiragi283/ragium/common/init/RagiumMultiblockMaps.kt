package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.multiblock.HTAxisMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTSimpleMultiblockComponent
import net.minecraft.world.level.block.Blocks

object RagiumMultiblockMaps {
    private val GRATE = HTSimpleMultiblockComponent(Blocks.COPPER_GRATE)
    private val SHAFT = HTAxisMultiblockComponent.YStatic(RagiumBlocks.SHAFT)
    private val SHAFT_HORIZONTAL = HTAxisMultiblockComponent.FrontHorizontal(RagiumBlocks.SHAFT)
    private val SHAFT_VERTICAL = HTAxisMultiblockComponent.FrontVertical(RagiumBlocks.SHAFT)

    private val BRICK_WALL = HTSimpleMultiblockComponent(Blocks.BRICK_WALL)
    private val TERRACOTTA = HTSimpleMultiblockComponent(Blocks.TERRACOTTA)

    private val DEEP_WALL = HTSimpleMultiblockComponent(Blocks.POLISHED_DEEPSLATE_WALL)
    private val DEEP_TILE = HTSimpleMultiblockComponent(Blocks.DEEPSLATE_TILES)

    @JvmField
    val BEDROCK_MINER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        // drill
        .add(0, -3, 0, HTSimpleMultiblockComponent(Blocks.BEDROCK))
        .add(0, -2, 0, SHAFT)
        .add(0, -1, 0, SHAFT)
        .add(-1, 0, 0, SHAFT_VERTICAL)
        .add(0, 0, -1, SHAFT_HORIZONTAL)
        .add(0, 0, 1, SHAFT_HORIZONTAL)
        .add(1, 0, 0, SHAFT_VERTICAL)
        // frame
        // .add(-2, -1, 0, CASING)
        // .add(0, -1, -2, CASING)
        // .add(0, -1, 2, CASING)
        // .add(2, -1, 0, CASING)
        // .add(-2, 0, 0, HULL)
        // .add(0, 0, -2, HULL)
        // .add(0, 0, 2, HULL)
        // .add(2, 0, 0, HULL)
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
        // .add(-2, 1, 0, STORAGE_BLOCK)
        // .add(0, 1, -2, STORAGE_BLOCK)
        // .add(0, 1, 2, STORAGE_BLOCK)
        // .add(2, 1, 0, STORAGE_BLOCK)
        .build()

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, 1..3, HTSimpleMultiblockComponent(Blocks.BRICKS))
        .addPillar(0, 1..3, 1, TERRACOTTA)
        .addPillar(0, 1..3, 3, TERRACOTTA)
        .addPillar(1, 1..3, 2, TERRACOTTA)
        .addPillar(1, 1..3, 1, BRICK_WALL)
        .addPillar(1, 1..3, 3, BRICK_WALL)
        .addPillar(-1, 1..3, 2, TERRACOTTA)
        .addPillar(-1, 1..3, 1, BRICK_WALL)
        .addPillar(-1, 1..3, 3, BRICK_WALL)
        .build()

    @JvmField
    val BLAST_FURNACE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, 1..3, HTSimpleMultiblockComponent(Blocks.DEEPSLATE_BRICKS))
        .addPillar(0, 1..3, 1, DEEP_TILE)
        .addPillar(0, 1..3, 3, DEEP_TILE)
        .addPillar(1, 1..3, 2, DEEP_TILE)
        .addPillar(1, 1..3, 1, DEEP_WALL)
        .addPillar(1, 1..3, 3, DEEP_WALL)
        .addPillar(-1, 1..3, 2, DEEP_TILE)
        .addPillar(-1, 1..3, 1, DEEP_WALL)
        .addPillar(-1, 1..3, 3, DEEP_WALL)
        .build()
}
