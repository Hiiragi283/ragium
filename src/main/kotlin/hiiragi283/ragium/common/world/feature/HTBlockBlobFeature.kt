package hiiragi283.ragium.common.world.feature

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext

/**
 * @see [net.minecraft.world.level.levelgen.feature.BlockBlobFeature]
 */
class HTBlockBlobFeature(codec: Codec<HTFilteredBlockConfiguration>) : Feature<HTFilteredBlockConfiguration>(codec) {
    override fun place(context: FeaturePlaceContext<HTFilteredBlockConfiguration>): Boolean {
        var pos: BlockPos = context.origin()
        val level: WorldGenLevel = context.level()
        val random: RandomSource = context.random()
        val config: HTFilteredBlockConfiguration = context.config()

        while (pos.y > level.minBuildHeight + 3) {
            pos = pos.below()
            if (!level.isEmptyBlock(pos)) {
                if (config.predicate.test(level, pos)) {
                    break
                }
            }
        }

        if (pos.y <= level.minBuildHeight + 3) return false
        repeat(3) {
            val i: Int = random.nextInt(2)
            val j: Int = random.nextInt(2)
            val k: Int = random.nextInt(2)
            val f: Float = (i + j + k) * 0.333f + 0.5f

            for (pos1: BlockPos in BlockPos.betweenClosed(pos.offset(-i, -j, -j), pos.offset(i, j, k))) {
                if (pos1.distSqr(pos) <= (f * f)) {
                    level.setBlock(pos1, config.provider.getState(random, pos1), 3)
                }
            }

            // pos = pos.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2))
        }
        return true
    }
}
