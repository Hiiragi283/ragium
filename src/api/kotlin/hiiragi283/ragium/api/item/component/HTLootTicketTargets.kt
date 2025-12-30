package hiiragi283.ragium.api.item.component

import hiiragi283.core.api.collection.randomOrNull
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.storage.loot.LootTable

@ConsistentCopyVisibility
data class HTLootTicketTargets private constructor(val lootTables: List<ResourceKey<LootTable>>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTLootTicketTargets> = VanillaBiCodecs
            .resourceKey(Registries.LOOT_TABLE)
            .listOrElement()
            .xmap(::HTLootTicketTargets, HTLootTicketTargets::lootTables)

        @JvmField
        val EMPTY = HTLootTicketTargets(listOf())

        @JvmStatic
        fun create(lootTables: List<ResourceKey<LootTable>>): HTLootTicketTargets =
            lootTables.takeUnless(List<ResourceKey<LootTable>>::isEmpty)?.let(::HTLootTicketTargets) ?: EMPTY

        @JvmStatic
        fun create(vararg lootTables: ResourceKey<LootTable>): HTLootTicketTargets = create(lootTables.toList())
    }

    fun getRandomLoot(random: RandomSource): ResourceKey<LootTable>? = lootTables.randomOrNull(random)
}
