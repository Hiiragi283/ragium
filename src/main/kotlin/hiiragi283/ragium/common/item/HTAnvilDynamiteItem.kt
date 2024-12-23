package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.onBlockHit
import hiiragi283.ragium.api.extension.onEntityHit
import hiiragi283.ragium.api.extension.sidedPos
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.Blocks
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class HTAnvilDynamiteItem(settings: Settings) : HTDynamiteItemBase(settings.descriptions(RagiumTranslationKeys.ANVIL_DYNAMITE)) {
    override fun onCollision(entity: HTDynamiteEntity, hitResult: HitResult) {
        val world: World = entity.world
        hitResult
            .onBlockHit { blockResult: BlockHitResult ->
                world.setBlockState(blockResult.sidedPos, Blocks.ANVIL.defaultState)
            }.onEntityHit { entityResult: EntityHitResult ->
                entityResult.entity.damage(world.damageSources.fallingAnvil(entity), 10f)
            }
    }
}
