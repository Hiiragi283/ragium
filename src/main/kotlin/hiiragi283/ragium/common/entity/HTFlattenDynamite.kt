package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3i

class HTFlattenDynamite : HTDynamite {
    constructor(type: EntityType<out HTFlattenDynamite>, level: Level) : super(type, level)

    constructor(level: Level, shooter: LivingEntity) : super(RagiumEntityTypes.FLATTEN_DYNAMITE.get(), level, shooter)

    override fun getDefaultItem(): Item = RagiumItems.FLATTEN_DYNAMITE.get()

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: Level = level()
        if (!level.isClientSide) {
            val hitPos = BlockPos(result.location.toVec3i())
            val chunkPos = ChunkPos(hitPos)
            for (y: Int in (hitPos.y..level.maxBuildHeight)) {
                for (x: Int in (chunkPos.minBlockX..chunkPos.maxBlockX)) {
                    for (z: Int in (chunkPos.minBlockZ..chunkPos.maxBlockZ)) {
                        level.removeBlock(BlockPos(x, y, z), false)
                    }
                }
            }
            discard()
        }
    }
}
