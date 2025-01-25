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
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
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

    private fun Builder<FurnaceFuel, Item>.addFuel(item: ItemLike, second: Int) = addItem(item, FurnaceFuel(second * 200))

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
        // Heating
        val heatingBuilder: Builder<HTMachineTier, Block> = builder(RagiumAPI.DataMapTypes.HEATING_TIER)

        heatingBuilder
            .addBlock(Blocks.MAGMA_BLOCK, HTMachineTier.BASIC)
            .add(BlockTags.CAMPFIRES, HTMachineTier.BASIC, false)
            .add(BlockTags.FIRE, HTMachineTier.ADVANCED, false)
            .addBlock(Blocks.LAVA, HTMachineTier.ADVANCED)

        RagiumBlocks.Burners.entries.forEach { burner: RagiumBlocks.Burners ->
            heatingBuilder.addBlock(burner.get(), burner.machineTier)
        }
        // Cooling
        val coolingBuilder: Builder<HTMachineTier, Block> = builder(RagiumAPI.DataMapTypes.COOLING_TIER)

        coolingBuilder
            .addBlock(Blocks.WATER, HTMachineTier.BASIC)
            .add(BlockTags.SNOW, HTMachineTier.BASIC, false)
            .addBlock(Blocks.ICE, HTMachineTier.BASIC)
            .addBlock(Blocks.PACKED_ICE, HTMachineTier.ADVANCED)
            .addBlock(Blocks.BLUE_ICE, HTMachineTier.ELITE)

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
