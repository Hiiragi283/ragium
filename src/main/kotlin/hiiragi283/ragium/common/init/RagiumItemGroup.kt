package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
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

    private fun ItemGroup.Entries.addAll(items: List<ItemConvertible>) {
        items.forEach(this::add)
    }

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
                entries.addAll(RagiumContents.Ores.entries)
                entries.addAll(RagiumContents.StorageBlocks.entries)
                entries.addAll(RagiumBlocks.NATURAL)

                entries.addAll(RagiumBlocks.Stones.entries)
                entries.addAll(RagiumBlocks.Slabs.entries)
                entries.addAll(RagiumBlocks.Stairs.entries)
                entries.addAll(RagiumBlocks.BUILDINGS)

                entries.addAll(RagiumContents.Dusts.entries)
                entries.addAll(RagiumContents.Gears.entries)
                entries.addAll(RagiumContents.Gems.entries)
                entries.addAll(RagiumContents.Ingots.entries)
                if (RagiumAPI.getInstance().config.isHardMode) {
                    entries.addAll(RagiumContents.Plates.entries)
                }
                entries.addAll(RagiumContents.RawMaterials.entries)

                entries.addAll(RagiumBlocks.FOODS)
                entries.addAll(RagiumItems.FOODS)

                entries.addAll(RagiumItems.ARMORS)
                entries.addAll(
                    buildList<Item> {
                        addAll(RagiumItems.TOOLS)
                        remove(RagiumItems.BACKPACK)
                        remove(RagiumItems.FILLED_FLUID_CUBE)
                    },
                )

                entries.addAll(RagiumItems.INGREDIENTS)

                entries.addAll(RagiumBlocks.MISC)
                entries.addAll(RagiumItems.MISC)

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
            icon { RagiumMachineKeys.ASSEMBLER.createItemStack(HTMachineTier.PRIMITIVE) }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                entries.addAll(RagiumContents.Grates.entries)
                entries.addAll(RagiumContents.Casings.entries)
                entries.addAll(RagiumContents.Hulls.entries)
                entries.addAll(RagiumContents.Coils.entries)

                entries.addAll(RagiumContents.Crates.entries)
                entries.add(RagiumBlocks.CREATIVE_CRATE)
                entries.addAll(RagiumContents.Drums.entries)
                entries.add(RagiumBlocks.CREATIVE_DRUM)
                entries.addAll(RagiumContents.Exporters.entries)
                entries.add(RagiumBlocks.CREATIVE_EXPORTER)
                entries.addAll(RagiumContents.Pipes.entries)
                entries.addAll(RagiumContents.CrossPipes.entries)
                entries.addAll(RagiumContents.PipeStations.entries)
                entries.addAll(RagiumContents.FilteringPipe.entries)

                entries.addAll(RagiumContents.CircuitBoards.entries)
                entries.addAll(RagiumContents.Circuits.entries)

                entries.addAll(RagiumContents.PressMolds.entries)

                entries.addAll(RagiumBlocks.MECHANICS)
                entries.add(RagiumBlocks.CREATIVE_SOURCE)
                HTMachineTier.entries.forEach { tier: HTMachineTier ->
                    RagiumAPI
                        .getInstance()
                        .machineRegistry
                        .keys
                        .map { key: HTMachineKey -> key.createItemStack(tier) }
                        .forEach(entries::add)
                }
            }
        }
    }
}
