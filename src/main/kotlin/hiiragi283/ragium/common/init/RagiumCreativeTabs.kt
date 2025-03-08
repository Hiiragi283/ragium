package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@Suppress("unused")
object RagiumCreativeTabs {
    @JvmField
    val REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(
        name: String,
        title: String,
        icon: ItemLike,
        action: MutableList<ItemLike>.(HolderLookup.Provider) -> Unit,
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> = REGISTER.register(name) { _: ResourceLocation ->
        CreativeModeTab
            .builder()
            .title(Component.literal(title))
            .icon(icon::toStack)
            .displayItems { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                buildList {
                    this.action(parameters.holders)
                }.forEach(output::accept)
            }.build()
    }

    @JvmField
    val COMMON: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register(
            "common",
            "Ragium",
            RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM),
        ) { provider: HolderLookup.Provider ->
            add(RagiumItems.CRUDE_OIL_BUCKET)
            add(RagiumItems.HONEY_BUCKET)
            // Material Blocks
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)
            addAll(RagiumBlocks.RAGINITE_ORES.oreItems)
            addAll(RagiumBlocks.RAGI_CRYSTAL_ORES.oreItems)
            addAll(RagiumBlocks.STORAGE_BLOCKS.values)
            add(RagiumBlocks.SLAG_BLOCK)
            // Decorations
            addAll(RagiumBlocks.RAGI_BRICK_SETS.blocks)
            addAll(RagiumBlocks.PLASTIC_SETS.blocks)
            addAll(RagiumBlocks.BLUE_NETHER_BRICK_SETS.blocks)

            addAll(RagiumBlocks.GLASSES)
            addAll(RagiumBlocks.LED_BLOCKS.values)

            // Material Items
            fun registerPrefix(prefix: HTTagPrefix) {
                addAll(RagiumItems.getMaterialItems(prefix))
            }

            registerPrefix(HTTagPrefix.DUST)
            add(RagiumItems.BEE_WAX)

            registerPrefix(HTTagPrefix.RAW_MATERIAL)
            add(RagiumItems.SLAG)

            registerPrefix(HTTagPrefix.GEM)

            add(RagiumItems.RAGI_ALLOY_COMPOUND)
            add(RagiumItems.STEEL_COMPOUND)
            registerPrefix(HTTagPrefix.INGOT)
            add(RagiumItems.INERT_RAGIUM_INGOT)

            registerPrefix(HTTagPrefix.GEAR)
            registerPrefix(HTTagPrefix.ROD)

            registerPrefix(HTTagPrefix.COIL)

            registerPrefix(HTTagPrefix.DIRTY_DUST)
            registerPrefix(HTTagPrefix.CLUMP)
            registerPrefix(HTTagPrefix.SHARD)
            registerPrefix(HTTagPrefix.CRYSTAL)
            // Molds
            add(RagiumItems.BLANK_PRESS_MOLD)
            add(RagiumItems.BALL_PRESS_MOLD)

            add(RagiumItems.BLOCK_PRESS_MOLD)
            add(RagiumItems.GEAR_PRESS_MOLD)
            add(RagiumItems.INGOT_PRESS_MOLD)
            add(RagiumItems.PLATE_PRESS_MOLD)
            add(RagiumItems.ROD_PRESS_MOLD)
            add(RagiumItems.WIRE_PRESS_MOLD)
            // Circuits
            add(RagiumItems.POLYMER_RESIN)
            add(RagiumItems.PLASTIC_PLATE)
            add(RagiumItems.CIRCUIT_BOARD)

            add(RagiumItems.BASIC_CIRCUIT)
            add(RagiumItems.ADVANCED_CIRCUIT)
            add(RagiumItems.ELITE_CIRCUIT)

            add(RagiumItems.STONE_BOARD)
            // Lens
            add(RagiumItems.REDSTONE_LENS)
            add(RagiumItems.GLOWSTONE_LENS)
            add(RagiumItems.DIAMOND_LENS)
            add(RagiumItems.EMERALD_LENS)
            add(RagiumItems.AMETHYST_LENS)
            // Ingredients
            add(RagiumBlocks.WOODEN_CASING)
            add(RagiumBlocks.COBBLESTONE_CASING)
            add(RagiumBlocks.MACHINE_FRAME)
            add(RagiumBlocks.CHEMICAL_MACHINE_FRAME)
            add(RagiumBlocks.PRECISION_MACHINE_FRAME)

            add(RagiumBlocks.SHAFT)
            add(RagiumItems.ENGINE)
            add(RagiumItems.LED)
            add(RagiumItems.SOLAR_PANEL)

            add(RagiumItems.SOAP)
            add(RagiumItems.TAR)
            add(RagiumItems.YELLOW_CAKE)
            add(RagiumItems.YELLOW_CAKE_PIECE)

            addAll(RagiumItems.TICKETS)
        }

    @JvmField
    val UTILITIES: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register("utilities", "Ragium - Utilities", RagiumItems.DYNAMITE) { provider: HolderLookup.Provider ->
            // Foods
            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)
            addAll(RagiumItems.FOODS)
            // Armors
            addAll(RagiumItems.EMBER_ALLOY_ARMORS.armors)
            addAll(RagiumItems.STEEL_ARMORS.armors)
            add(RagiumItems.DIVING_GOGGLE)
            add(RagiumItems.JETPACK)
            // Tools
            add(RagiumItems.FORGE_HAMMER)
            add(RagiumItems.RAGI_LANTERN)
            add(RagiumItems.RAGI_SHEARS)

            addAll(RagiumItems.EMBER_ALLOY_TOOLS.tools)
            addAll(RagiumItems.STEEL_TOOLS.tools)
            add(RagiumItems.FEVER_PICKAXE)
            add(RagiumItems.SILKY_PICKAXE)

            add(RagiumItems.POTION_BUNDLE)
            add(RagiumItems.DEFOLIANT)
            add(RagiumItems.DURALUMIN_CASE)
            // Magnets
            addAll(RagiumItems.MAGNETS)
            // Dynamites
            addAll(RagiumItems.DYNAMITES)
        }

    @JvmField
    val MACHINE: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        register("machine", "Ragium - Machines", RagiumBlocks.MANUAL_GRINDER) { provider: HolderLookup.Provider ->
            // Storage
            addAll(RagiumBlocks.CRATES.values)
            addAll(RagiumBlocks.DRUMS.values)
            // Manual Machines
            add(RagiumBlocks.MANUAL_GRINDER)

            add(RagiumBlocks.DISENCHANTING_TABLE)
            // Utilities
            addAll(RagiumBlocks.ADDONS)
            addAll(RagiumBlocks.BURNERS)
            // Machines
            addAll(HTMachineType.entries)
        }
}
