package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.multiblock.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTSpawnerMultiblockComponent
import hiiragi283.ragium.common.multiblock.HTTagMultiblockComponent
import net.minecraft.world.level.block.Blocks

object RagiumMultiblockMaps {
    private val BRICK_WALL = HTSimpleMultiblockComponent(Blocks::BRICK_WALL)
    private val DEEPSLATE_BRICKS = HTSimpleMultiblockComponent(Blocks::DEEPSLATE_BRICKS)
    private val RAGI_BRICK_WALL = HTSimpleMultiblockComponent(RagiumBlocks.RAGI_BRICK_FAMILY.wall)
    private val RAGI_BRICKS = HTSimpleMultiblockComponent(RagiumBlocks.RAGI_BRICKS)
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
        .add(-1, 0, 1, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
        .add(1, 0, 1, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
        .add(1, 0, 3, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
        .add(-1, 0, 3, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
        // Steel
        .add(0, 0, 1, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, CommonMaterials.STEEL))
        .add(-1, 0, 2, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, CommonMaterials.STEEL))
        .add(1, 0, 2, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, CommonMaterials.STEEL))
        .add(0, 0, 3, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, CommonMaterials.STEEL))
        // Diamond
        .add(0, 0, 2, HTTagMultiblockComponent(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.DIAMOND))
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
