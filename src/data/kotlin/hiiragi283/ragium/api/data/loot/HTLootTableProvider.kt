package hiiragi283.ragium.api.data.loot

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments

interface HTLootTableProvider : LootTableSubProvider {
    val enchLookup: HolderLookup.RegistryLookup<Enchantment>

    val fortune: Holder.Reference<Enchantment> get() = enchLookup.getOrThrow(Enchantments.FORTUNE)
    val silkTouch: Holder.Reference<Enchantment> get() = enchLookup.getOrThrow(Enchantments.SILK_TOUCH)
}
