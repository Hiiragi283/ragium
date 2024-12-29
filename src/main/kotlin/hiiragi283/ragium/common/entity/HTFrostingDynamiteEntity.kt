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
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTFrostingDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTFrostingDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.FROSTING_DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.FROSTING_DYNAMITE,
        x,
        y,
        z,
        world,
    )

    override fun getDefaultItem(): Item = RagiumItems.Dynamites.FROSTING.asItem()

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        placeSnow(entityHitResult.entity.blockPos)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        placeSnow(blockHitResult.blockPos)
    }

    private fun placeSnow(hitPos: BlockPos) {
        if (!world.isClient) {
            val radius: Int = RagiumAPI
                .getInstance()
                .config.utility.dynamitePlaceRadius
            BlockPos.stream(hitPos.add(radius, 0, radius), hitPos.add(-radius, 0, -radius)).forEach { posIn: BlockPos ->
                val state: BlockState = Blocks.POWDER_SNOW.defaultState
                val stateAt: BlockState = world.getBlockState(posIn)
                if (stateAt.isReplaceable && state.canPlaceAt(world, posIn)) {
                    world.setBlockState(
                        posIn,
                        when {
                            world.getFluidState(posIn).isIn(FluidTags.WATER) -> Blocks.ICE.defaultState
                            else -> state
                        },
                    )
                }
            }
            discard()
        }
    }
}
