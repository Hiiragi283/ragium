package hiiragi283.ragium.common.entity

import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
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

    private var action: (HTDynamiteEntity, HitResult) -> Unit = { _: HTDynamiteEntity, _: HitResult -> }

    fun setAction(action: (HTDynamiteEntity, HitResult) -> Unit): HTDynamiteEntity = apply {
        this.action = action
    }

    override fun getDefaultItem(): Item = RagiumItemsNew.Dynamites.SIMPLE.asItem()

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            action(this, hitResult)
            discard()
        }
    }
}
