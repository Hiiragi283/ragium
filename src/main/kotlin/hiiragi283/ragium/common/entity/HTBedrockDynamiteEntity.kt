package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

class HTBedrockDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTBedrockDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.BEDROCK_DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.BEDROCK_DYNAMITE,
        x,
        y,
        z,
        world,
    )

    override fun getDefaultItem(): Item = RagiumItemsNew.Dynamites.BEDROCK.asItem()

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            val bottomY: Int = world.bottomY
            ChunkPos(blockHitResult.blockPos).forEach(bottomY + 1..bottomY + 5) { pos: BlockPos ->
                if (world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                    world.removeBlock(pos, false)
                }
            }
            discard()
        }
    }
}
