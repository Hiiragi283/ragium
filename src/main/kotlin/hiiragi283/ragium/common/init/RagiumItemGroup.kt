package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text

object RagiumItemGroup {
    @JvmField
    val ITEM_KEY: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, RagiumAPI.id("item"))

    @JvmField
    val MACHINE_KEY: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, RagiumAPI.id("machine"))

    @JvmStatic
    private inline fun register(key: RegistryKey<ItemGroup>, action: ItemGroup.Builder.() -> Unit): ItemGroup =
        Registry.register(Registries.ITEM_GROUP, key, FabricItemGroup.builder().apply(action).build())

    @JvmStatic
    fun init() {
        register(ITEM_KEY) {
            displayName(Text.translatable("itemGroup.ragium.item"))
            icon {
                RagiumContents.Ingots.RAGI_STEEL
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                Registries.ITEM
                    .streamEntries()
                    .filter { it.registryKey().value.namespace == RagiumAPI.MOD_ID }
                    .map(RegistryEntry.Reference<Item>::value)
                    .filter { it != RagiumContents.META_MACHINE.asItem() }
                    .forEach(entries::add)
            }
        }

        register(MACHINE_KEY) {
            displayName(Text.translatable("itemGroup.ragium.machine"))
            icon {
                RagiumContents.META_MACHINE
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                HTMachineTier.entries.forEach { tier: HTMachineTier ->
                    RagiumAPI
                        .getInstance()
                        .machineTypeRegistry.types
                        .map { type: HTMachineType -> type.createItemStack(tier) }
                        .forEach(entries::add)
                }
            }
        }
    }
}
