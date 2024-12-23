package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTFlatteningDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTFlatteningDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.FLATTENING_DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.FLATTENING_DYNAMITE,
        x,
        y,
        z,
        world,
    )

    override fun getDefaultItem(): Item = RagiumItemsNew.Dynamites.FLATTENING.asItem()

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            val pos: BlockPos = blockHitResult.blockPos
            val hitY: Int = pos.y
            val minY: Int = when (blockHitResult.side) {
                Direction.UP -> hitY + 1
                else -> hitY
            }
            ChunkPos(pos).forEach(minY..world.height) { pos: BlockPos ->
                world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            }
            discard()
        }
    }
}
