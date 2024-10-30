package hiiragi283.ragium.common.unused

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class HTOblivionCubeEntity(entityType: EntityType<HTOblivionCubeEntity>, world: World) : HostileEntity(entityType, world) {
    companion object {
        @JvmStatic
        fun createAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
    }

    init {
        experiencePoints = 10
    }

    override fun initGoals() {
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(1, PowderSnowJumpGoal(this, this.world))
        goalSelector.add(2, MeleeAttackGoal(this, 1.0, false))
        goalSelector.add(3, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(7, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(8, LookAroundGoal(this))
        targetSelector.add(1, RevengeGoal(this).setGroupRevenge())
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
    }

    override fun getMoveEffect(): MoveEffect = MoveEffect.EVENTS

    override fun getAmbientSound(): SoundEvent? = SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE

    override fun getHurtSound(source: DamageSource?): SoundEvent = SoundEvents.ENTITY_WARDEN_HEARTBEAT

    override fun getDeathSound(): SoundEvent = SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK
}
