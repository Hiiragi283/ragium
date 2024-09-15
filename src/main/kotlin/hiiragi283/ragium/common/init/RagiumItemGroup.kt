package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.item.HTPortableScreenType
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text

object RagiumItemGroup {
    // val BLOCK: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("block"))

    @JvmField
    val ITEM: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("item"))

    @JvmStatic
    fun init() {
        register(ITEM) {
            displayName(Text.translatable(RagiumTranslationKeys.ITEM_GROUP_ITEM))
            icon { RagiumItems.RAGI_ALLOY_INGOT.defaultStack }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                RagiumBlocks.REGISTER.forEach(entries::add)
                HTMachineType.getEntries().map(HTMachineType::block).forEach(entries::add)
                RagiumItems.REGISTER.forEach(entries::add)
                HTPortableScreenType.entries.forEach(entries::add)
            }
        }
        /*Registry.register(
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
        )*/
    }

    private inline fun register(key: RegistryKey<ItemGroup>, action: ItemGroup.Builder.() -> Unit) {
        Registry.register(
            Registries.ITEM_GROUP,
            key,
            FabricItemGroup.builder().apply(action).build(),
        )
    }
}
