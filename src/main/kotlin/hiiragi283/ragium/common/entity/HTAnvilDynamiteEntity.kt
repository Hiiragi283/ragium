package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.extension.sidedPos
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

class HTAnvilDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTAnvilDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.ANVIL_DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(RagiumEntityTypes.ANVIL_DYNAMITE, x, y, z, world)

    override fun getDefaultItem(): Item = RagiumItemsNew.Dynamites.ANVIL.asItem()

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            world.setBlockState(blockHitResult.sidedPos, Blocks.ANVIL.defaultState)
            discard()
        }
    }
}
