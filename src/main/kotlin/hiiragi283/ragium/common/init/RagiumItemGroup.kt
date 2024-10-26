package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.item.HTBackpackItem
import hiiragi283.ragium.common.item.HTCrafterHammerItem
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
                RagiumContents.Misc.RAGIUM
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                buildList {
                    addAll(RagiumContents.Ores.entries)
                    add(RagiumBlocks.POROUS_NETHERRACK)
                    add(RagiumBlocks.SNOW_SPONGE)
                    addAll(RagiumContents.StorageBlocks.entries)

                    addAll(RagiumContents.Dusts.entries)
                    addAll(RagiumContents.Gems.entries)
                    addAll(RagiumContents.Ingots.entries)
                    addAll(RagiumContents.Plates.entries)
                    addAll(RagiumContents.RawMaterials.entries)

                    addAll(RagiumContents.Armors.entries)
                    addAll(RagiumContents.Tools.entries)
                    addAll(HTCrafterHammerItem.Behavior.entries)

                    addAll(RagiumContents.Hulls.entries)
                    add(RagiumBlocks.BASIC_CASING)
                    add(RagiumBlocks.ADVANCED_CASING)
                    addAll(RagiumContents.Coils.entries)
                    addAll(RagiumContents.CircuitBoards.entries)
                    addAll(RagiumContents.Circuits.entries)

                    addAll(RagiumContents.Foods.entries)
                    add(RagiumBlocks.SPONGE_CAKE)
                    add(RagiumBlocks.SWEET_BERRIES_CAKE)
                    addAll(RagiumContents.Misc.entries)
                    remove(RagiumContents.Misc.BACKPACK)
                    remove(RagiumContents.Misc.FILLED_FLUID_CUBE)
                }.forEach(entries::add)

                DyeColor.entries.map(HTBackpackItem::createStack).forEach(entries::add)
            }
        }

        register(FLUID_KEY) {
            displayName(Text.translatable("itemGroup.ragium.fluid"))
            icon {
                RagiumContents.Misc.EMPTY_FLUID_CUBE
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                Registries.FLUID
                    .filter { it.isStill(it.defaultState) }
                    .map(RagiumAPI.getInstance()::createFilledCube)
                    .forEach(entries::add)
            }
        }

        register(MACHINE_KEY) {
            displayName(Text.translatable("itemGroup.ragium.machine"))
            icon {
                RagiumBlocks.META_MACHINE
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                buildList {
                    add(RagiumBlocks.CREATIVE_SOURCE)
                    add(RagiumBlocks.MANUAL_FORGE)
                    add(RagiumBlocks.MANUAL_GRINDER)
                    add(RagiumBlocks.MANUAL_MIXER)
                    add(RagiumBlocks.SHAFT)
                    add(RagiumBlocks.ITEM_DISPLAY)
                    add(RagiumBlocks.NETWORK_INTERFACE)
                    add(RagiumBlocks.BACKPACK_INTERFACE)

                    addAll(RagiumContents.Exporters.entries)
                    addAll(RagiumContents.Pipes.entries)
                }.forEach(entries::add)
                // machines
                HTMachineTier.entries.forEach { tier: HTMachineTier ->
                    RagiumAPI
                        .getInstance()
                        .machineTypeRegistry
                        .types
                        .filterNot { it == HTMachineType.DEFAULT }
                        .map { type: HTMachineType -> type.createItemStack(tier) }
                        .forEach(entries::add)
                }
            }
        }
    }
}
