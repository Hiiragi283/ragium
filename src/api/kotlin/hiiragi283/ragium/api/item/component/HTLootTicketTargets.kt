package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.collection.randomOrNull
import hiiragi283.ragium.api.item.HTTooltipProvider
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.text.RagiumTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.storage.loot.LootTable

@ConsistentCopyVisibility
data class HTLootTicketTargets private constructor(private val lootTables: List<ResourceKey<LootTable>>) : HTTooltipProvider {
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

    override fun addToTooltip(context: Item.TooltipContext, consumer: (Component) -> Unit, flag: TooltipFlag) {
        lootTables
            .asSequence()
            .map(ResourceKey<LootTable>::location)
            .map(ResourceLocation::toString)
            .map { RagiumTranslation.TOOLTIP_LOOT_TABLE_ID.getColoredComponent(ChatFormatting.YELLOW, it) }
            .forEach(consumer)
    }
}
