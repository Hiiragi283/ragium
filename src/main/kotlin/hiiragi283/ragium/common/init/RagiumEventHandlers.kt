package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium.id
import hiiragi283.ragium.common.Ragium.log
import hiiragi283.ragium.common.advancement.HTAdvancementRewardCallback
import hiiragi283.ragium.common.component.item.HTTooltipsComponent
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.util.sendTitle
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.component.ComponentMap
import net.minecraft.item.Item
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object RagiumEventHandlers {
    @JvmStatic
    fun init() {
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            log { info("Current advancement; ${entry.id}") }
            if (entry.id == id("tier1/root")) {
                RagiumComponentKeys.PROGRESS.get(player).value = HTMachineTier.HEAT
                player.sendTitle(Text.literal("Welcome to Heat Age!"))
            }
            if (entry.id == id("tier2/root")) {
                RagiumComponentKeys.PROGRESS.get(player).value = HTMachineTier.KINETIC
                player.sendTitle(Text.literal("Welcome to Kinetic Age!"))
            }
            if (entry.id == id("tier3/root")) {
                RagiumComponentKeys.PROGRESS.get(player).value = HTMachineTier.ELECTRIC
                player.sendTitle(Text.literal("Welcome to Electric Age!"))
            }
            if (entry.id == id("tier4/root")) {
                RagiumComponentKeys.UNLOCK_ALCHEMY.get(player).value = true
                player.sendTitle(Text.literal("Welcome to Alchemical Age!"))
            }
        }

        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            customTooltip(context, RagiumItems.TWILIGHT_METAL_INGOT, Formatting.GRAY, Formatting.ITALIC)
            customTooltip(context, RagiumItems.SOAP_INGOT, Formatting.GRAY, Formatting.ITALIC)
            customTooltip(context, RagiumItems.RAW_RAGINITE, Formatting.GRAY, Formatting.OBFUSCATED)
            // auto setting
            context.modify(::canSetTooltip) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.TOOLTIPS,
                    HTTooltipsComponent.fromItem(item, Formatting.GRAY),
                )
            }
        }
    }

    @JvmStatic
    private fun customTooltip(context: DefaultItemComponentEvents.ModifyContext, item: Item, vararg formattings: Formatting) {
        context.modify(item) { builder: ComponentMap.Builder ->
            builder.add(
                RagiumComponentTypes.TOOLTIPS,
                HTTooltipsComponent.fromItem(item, *formattings),
            )
        }
    }

    @JvmStatic
    private fun canSetTooltip(item: Item): Boolean = when {
        item !in RagiumItems.REGISTER -> false
        RagiumComponentTypes.TOOLTIPS in item.components -> false
        RagiumComponentTypes.DISABLE_TOOLTIPS in item.components -> false
        else -> true
    }
}
