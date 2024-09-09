package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import kotlin.jvm.optionals.getOrDefault

object RagiumItemGroup {

    @JvmField
    val ITEM: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("item"))

    fun init() {
        Registry.register(
            Registries.ITEM_GROUP,
            ITEM,
            FabricItemGroup.builder()
                .displayName(Text.literal(Ragium.MOD_NAME))
                .icon { RagiumItems.RAGI_ALLOY_INGOT.defaultStack }
                .entries { context: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                    context.lookup.getWrapperOrThrow(RegistryKeys.ITEM)
                        .streamEntries()
                        .filter { entry: RegistryEntry.Reference<Item> ->
                            entry.key.map { it.value.namespace == Ragium.MOD_ID }.getOrDefault(false)
                        }
                        .map(RegistryEntry.Reference<Item>::value)
                        .forEach(entries::add)
                }
                .build()
        )

    }

}