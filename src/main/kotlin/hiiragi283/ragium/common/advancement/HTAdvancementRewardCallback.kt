package hiiragi283.ragium.common.advancement

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.server.network.ServerPlayerEntity

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

    fun onRewards(player: ServerPlayerEntity, entry: AdvancementEntry)
}
