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
import hiiragi283.ragium.api.util.HTTemperatureInfo
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.DataMapProvider
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

    private fun Builder<FurnaceFuel, Item>.addFuel(item: ItemLike, second: Int): Builder<FurnaceFuel, Item> =
        addItem(item, FurnaceFuel(second * 200))

    override fun gather() {
        // Fuel
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .addFuel(RagiumItems.COAL_CHIP, 1)
            .addFuel(RagiumItems.RESIDUAL_COKE, 8)

        // Machine
        val machineBuilder: Builder<HTMachineKey, Item> = builder(RagiumAPI.DataMapTypes.MACHINE_KEY)

        RagiumAPI.machineRegistry.blockMap.forEach { (key: HTMachineKey, content: HTBlockContent) ->
            machineBuilder.addContent(content, key)
        }
        // Machine Tier
        val tierBuilder: Builder<HTMachineTier, Item> = builder(RagiumAPI.DataMapTypes.MACHINE_TIER)

        fun registerTier(contents: Iterable<HTBlockContent.Tier>) {
            for (content: HTBlockContent.Tier in contents) {
                tierBuilder.addContent(content, content.machineTier)
            }
        }

        registerTier(RagiumBlocks.Grates.entries)
        registerTier(RagiumBlocks.Casings.entries)
        registerTier(RagiumBlocks.Hulls.entries)
        registerTier(RagiumBlocks.Coils.entries)
        registerTier(RagiumBlocks.Burners.entries)

        registerTier(RagiumBlocks.Drums.entries)

        tierBuilder.addContent(RagiumMachineKeys.ASSEMBLER.getBlock(), HTMachineTier.ADVANCED)
        tierBuilder.addContent(RagiumMachineKeys.CHEMICAL_REACTOR.getBlock(), HTMachineTier.ADVANCED)
        tierBuilder.addContent(RagiumMachineKeys.CUTTING_MACHINE.getBlock(), HTMachineTier.ELITE)
        tierBuilder.addContent(RagiumMachineKeys.EXTRACTOR.getBlock(), HTMachineTier.ADVANCED)
        tierBuilder.addContent(RagiumMachineKeys.GRINDER.getBlock(), HTMachineTier.ADVANCED)
        tierBuilder.addContent(RagiumMachineKeys.GROWTH_CHAMBER.getBlock(), HTMachineTier.ADVANCED)
        
        tierBuilder.addContent(RagiumMachineKeys.LASER_TRANSFORMER.getBlock(), HTMachineTier.ELITE)
        tierBuilder.addContent(RagiumMachineKeys.MULTI_SMELTER.getBlock(), HTMachineTier.ELITE)
        // Temperature
        val tempBuilder: Builder<HTTemperatureInfo, Block> = builder(RagiumAPI.DataMapTypes.TEMP_TIER)

        tempBuilder
            .addBlock(Blocks.MAGMA_BLOCK, HTTemperatureInfo.heating(HTMachineTier.BASIC))
            .add(BlockTags.CAMPFIRES, HTTemperatureInfo.heating(HTMachineTier.BASIC), false)
            .add(BlockTags.FIRE, HTTemperatureInfo.heating(HTMachineTier.ADVANCED), false)
            .addBlock(Blocks.LAVA, HTTemperatureInfo.heating(HTMachineTier.ADVANCED))
            .addBlock(RagiumBlocks.SOUL_MAGMA_BLOCK.get(), HTTemperatureInfo.heating(HTMachineTier.ADVANCED))

        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            tempBuilder.addBlock(burner.get(), HTTemperatureInfo.heating(burner.machineTier))
        }

        tempBuilder
            .addBlock(Blocks.WATER, HTTemperatureInfo.cooling(HTMachineTier.BASIC))
            .add(BlockTags.SNOW, HTTemperatureInfo.cooling(HTMachineTier.BASIC), false)
            .addBlock(Blocks.ICE, HTTemperatureInfo.cooling(HTMachineTier.BASIC))
            .addBlock(Blocks.PACKED_ICE, HTTemperatureInfo.cooling(HTMachineTier.ADVANCED))
            .addBlock(Blocks.BLUE_ICE, HTTemperatureInfo.cooling(HTMachineTier.ELITE))

        // Material
        val materialBuilder: Builder<HTMaterialDefinition, Item> = builder(RagiumAPI.DataMapTypes.MATERIAL)

        HTTagPrefix.entries.forEach { prefix: HTTagPrefix ->
            RagiumAPI.materialRegistry.keys.forEach { key: HTMaterialKey ->
                materialBuilder.add(prefix.createTag(key), HTMaterialDefinition(prefix, key), false)
            }
        }

        materialBuilder
            .addItem(Items.NETHERITE_SCRAP, HTMaterialDefinition(HTTagPrefix.GEM, RagiumMaterialKeys.NETHERITE_SCRAP))
    }
}
