package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumEntityTypes
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
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

    override fun getDefaultItem(): Item = RagiumContents.Misc.BEDROCK_DYNAMITE.value

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            val pos: Vec3d = hitResult.pos
            val chunkPos = ChunkPos(pos.x.toInt(), pos.z.toInt())
            BlockPos
                .iterate(
                    chunkPos.startX,
                    world.bottomY + 5,
                    chunkPos.startZ,
                    chunkPos.endX,
                    world.bottomY,
                    chunkPos.endZ,
                ).forEach { posIn: BlockPos ->
                    if (world.getBlockState(posIn).isOf(Blocks.BEDROCK)) {
                        world.breakBlock(posIn, false)
                    }
                }
            discard()
        }
    }
}
