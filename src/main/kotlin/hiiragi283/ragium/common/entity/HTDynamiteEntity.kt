package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class HTDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(RagiumEntityTypes.DYNAMITE, x, y, z, world)

    override fun getDefaultItem(): Item = RagiumItemsNew.Dynamites.SIMPLE.asItem()

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            val pos: Vec3d = hitResult.pos
            stack
                .getOrDefault(HTExplosionComponent.COMPONENT_TYPE, HTExplosionComponent.DEFAULT)
                .createExplosion(world, this, pos.x, pos.y, pos.z)
            discard()
        }
    }
}
