package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.init.RagiumEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class HTFlare : ThrowableItemProjectile {
    constructor(type: EntityType<out HTFlare>, level: Level) : super(type, level)

    constructor(level: Level, shooter: LivingEntity) : super(RagiumEntityTypes.FLARE.get(), shooter, level)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(RagiumEntityTypes.FLARE.get(), x, y, z, level)

    override fun getDefaultItem(): Item = Items.LIGHT

    init {
        isNoGravity = true
    }

    // Add Glowing Effect
    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        if (!level().isClientSide) {
            val target: Entity = result.entity
            if (target is LivingEntity) {
                target.addEffect(MobEffectInstance(MobEffects.GLOWING, 20 * 10))
                discard()
            }
        }
    }

    // Place Light Block
    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (!level().isClientSide) {
            val pos: BlockPos = result.blockPos
            val side: Direction = result.direction
            level().setBlockAndUpdate(pos.relative(side), Blocks.LIGHT.defaultBlockState())
            discard()
        }
    }
}
