package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.onBlockHit
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.Blocks
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

class HTBedrockDynamiteItem(settings: Settings) : HTDynamiteItemBase(settings.descriptions(RagiumTranslationKeys.BEDROCK_DYNAMITE)) {
    override fun onCollision(entity: HTDynamiteEntity, hitResult: HitResult) {
        hitResult.onBlockHit { blockResult: BlockHitResult ->
            val world: World = entity.world
            val bottomY: Int = world.bottomY
            ChunkPos(blockResult.blockPos).forEach(bottomY + 1..bottomY + 5) { pos ->
                if (world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                    world.removeBlock(pos, false)
                }
            }
        }
    }
}
