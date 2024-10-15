package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.component.HTDynamiteComponent
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEntityTypes
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class HTDynamiteEntity : ThrownItemEntity {
    constructor(type: EntityType<HTDynamiteEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(RagiumEntityTypes.DYNAMITE, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(RagiumEntityTypes.DYNAMITE, x, y, z, world)

    override fun getDefaultItem(): Item = RagiumContents.Misc.DYNAMITE.value

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            stack
                .getOrDefault(RagiumComponentTypes.DYNAMITE, HTDynamiteComponent.DEFAULT)
                .createExplosion(world, this, x, y, z)
            discard()
        }
    }
}
