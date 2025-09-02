package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable

object HTLootTicketHelper {
    @JvmField
    val DEFAULT_LOOT_IDS: List<ResourceKey<LootTable>> = listOf(
        BuiltInLootTables.END_CITY_TREASURE,
        BuiltInLootTables.SIMPLE_DUNGEON,
        BuiltInLootTables.ABANDONED_MINESHAFT,
        BuiltInLootTables.NETHER_BRIDGE,
        // Stronghold
        BuiltInLootTables.DESERT_PYRAMID,
        BuiltInLootTables.JUNGLE_TEMPLE,
        BuiltInLootTables.IGLOO_CHEST,
        BuiltInLootTables.WOODLAND_MANSION,
        // Underwater Ruin
        BuiltInLootTables.BURIED_TREASURE,
        BuiltInLootTables.SHIPWRECK_TREASURE,
        BuiltInLootTables.BASTION_TREASURE,
        BuiltInLootTables.ANCIENT_CITY,
        BuiltInLootTables.RUINED_PORTAL,
        // Trial Chamber
        // Fishing
    )

    @JvmStatic
    fun getDefaultLootTickets(): Map<ResourceKey<LootTable>, ItemStack> = DEFAULT_LOOT_IDS.associateWith(::createTicket)

    @JvmStatic
    fun getLootTicket(lootTableKey: ResourceKey<LootTable>): ItemStack = createTicket(lootTableKey)

    @JvmStatic
    private fun createTicket(lootTableKey: ResourceKey<LootTable>): ItemStack = createItemStack(RagiumItems.LOOT_TICKET, 1) {
        set(RagiumDataComponents.LOOT_TABLE_ID, lootTableKey)
    }
}
