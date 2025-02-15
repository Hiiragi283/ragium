package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.multiblock.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTSpawnerMultiblockComponent
import net.minecraft.world.level.block.Blocks

object RagiumMultiblockMaps {
    private val BRICK_WALL = HTSimpleMultiblockComponent(Blocks::BRICK_WALL)
    private val TERRACOTTA = HTSimpleMultiblockComponent(Blocks::TERRACOTTA)

    private val LBF_WALL = HTSimpleMultiblockComponent(RagiumBlocks.RAGI_BRICK_FAMILY.wall)
    private val LBF_PILLAR = HTSimpleMultiblockComponent(RagiumBlocks.RAGI_BRICKS)

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
        .addLayer(-1..1, 0, 1..3, HTSimpleMultiblockComponent(Blocks::DEEPSLATE_BRICKS))
        .addPillar(0, 1..3, 1, LBF_PILLAR)
        .addPillar(0, 1..3, 3, LBF_PILLAR)
        .addPillar(1, 1..3, 2, LBF_PILLAR)
        .addPillar(1, 1..3, 1, LBF_WALL)
        .addPillar(1, 1..3, 3, LBF_WALL)
        .addPillar(-1, 1..3, 2, LBF_PILLAR)
        .addPillar(-1, 1..3, 1, LBF_WALL)
        .addPillar(-1, 1..3, 3, LBF_WALL)
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
