package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBlazingDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTBlazingDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.BLAZING_DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.BLAZING_DYNAMITE,
        x,
        y,
        z,
        world,
    )

    override fun getDefaultItem(): Item = RagiumItems.Dynamites.BLAZING.asItem()

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if (!world.isClient) {
            entityHitResult.entity.setOnFireFromLava()
            discard()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            val hitPos: BlockPos = blockHitResult.blockPos
            val radius: Int = RagiumAPI
                .getInstance()
                .config.utility.dynamitePlaceRadius
            BlockPos
                .stream(hitPos.add(radius, radius, radius), hitPos.add(-radius, -radius, -radius))
                .forEach { posIn: BlockPos ->
                    val state: BlockState = Blocks.FIRE.defaultState
                    val stateAt: BlockState = world.getBlockState(posIn)
                    if (stateAt.isReplaceable && state.canPlaceAt(world, posIn)) {
                        world.setBlockState(posIn, state)
                    }
                }
            discard()
        }
    }
}
