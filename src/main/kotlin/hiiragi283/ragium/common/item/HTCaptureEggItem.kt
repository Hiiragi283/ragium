package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.Level

class HTCaptureEggItem(properties: Properties) : Item(properties) {
    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand,
    ): InteractionResult {
        val entityType: EntityType<*> = interactionTarget.type
        // エンティティがブラックリストに入っていたらパス
        if (entityType.`is`(RagiumModTags.EntityTypes.CAPTURE_BLACKLIST)) {
            return super.interactLivingEntity(stack, player, interactionTarget, usedHand)
        }
        // クリックしたモブのスポーンエッグを取得する
        val spawnEgg: ItemStack = SpawnEggItem.byId(entityType)?.let(::ItemStack)
            ?: return super.interactLivingEntity(stack, player, interactionTarget, usedHand)
        // SEを鳴らす
        player.playSound(SoundEvents.FIREWORK_ROCKET_BLAST)
        // パーティクルを出す
        val level: Level = player.level()
        if (level.isClientSide) {
            Minecraft.getInstance().particleEngine.createTrackingEmitter(interactionTarget, ParticleTypes.FIREWORK)
        }
        // モブを消す
        interactionTarget.discard()
        // このアイテムを減らす
        stack.consume(1, player)
        // スポーンエッグをプレイヤーに渡す
        dropStackAt(player, spawnEgg)
        return InteractionResult.sidedSuccess(player.level().isClientSide)
    }
}
