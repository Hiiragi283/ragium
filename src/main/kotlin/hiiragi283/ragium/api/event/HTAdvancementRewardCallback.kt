package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.server.network.ServerPlayerEntity

/**
 * 進捗を解除したときに呼ばれるイベント
 *
 * [net.minecraft.advancement.PlayerAdvancementTracker.rewardEmptyAdvancements]と[net.minecraft.advancement.PlayerAdvancementTracker.grantCriterion]にフックされています。
 */
fun interface HTAdvancementRewardCallback {
    companion object {
        @JvmField
        val EVENT: Event<HTAdvancementRewardCallback> =
            EventFactory.createArrayBacked(HTAdvancementRewardCallback::class.java) { listeners: Array<HTAdvancementRewardCallback> ->
                HTAdvancementRewardCallback { player: ServerPlayerEntity, entry: AdvancementEntry ->
                    listeners.forEach { it.onRewards(player, entry) }
                }
            }
    }

    /**
     * @param player 進捗を解除したプレイヤー
     * @param entry 解除された進捗
     */
    fun onRewards(player: ServerPlayerEntity, entry: AdvancementEntry)
}
