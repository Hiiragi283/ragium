package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
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
                RagiumItems.Gems.RAGIUM
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                entries.addAll(RagiumBlocks.NATURAL)
                entries.addAll(RagiumBlocks.Stones.entries)
                entries.addAll(RagiumBlocks.Slabs.entries)
                entries.addAll(RagiumBlocks.Stairs.entries)
                entries.add(RagiumBlocks.ITEM_DISPLAY)
                entries.addAll(RagiumBlocks.Glasses.entries)
                entries.addAll(RagiumBlocks.WhiteLines.entries)

                entries.addAll(RagiumBlocks.Ores.entries)
                entries.addAll(RagiumBlocks.StorageBlocks.entries)
                entries.addAll(RagiumBlocks.Grates.entries)
                entries.addAll(RagiumBlocks.Casings.entries)
                entries.addAll(RagiumBlocks.Hulls.entries)
                entries.add(RagiumBlocks.SHAFT)
                entries.addAll(RagiumBlocks.Coils.entries)

                entries.addAll(RagiumItems.Dusts.entries)
                entries.addAll(RagiumItems.Gears.entries)
                entries.addAll(RagiumItems.Gems.entries)
                entries.addAll(RagiumItems.Ingots.entries)
                if (RagiumAPI.getInstance().config.isHardMode) {
                    entries.addAll(RagiumItems.Plates.entries)
                }
                entries.addAll(RagiumItems.RawMaterials.entries)

                entries.addAll(RagiumBlocks.FOODS)
                entries.addAll(RagiumItems.FOODS)

                entries.addAll(RagiumItems.SteelArmors.entries)
                entries.addAll(RagiumItems.DeepSteelArmors.entries)
                entries.addAll(RagiumItems.StellaSuits.entries)

                entries.addAll(RagiumItems.SteelTools.entries)
                entries.addAll(RagiumItems.DeepSteelTools.entries)
                entries.add(RagiumItems.FORGE_HAMMER)
                entries.add(RagiumItems.RAGI_WRENCH)
                entries.add(RagiumItems.STELLA_SABER)
                entries.add(RagiumItems.RAGIUM_SABER)
                entries.add(RagiumItems.GIGANT_HAMMER)
                entries.addAll(RagiumItems.Dynamites.entries)
                entries.add(RagiumItems.EMPTY_FLUID_CUBE)
                entries.add(RagiumItems.FLUID_FILTER)
                entries.add(RagiumItems.ITEM_FILTER)
                entries.add(RagiumItems.GUIDE_BOOK)
                entries.add(RagiumItems.TRADER_CATALOG)

                entries.addAll(RagiumItems.CircuitBoards.entries)
                entries.addAll(RagiumItems.Circuits.entries)
                entries.addAll(RagiumItems.PressMolds.entries)
                entries.addAll(RagiumItems.Ingredients.entries)
                entries.addAll(RagiumItems.Radioactives.entries)

                entries.addAll(RagiumItems.MISC)

                DyeColor.entries.map(HTBackpackItem::createStack).forEach(entries::add)
            }
        }

        register(FLUID_KEY) {
            displayName(Text.translatable("itemGroup.ragium.fluid"))
            icon { RagiumItems.EMPTY_FLUID_CUBE.get().defaultStack }
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
                entries.addAll(RagiumBlocks.Creatives.entries)

                entries.addAll(RagiumBlocks.Crates.entries)
                entries.addAll(RagiumBlocks.Drums.entries)

                entries.addAll(RagiumBlocks.Exporters.entries)
                entries.addAll(RagiumBlocks.Pipes.entries)
                entries.addAll(RagiumBlocks.CrossPipes.entries)
                entries.addAll(RagiumBlocks.PipeStations.entries)
                entries.addAll(RagiumBlocks.FilteringPipes.entries)

                entries.addAll(RagiumBlocks.MECHANICS)
                entries.add(RagiumBlocks.BACKPACK_INTERFACE)

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
