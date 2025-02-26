package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.multiblock.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTSpawnerMultiblockComponent
import net.minecraft.world.level.block.Blocks

object RagiumMultiblockMaps {
    private val BRICK_WALL = HTSimpleMultiblockComponent(Blocks::BRICK_WALL)
    private val DEEPSLATE_BRICKS = HTSimpleMultiblockComponent(Blocks::DEEPSLATE_BRICKS)
    private val DIAMOND_BLOCK = HTSimpleMultiblockComponent(Blocks::DIAMOND_BLOCK)
    private val IRON_BLOCK = HTSimpleMultiblockComponent(Blocks::IRON_BLOCK)
    private val RAGI_BRICK_WALL = HTSimpleMultiblockComponent(RagiumBlocks.RAGI_BRICK_FAMILY.wall)
    private val RAGI_BRICKS = HTSimpleMultiblockComponent(RagiumBlocks.RAGI_BRICKS)
    private val STEEL_BLOCK = HTSimpleMultiblockComponent { RagiumBlocks.STORAGE_BLOCKS[CommonMaterials.STEEL]!!.get() }
    private val TERRACOTTA = HTSimpleMultiblockComponent(Blocks::TERRACOTTA)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, 1..3, HTSimpleMultiblockComponent(Blocks::BRICKS))
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
        .addLayer(-1..1, 0, 1..3, DEEPSLATE_BRICKS)
        .addPillar(0, 1..3, 1, RAGI_BRICKS)
        .addPillar(0, 1..3, 3, RAGI_BRICKS)
        .addPillar(1, 1..3, 2, RAGI_BRICKS)
        .addPillar(1, 1..3, 1, RAGI_BRICK_WALL)
        .addPillar(1, 1..3, 3, RAGI_BRICK_WALL)
        .addPillar(-1, 1..3, 2, RAGI_BRICKS)
        .addPillar(-1, 1..3, 1, RAGI_BRICK_WALL)
        .addPillar(-1, 1..3, 3, RAGI_BRICK_WALL)
        .build()

    @JvmField
    val CRUSHER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, -1, 1..3, DEEPSLATE_BRICKS)
        .addLayer(-1..1, 1, 1..3, RAGI_BRICKS)
        // Iron
        .add(-1, 0, 1, IRON_BLOCK)
        .add(1, 0, 1, IRON_BLOCK)
        .add(1, 0, 3, IRON_BLOCK)
        .add(-1, 0, 3, IRON_BLOCK)
        // Steel
        .add(0, 0, 1, STEEL_BLOCK)
        .add(-1, 0, 2, STEEL_BLOCK)
        .add(1, 0, 2, STEEL_BLOCK)
        .add(0, 0, 3, STEEL_BLOCK)
        // Diamond
        .add(0, 0, 2, DIAMOND_BLOCK)
        .build()

    @JvmField
    val SPAWNER: HTMultiblockMap.Relative = HTMultiblockMap
        .builder()
        .addLayer(-1..1, 0, -1..1, HTSimpleMultiblockComponent(Blocks::NETHER_BRICKS))
        .add(0, -1, 0, HTSpawnerMultiblockComponent)
        .add(1, -1, 1, HTSimpleMultiblockComponent(Blocks::CHISELED_NETHER_BRICKS))
        .add(1, -1, -1, HTSimpleMultiblockComponent(Blocks::CHISELED_NETHER_BRICKS))
        .add(-1, -1, 1, HTSimpleMultiblockComponent(Blocks::CHISELED_NETHER_BRICKS))
        .add(-1, -1, -1, HTSimpleMultiblockComponent(Blocks::CHISELED_NETHER_BRICKS))
        .addLayer(-1..1, -2, -1..1, HTSimpleMultiblockComponent(Blocks::NETHER_BRICKS))
        .build()
}
