package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.server.network.ServerPlayerEntity

/**
 * Callback for unlocking advancement, is hooked in giving rewards at [net.minecraft.advancement.PlayerAdvancementTracker.rewardEmptyAdvancements] and [net.minecraft.advancement.PlayerAdvancementTracker.grantCriterion]
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
     * @param player a player which unlocked the advancement
     * @param entry a [AdvancementEntry] which [player] unlocked
     */
    fun onRewards(player: ServerPlayerEntity, entry: AdvancementEntry)
}
