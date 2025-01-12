package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.splitWith
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.item.HTBackpackItem
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.DyeColor

object RagiumItemGroup {
    @JvmField
    val BUILDING: RegistryKey<ItemGroup> = create("building")

    @JvmField
    val FLUID: RegistryKey<ItemGroup> = create("fluid")

    @JvmField
    val ITEM: RegistryKey<ItemGroup> = create("item")

    @JvmField
    val MACHINE: RegistryKey<ItemGroup> = create("machine")

    @JvmField
    val TRANSFER: RegistryKey<ItemGroup> = create("transfer")

    @JvmStatic
    private fun create(path: String): RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, RagiumAPI.id(path))

    @JvmStatic
    private inline fun register(key: RegistryKey<ItemGroup>, action: ItemGroup.Builder.() -> Unit): ItemGroup = Registry.register(
        Registries.ITEM_GROUP,
        key,
        FabricItemGroup
            .builder()
            .apply(action)
            .displayName(key.text)
            .build(),
    )

    private val RegistryKey<ItemGroup>.text: MutableText
        get() = Text.translatable("itemGroup.${value.splitWith('.')}")

    private fun ItemGroup.Entries.addAll(items: List<ItemConvertible>) {
        items.forEach(this::add)
    }

    @JvmStatic
    fun init() {
        register(BUILDING) {
            icon { ItemStack(RagiumBlocks.Grates.PRIMITIVE) }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                entries.addAll(RagiumBlocks.Stones.entries)
                entries.addAll(RagiumBlocks.Slabs.entries)
                entries.addAll(RagiumBlocks.Stairs.entries)

                entries.add(RagiumBlocks.ITEM_DISPLAY)
                entries.addAll(RagiumBlocks.Glasses.entries)
                entries.addAll(RagiumBlocks.WhiteLines.entries)

                entries.add(RagiumBlocks.PLASTIC_BLOCK)
                entries.addAll(RagiumBlocks.Decorations.entries)

                entries.addAll(RagiumBlocks.LEDBlocks.entries)
            }
        }

        register(FLUID) {
            icon { RagiumItems.EMPTY_FLUID_CUBE.get().defaultStack }
            entries { context: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                // empty cubes
                entries.add(RagiumItems.EMPTY_FLUID_CUBE)
                // filled cubes
                context.lookup
                    .getWrapperOrThrow(RegistryKeys.FLUID)
                    .streamEntries()
                    .filter { entry: RegistryEntry.Reference<Fluid> ->
                        val fluid: Fluid = entry.value()
                        fluid.isStill(fluid.defaultState)
                    }.map(RagiumAPI.getInstance()::createFilledCube)
                    .forEach(entries::add)
            }
        }

        register(ITEM) {
            icon {
                RagiumItems.Ingots.RAGIUM
                    .asItem()
                    .defaultStack
            }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                entries.addAll(RagiumBlocks.NATURAL)
                entries.addAll(RagiumBlocks.Ores.entries)
                entries.addAll(RagiumBlocks.StorageBlocks.entries)

                entries.addAll(RagiumItems.Dusts.entries)
                entries.addAll(RagiumItems.Gears.entries)
                entries.addAll(RagiumItems.Gems.entries)
                entries.addAll(RagiumItems.Ingots.entries)
                entries.addAll(RagiumItems.Plates.entries)
                entries.addAll(RagiumItems.RawMaterials.entries)

                entries.addAll(RagiumBlocks.FOODS)
                entries.addAll(RagiumItems.FOODS)

                entries.addAll(RagiumItems.SteelArmors.entries)
                entries.addAll(RagiumItems.DeepSteelArmors.entries)
                entries.addAll(RagiumItems.StellaSuits.entries)
                entries.add(RagiumItems.DRAGONIC_ELYTRA)

                entries.addAll(RagiumItems.SteelTools.entries)
                entries.addAll(RagiumItems.DeepSteelTools.entries)
                entries.add(RagiumItems.FORGE_HAMMER)
                entries.add(RagiumItems.RAGI_WRENCH)
                entries.add(RagiumItems.STELLA_SABER)
                entries.add(RagiumItems.RAGIUM_SABER)
                entries.add(RagiumItems.GIGANT_HAMMER)
                entries.add(RagiumItems.ECHO_BULLET)
                entries.addAll(RagiumItems.Dynamites.entries)

                entries.add(RagiumItems.FLUID_FILTER)
                entries.add(RagiumItems.ITEM_FILTER)
                entries.add(RagiumItems.TRADER_CATALOG)

                entries.addAll(RagiumItems.Plastics.entries)
                entries.addAll(RagiumItems.CircuitBoards.entries)
                entries.addAll(RagiumItems.Circuits.entries)
                entries.addAll(RagiumItems.Processors.entries)
                entries.addAll(RagiumItems.PressMolds.entries)
                entries.addAll(RagiumItems.Radioactives.entries)
                entries.addAll(RagiumItems.INGREDIENTS)

                entries.addAll(RagiumItems.MISC)

                DyeColor.entries.map(HTBackpackItem::createStack).forEach(entries::add)
            }
        }

        register(MACHINE) {
            icon { RagiumMachineKeys.ASSEMBLER.createItemStack(HTMachineTier.PRIMITIVE) }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                entries.addAll(RagiumBlocks.Creatives.entries)
                entries.addAll(RagiumBlocks.MECHANICS)

                entries.addAll(RagiumBlocks.Grates.entries)
                entries.addAll(RagiumBlocks.Casings.entries)
                entries.addAll(RagiumBlocks.Hulls.entries)
                entries.add(RagiumBlocks.SHAFT)
                entries.addAll(RagiumBlocks.Coils.entries)

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

        register(TRANSFER) {
            icon { ItemStack(RagiumBlocks.Crates.PRIMITIVE) }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                entries.addAll(RagiumBlocks.Crates.entries)
                entries.add(RagiumBlocks.OPEN_CRATE)
                entries.add(RagiumBlocks.VOID_CRATE)
                entries.add(RagiumBlocks.BACKPACK_CRATE)

                entries.addAll(RagiumBlocks.Drums.entries)

                entries.addAll(RagiumBlocks.Exporters.entries)
                entries.addAll(RagiumBlocks.Pipes.entries)
                entries.addAll(RagiumBlocks.CrossPipes.entries)
                entries.addAll(RagiumBlocks.PipeStations.entries)
                entries.addAll(RagiumBlocks.FilteringPipes.entries)
            }
        }
    }
}
