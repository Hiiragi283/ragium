package hiiragi283.ragium.api.item.component

import com.mojang.serialization.Codec
import hiiragi283.core.api.collection.randomOrNull
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.serialization.network.listOf
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.storage.loot.LootTable

@ConsistentCopyVisibility
data class HTLootTicketTargets private constructor(val lootTables: List<ResourceKey<LootTable>>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTLootTicketTargets> = HTCodecs
            .resourceKey(Registries.LOOT_TABLE)
            .listOrElement()
            .xmap(::HTLootTicketTargets, HTLootTicketTargets::lootTables)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTLootTicketTargets> = HTStreamCodecs
            .resourceKey(Registries.LOOT_TABLE)
            .listOf()
            .map(::HTLootTicketTargets, HTLootTicketTargets::lootTables)

        @JvmField
        val EMPTY = HTLootTicketTargets(listOf())

        @JvmStatic
        fun create(lootTables: List<ResourceKey<LootTable>>): HTLootTicketTargets = lootTables.takeUnless(List<ResourceKey<LootTable>>::isEmpty)?.let(::HTLootTicketTargets) ?: EMPTY

        @JvmStatic
        fun create(vararg lootTables: ResourceKey<LootTable>): HTLootTicketTargets = create(lootTables.toList())
    }

    fun getRandomLoot(random: RandomSource): ResourceKey<LootTable>? = lootTables.randomOrNull(random)
}
