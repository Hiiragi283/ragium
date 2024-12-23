package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.onBlockHit
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTFlatteningDynamiteItem(settings: Settings) :
    HTDynamiteItemBase(settings.descriptions(RagiumTranslationKeys.FLATTENING_DYNAMITE)) {
    override fun onCollision(entity: HTDynamiteEntity, hitResult: HitResult) {
        hitResult.onBlockHit { blockResult: BlockHitResult ->
            val world: World = entity.world
            val pos: BlockPos = blockResult.blockPos
            val hitY: Int = pos.y
            val minY: Int = when (blockResult.side) {
                Direction.UP -> hitY + 1
                else -> hitY
            }
            ChunkPos(pos).forEach(minY..world.height) { pos: BlockPos ->
                world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            }
        }
    }
}
