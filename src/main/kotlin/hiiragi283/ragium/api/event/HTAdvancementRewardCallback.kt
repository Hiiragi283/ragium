package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

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
                HTAdvancementRewardCallback { helper: Helper ->
                    listeners.forEach { it.onRewards(helper) }
                }
            }
    }

    fun onRewards(helper: Helper)

    /**
     * @param player 進捗を解除したプレイヤー
     * @param entry 解除された進捗
     */
    data class Helper(private val player: ServerPlayerEntity, private val entry: AdvancementEntry) {
        /**
         * 指定した[filter]にプレイヤーが適している場合のみ[action]を実行します。
         */
        fun onMatchingPlayer(filter: (ServerPlayerEntity) -> Boolean, action: (ServerPlayerEntity, AdvancementEntry) -> Unit) {
            if (filter(player)) action(player, entry)
        }

        /**
         * 指定した[id]に進捗が一致した場合のみ[action]を実行します。
         */
        fun onMatchingEntry(id: Identifier, action: (ServerPlayerEntity, AdvancementEntry) -> Unit) {
            if (entry.id == id) action(player, entry)
        }
    }
}
