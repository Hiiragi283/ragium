package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEntityTypes
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class HTRemoverDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTRemoverDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.REMOVER_DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.REMOVER_DYNAMITE,
        x,
        y,
        z,
        world,
    )

    override fun getDefaultItem(): Item = RagiumContents.Misc.REMOVER_DYNAMITE.value

    override fun handleStatus(status: Byte) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            repeat(8) {
                world.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    x,
                    y,
                    z,
                    (random.nextDouble() - 0.5) * 0.08,
                    (random.nextDouble() - 0.5) * 0.08,
                    (random.nextDouble() - 0.5) * 0.08,
                )
            }
        }
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES)
            discard()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            stack.get(RagiumComponentTypes.REMOVER_DYNAMITE)?.onBlockHit(world, blockHitResult)
        }
    }
}
