package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.extension.giveOrDropStack
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class HTThrownCaptureEgg : ThrowableItemProjectile {
    companion object {
        @JvmStatic
        fun getCapturedStack(target: Entity): ItemStack? {
            val targetType: EntityType<*> = target.type
            // 対象がブラックリストに入っていたらパス
            if (targetType.`is`(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST)) return null
            // クリックしたモブのスポーンエッグを取得する
            return SpawnEggItem.byId(targetType)?.let(::ItemStack)
        }
    }

    constructor(entityType: EntityType<out HTThrownCaptureEgg>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(RagiumEntityTypes.ELDRITCH_EGG.get(), shooter, level)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.ELDRITCH_EGG.get(),
        x,
        y,
        z,
        level,
    )

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        if (!level().isClientSide) {
            val target: Entity = result.entity
            // クリックしたモブのスポーンエッグを取得する
            val spawnEgg: ItemStack = getCapturedStack(target) ?: return
            // SEを鳴らす
            playSound(SoundEvents.FIREWORK_ROCKET_BLAST)
            // パーティクルを出す
            if (level().isClientSide) {
                Minecraft.getInstance().particleEngine.createTrackingEmitter(target, ParticleTypes.FIREWORK)
            }
            // 対象を消す
            target.discard()
            // スポーンエッグをプレイヤーに渡す
            giveOrDropStack(owner ?: this, spawnEgg)
            // 自身を消す
            discard()
        }
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (!level().isClientSide) {
            // アイテムに戻る
            giveOrDropStack(owner ?: this, item)
            // 自身を消す
            discard()
        }
    }

    override fun getDefaultItem(): Item = RagiumItems.ELDRITCH_EGG.get()
}
