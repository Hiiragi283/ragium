package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

object RagiumItemGroup {
    @JvmField
    val ITEM_KEY: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, RagiumAPI.id("item"))

    @JvmField
    val FLUID_KEY: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, RagiumAPI.id("fluid"))

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
                RagiumContents.Gems.RAGIUM
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                buildList {
                    addAll(RagiumContents.Ores.entries)
                    addAll(RagiumContents.StorageBlocks.entries)
                    addAll(RagiumBlocks.NATURAL)
                    addAll(RagiumBlocks.BUILDINGS)

                    addAll(RagiumContents.Dusts.entries)
                    addAll(RagiumContents.Gems.entries)
                    addAll(RagiumContents.Ingots.entries)
                    if (RagiumAPI.getInstance().config.isHardMode) {
                        addAll(RagiumContents.Plates.entries)
                    }
                    addAll(RagiumContents.RawMaterials.entries)

                    addAll(RagiumBlocks.FOODS)
                    addAll(RagiumItems.FOODS)

                    addAll(RagiumItems.ARMORS)
                    addAll(RagiumItems.TOOLS)

                    addAll(RagiumItems.INGREDIENTS)

                    addAll(RagiumBlocks.MISC)
                    addAll(RagiumItems.MISC)

                    remove(RagiumItems.BACKPACK)
                    remove(RagiumItems.FILLED_FLUID_CUBE)
                }.forEach(entries::add)

                DyeColor.entries.map(HTBackpackItem::createStack).forEach(entries::add)
            }
        }

        register(FLUID_KEY) {
            displayName(Text.translatable("itemGroup.ragium.fluid"))
            icon { RagiumItems.EMPTY_FLUID_CUBE.defaultStack }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                Registries.FLUID
                    .filter { it.isStill(it.defaultState) }
                    .map(RagiumAPI.getInstance()::createFilledCube)
                    .forEach(entries::add)
            }
        }

        register(MACHINE_KEY) {
            displayName(Text.translatable("itemGroup.ragium.machine"))
            icon { RagiumMachineKeys.ALLOY_FURNACE.createItemStack(HTMachineTier.PRIMITIVE) }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                buildList {
                    addAll(RagiumContents.Grates.entries)
                    addAll(RagiumContents.Casings.entries)
                    addAll(RagiumContents.Hulls.entries)
                    addAll(RagiumContents.Coils.entries)

                    addAll(RagiumContents.Drums.entries)
                    addAll(RagiumContents.Exporters.entries)
                    addAll(RagiumContents.Pipes.entries)
                    addAll(RagiumContents.CrossPipes.entries)

                    addAll(RagiumContents.CircuitBoards.entries)
                    addAll(RagiumContents.Circuits.entries)

                    addAll(RagiumBlocks.MECHANICS)
                }.forEach(entries::add)
                RagiumAPI
                    .getInstance()
                    .machineRegistry
                    .blocks
                    .forEach(entries::add)
            }
        }
    }
}
