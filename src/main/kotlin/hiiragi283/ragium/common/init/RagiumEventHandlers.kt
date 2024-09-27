package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium.id
import hiiragi283.ragium.common.Ragium.log
import hiiragi283.ragium.common.advancement.HTAdvancementRewardCallback
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.util.sendTitle
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object RagiumEventHandlers {
    @JvmStatic
    fun init() {
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            log { info("Current advancement; ${entry.id}") }
            if (entry.id == id("tier1/root")) {
                player.sendTitle(Text.literal("Welcome to Heat Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.BRICKS)
            }
            if (entry.id == id("tier2/root")) {
                player.sendTitle(Text.literal("Welcome to Kinetic Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.POLISHED_BLACKSTONE_BRICKS)
            }
            if (entry.id == id("tier3/root")) {
                player.sendTitle(Text.literal("Welcome to Electric Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.END_STONE_BRICKS)
            }
            if (entry.id == id("tier4/root")) {
                player.sendTitle(Text.literal("Welcome to Alchemical Age!"))
                RagiumNetworks.sendFloatingItem(player, RagiElement.RAGIUM.dustItem)
            }
        }

        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
        }
    }
}
