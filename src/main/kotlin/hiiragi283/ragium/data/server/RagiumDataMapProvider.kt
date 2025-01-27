package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.util.HTTemperatureInfo
import hiiragi283.ragium.common.init.*
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class RagiumDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    @Suppress("DEPRECATION")
    private fun <T : Any> Builder<T, Block>.addBlock(block: Block, value: T): Builder<T, Block> =
        add(block.builtInRegistryHolder(), value, false)

    private fun <T : Any> Builder<T, Item>.addItem(item: ItemLike, value: T): Builder<T, Item> = add(item.asHolder(), value, false)

    private fun <T : Any> Builder<T, Item>.addContent(content: HTContent<out ItemLike>, value: T): Builder<T, Item> =
        add(content.id, value, false)

    private fun Builder<Map<HTMachineKey, Int>, Fluid>.addFuel(
        fluid: RagiumFluids,
        machine: HTMachineKey,
        value: Int,
    ): Builder<Map<HTMachineKey, Int>, Fluid> = add(fluid.commonTag, mapOf(machine to value), false)

    private fun Builder<FurnaceFuel, Item>.addFuel(item: ItemLike, second: Int): Builder<FurnaceFuel, Item> =
        addItem(item, FurnaceFuel(second * 200))

    override fun gather() {
        furnaceFuel(builder(NeoForgeDataMaps.FURNACE_FUELS))

        machineKey(builder(RagiumAPI.DataMapTypes.MACHINE_KEY))
        machineTier(builder(RagiumAPI.DataMapTypes.MACHINE_TIER))
        machineFuel(builder(RagiumAPI.DataMapTypes.MACHINE_FUEL))

        temperature(builder(RagiumAPI.DataMapTypes.TEMP_TIER))
        material(builder(RagiumAPI.DataMapTypes.MATERIAL))
    }

    private fun furnaceFuel(builder: Builder<FurnaceFuel, Item>) {
        builder.addFuel(RagiumItems.COAL_CHIP, 1)
        builder.addFuel(RagiumItems.RESIDUAL_COKE, 8)
        builder.addFuel(RagiumItems.COKE, 16)
    }

    private fun machineKey(builder: Builder<HTMachineKey, Item>) {
        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: HTBlockContent) ->
            builder.addContent(content, key)
        }
    }

    private fun machineTier(builder: Builder<HTMachineTier, Item>) {
        fun registerTier(contents: Iterable<HTBlockContent.Tier>) {
            for (content: HTBlockContent.Tier in contents) {
                builder.addContent(content, content.machineTier)
            }
        }

        registerTier(RagiumBlocks.Grates.entries)
        registerTier(RagiumBlocks.Casings.entries)
        registerTier(RagiumBlocks.Hulls.entries)
        registerTier(RagiumBlocks.Coils.entries)
        registerTier(RagiumBlocks.Burners.entries)

        registerTier(RagiumBlocks.Drums.entries)

        builder.addContent(RagiumMachineKeys.ASSEMBLER.getBlock(), HTMachineTier.ADVANCED)
        builder.addContent(RagiumMachineKeys.CHEMICAL_REACTOR.getBlock(), HTMachineTier.ADVANCED)
        builder.addContent(RagiumMachineKeys.CUTTING_MACHINE.getBlock(), HTMachineTier.ELITE)
        builder.addContent(RagiumMachineKeys.EXTRACTOR.getBlock(), HTMachineTier.ADVANCED)
        builder.addContent(RagiumMachineKeys.GRINDER.getBlock(), HTMachineTier.ADVANCED)
        builder.addContent(RagiumMachineKeys.GROWTH_CHAMBER.getBlock(), HTMachineTier.ADVANCED)

        builder.addContent(RagiumMachineKeys.LASER_TRANSFORMER.getBlock(), HTMachineTier.ELITE)
        builder.addContent(RagiumMachineKeys.MULTI_SMELTER.getBlock(), HTMachineTier.ELITE)
    }

    private fun machineFuel(builder: Builder<Map<HTMachineKey, Int>, Fluid>) {
        builder.add(
            RagiumFluidTags.NON_NITRO_FUEL,
            mapOf(RagiumMachineKeys.COMBUSTION_GENERATOR to FluidType.BUCKET_VOLUME / 10),
            false,
        )
        builder.add(
            RagiumFluidTags.NITRO_FUEL,
            mapOf(RagiumMachineKeys.COMBUSTION_GENERATOR to FluidType.BUCKET_VOLUME / 100),
            false,
        )

        builder.addFuel(RagiumFluids.METHANE, RagiumMachineKeys.COMBUSTION_GENERATOR, FluidType.BUCKET_VOLUME / 100)
        builder.addFuel(RagiumFluids.ETHENE, RagiumMachineKeys.COMBUSTION_GENERATOR, FluidType.BUCKET_VOLUME / 20)
        builder.addFuel(RagiumFluids.ACETYLENE, RagiumMachineKeys.COMBUSTION_GENERATOR, FluidType.BUCKET_VOLUME / 50)

        builder.add(
            RagiumFluidTags.THERMAL_FUEL,
            mapOf(RagiumMachineKeys.THERMAL_GENERATOR to FluidType.BUCKET_VOLUME / 10),
            false,
        )
    }

    private fun temperature(builder: Builder<HTTemperatureInfo, Block>) {
        builder.addBlock(Blocks.MAGMA_BLOCK, HTTemperatureInfo.heating(HTMachineTier.BASIC))
        builder.add(BlockTags.CAMPFIRES, HTTemperatureInfo.heating(HTMachineTier.BASIC), false)
        builder.add(BlockTags.FIRE, HTTemperatureInfo.heating(HTMachineTier.ADVANCED), false)
        builder.addBlock(Blocks.LAVA, HTTemperatureInfo.heating(HTMachineTier.ADVANCED))
        builder.addBlock(RagiumBlocks.SOUL_MAGMA_BLOCK.get(), HTTemperatureInfo.heating(HTMachineTier.ADVANCED))

        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            builder.addBlock(burner.get(), HTTemperatureInfo.heating(burner.machineTier))
        }

        builder.addBlock(Blocks.WATER, HTTemperatureInfo.cooling(HTMachineTier.BASIC))
        builder.add(BlockTags.SNOW, HTTemperatureInfo.cooling(HTMachineTier.BASIC), false)
        builder.addBlock(Blocks.ICE, HTTemperatureInfo.cooling(HTMachineTier.BASIC))
        builder.addBlock(Blocks.PACKED_ICE, HTTemperatureInfo.cooling(HTMachineTier.ADVANCED))
        builder.addBlock(Blocks.BLUE_ICE, HTTemperatureInfo.cooling(HTMachineTier.ELITE))
    }

    private fun material(builder: Builder<HTMaterialDefinition, Item>) {
        HTTagPrefix.entries.forEach { prefix: HTTagPrefix ->
            RagiumAPI.materialRegistry.keys.forEach { key: HTMaterialKey ->
                builder.add(prefix.createTag(key), HTMaterialDefinition(prefix, key), false)
            }
        }

        builder.addItem(
            Items.NETHERITE_SCRAP,
            HTMaterialDefinition(HTTagPrefix.GEM, RagiumMaterialKeys.NETHERITE_SCRAP),
        )
    }
}
