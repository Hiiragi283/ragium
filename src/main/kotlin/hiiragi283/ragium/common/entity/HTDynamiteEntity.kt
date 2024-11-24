package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
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

    var action: Action = Action { _: HTDynamiteEntity, _: HitResult -> }

    override fun getDefaultItem(): Item = RagiumItems.DYNAMITE

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            action.onCollision(this, hitResult)
            discard()
        }
    }

    //    Action    //

    fun interface Action {
        fun onCollision(entity: HTDynamiteEntity, hitResult: HitResult)
    }
}
