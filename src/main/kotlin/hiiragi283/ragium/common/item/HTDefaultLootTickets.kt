package hiiragi283.ragium.common.item

import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.ragium.api.item.HTLootTicketTargets
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable

enum class HTDefaultLootTickets(val targets: HTLootTicketTargets) {
    END_CITY(BuiltInLootTables.END_CITY_TREASURE),
    DUNGEON(BuiltInLootTables.SIMPLE_DUNGEON),
    MINESHAFT(BuiltInLootTables.ABANDONED_MINESHAFT),
    NETHER_FORTRESS(BuiltInLootTables.NETHER_BRIDGE),
    STRONGHOLD(
        BuiltInLootTables.STRONGHOLD_LIBRARY,
        BuiltInLootTables.STRONGHOLD_CROSSING,
        BuiltInLootTables.STRONGHOLD_CORRIDOR,
    ),
    DESERT_PYRAMID(BuiltInLootTables.DESERT_PYRAMID),
    TEMPLE(BuiltInLootTables.JUNGLE_TEMPLE, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER),
    IGLOO(BuiltInLootTables.IGLOO_CHEST),
    MANSION(BuiltInLootTables.WOODLAND_MANSION),
    UNDERWATER_RUIN(BuiltInLootTables.UNDERWATER_RUIN_SMALL, BuiltInLootTables.UNDERWATER_RUIN_BIG),
    BURIED_TREASURE(BuiltInLootTables.BURIED_TREASURE),
    SHIPWRECK(
        BuiltInLootTables.SHIPWRECK_MAP,
        BuiltInLootTables.SHIPWRECK_SUPPLY,
        BuiltInLootTables.SHIPWRECK_TREASURE,
    ),
    PILLAGER_OUTPOST(BuiltInLootTables.PILLAGER_OUTPOST),
    BASTION_REMNANT(
        BuiltInLootTables.BASTION_TREASURE,
        BuiltInLootTables.BASTION_OTHER,
        BuiltInLootTables.BASTION_BRIDGE,
        BuiltInLootTables.BASTION_HOGLIN_STABLE,
    ),
    ANCIENT_CITY(BuiltInLootTables.ANCIENT_CITY, BuiltInLootTables.ANCIENT_CITY_ICE_BOX),
    RUINED_PORTAL(BuiltInLootTables.RUINED_PORTAL),
    ;

    companion object {
        @JvmStatic
        private val ticketCache: MutableMap<HTDefaultLootTickets, HTItemResourceType> = hashMapOf()

        @JvmStatic
        fun getLootTicket(lootTicket: HTDefaultLootTickets, count: Int = 1): ItemStack = ticketCache
            .computeIfAbsent(lootTicket) {
                createItemStack(RagiumItems.LOOT_TICKET, RagiumDataComponents.LOOT_TICKET, it.targets).toResource()!!
            }.toStack(count)
    }

    constructor(vararg lootTables: ResourceKey<LootTable>) : this(HTLootTicketTargets.create(*lootTables))
}
