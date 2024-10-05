package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.util.dropStackAt
import hiiragi283.ragium.common.util.sendTitle
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.Items
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object RagiumEventHandlers {
    @JvmStatic
    fun init() {
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            RagiumAPI.log { info("Current advancement; ${entry.id}") }
            if (entry.id == RagiumAPI.id("tier1/root")) {
                player.sendTitle(Text.literal("Welcome to Heat Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier2/root")) {
                player.sendTitle(Text.literal("Welcome to Kinetic Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.POLISHED_BLACKSTONE_BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier3/root")) {
                player.sendTitle(Text.literal("Welcome to Electric Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.END_STONE_BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier4/root")) {
                player.sendTitle(Text.literal("Welcome to Alchemical Age!"))
                RagiumNetworks.sendFloatingItem(player, RagiElement.RAGIUM.dustItem)
            }
        }

        // DefaultItemComponentEvents
        ServerLivingEntityEvents.AFTER_DEATH.register { entity: LivingEntity, damage: DamageSource ->
            if (entity.type.isIn(EntityTypeTags.UNDEAD) && damage.isIn(DamageTypeTags.IS_PLAYER_ATTACK)) {
                dropStackAt(entity, Items.NETHER_STAR.defaultStack)
            }
        }
    }
}
