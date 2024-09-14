package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.fluid.HTFluidContent
import hiiragi283.ragium.common.item.HTPortableScreenType
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.util.getFilteredInstances
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text

object RagiumItemGroup {

    @JvmField
    val BLOCK: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("block"))

    @JvmField
    val ITEM: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("item"))

    @JvmStatic
    fun init() {
        register(BLOCK) {
            displayName(Text.translatable("itemGroup.ragium.block"))
            icon { RagiumBlocks.RAGINITE_ORE.asItem().defaultStack }
            entries { context: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                RagiumBlocks
                    .getFilteredInstances<Block>()
                    .forEach(entries::add)
                HTMachineType
                    .getEntries()
                    .map(HTMachineType::block)
                    .forEach(entries::add)
            }
        }
        register(ITEM) {
            displayName(Text.translatable("itemGroup.ragium.item"))
            icon { RagiumItems.RAGI_ALLOY_INGOT.defaultStack }
            entries { context: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                RagiumItems
                    .getFilteredInstances<Item>()
                    .forEach(entries::add)
                HTPortableScreenType
                    .entries
                    .forEach(entries::add)
                RagiumFluids
                    .getFilteredInstances<HTFluidContent>()
                    .map(HTFluidContent::bucketItem)
                    .forEach(entries::add)
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
            FabricItemGroup.builder().apply(action).build()
        )
    }

}