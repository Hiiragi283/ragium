package hiiragi283.ragium.integration.jei.entry

import com.mojang.serialization.Codec
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

data class HTStirlingFuelEntry(val item: Holder<Item>) {
    companion object {
        @JvmField
        val CODEC: Codec<HTStirlingFuelEntry> =
            RegistryFixedCodec.create(Registries.ITEM).xmap(::HTStirlingFuelEntry, HTStirlingFuelEntry::item)
    }

    val requiredWater: Int = (item.getData(NeoForgeDataMaps.FURNACE_FUELS)?.burnTime ?: error("Invalid item")) / 10
}
