package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.component.HTExplosionComponent
import hiiragi283.ragium.common.init.RagiumEntityTypes
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ExplosiveProjectileEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class HTEchoBulletEntity : ExplosiveProjectileEntity {
    constructor(type: EntityType<HTEchoBulletEntity>, world: World) : super(type, world)

    constructor(world: World, owner: LivingEntity) : super(
        RagiumEntityTypes.ECHO_BULLET,
        owner,
        owner.pos.offset(owner.facing, 2.0),
        world,
    )

    constructor(world: World, pos: Position, direction: Direction) : super(
        RagiumEntityTypes.ECHO_BULLET,
        pos.x,
        pos.y,
        pos.z,
        Vec3d(direction.offsetX.toDouble(), direction.offsetY.toDouble(), direction.offsetZ.toDouble()),
        world,
    )

    private var isHit: Boolean = false
    private var hitTick: Int = 0

    override fun tick() {
        super.tick()
        if (isHit) {
            hitTick++
        }
        if (hitTick >= 50) {
            discard()
        }
    }

    override fun getGravity(): Double = 0.0

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        hitTick = nbt.getInt("HitTick")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("HitTick", hitTick)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        HTExplosionComponent(4f, true).createExplosion(world, blockHitResult.blockPos)
        isHit = true
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        HTExplosionComponent(4f, true).createExplosion(world, entityHitResult.entity.blockPos)
        isHit = true
    }

    override fun isBurning(): Boolean = false
}
