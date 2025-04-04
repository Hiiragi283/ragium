package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.data.HTCatalystConversion
import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SculkShriekerBlock
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumDataMapProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(output, provider) {
    private lateinit var blockLookup: HolderGetter<Block>

    override fun gather(provider: HolderLookup.Provider) {
        blockLookup = provider.blockLookup()

        azureCatalyst()
        deepCatalyst()
        ragiumCatalyst()
    }

    private fun azureCatalyst() {
        val builder: Builder<HTCatalystConversion, Block> = builder(HTCatalystConversion.AZURE_TYPE)

        // Iron Block -> Azure Steel Ingot
        builder.add(
            Tags.Blocks.STORAGE_BLOCKS_IRON,
            HTCatalystConversion.drop(RagiumItems.Ingots.AZURE_STEEL, 3),
            false,
        )

        // Amethyst Block -> Budding Amethyst Block
        builder.add(
            Blocks.AMETHYST_BLOCK.builtInRegistryHolder(),
            HTCatalystConversion.replace(Blocks.BUDDING_AMETHYST),
            false,
        )
        // Deepslate Tiles -> Azure Tiles
        builder.add(
            Blocks.DEEPSLATE_TILES.builtInRegistryHolder(),
            HTCatalystConversion.replace(RagiumBlocks.AZURE_TILE_SETS.base),
            false,
        )
    }

    private fun deepCatalyst() {
        val builder: Builder<HTCatalystConversion, Block> = builder(HTCatalystConversion.DEEP_TYPE)

        // Azure Steel Block -> Deep Steel Ingot
        builder.add(
            HTTagPrefixes.STORAGE_BLOCK.createBlockTag(RagiumMaterials.AZURE_STEEL),
            HTCatalystConversion.drop(RagiumItems.Ingots.DEEP_STEEL, 3),
            false,
        )

        // Deepslate -> Reinforced Deepslate
        builder.add(
            Blocks.DEEPSLATE.builtInRegistryHolder(),
            HTCatalystConversion.replace(Blocks.REINFORCED_DEEPSLATE),
            false,
        )
        // Moss Block -> Sculk
        builder.add(
            Blocks.MOSS_BLOCK.builtInRegistryHolder(),
            HTCatalystConversion.replace(Blocks.SCULK),
            false,
        )
        // Vine -> Sculk Vein
        builder.add(
            Blocks.VINE.builtInRegistryHolder(),
            HTCatalystConversion.replace(Blocks.SCULK_VEIN),
            false,
        )
        // Bone Block -> Sculk Catalyst
        builder.add(
            Tags.Blocks.STORAGE_BLOCKS_BONE_MEAL,
            HTCatalystConversion.replace(Blocks.SCULK_CATALYST),
            false,
        )
        // Note Block -> Sculk Sensor
        builder.add(
            Blocks.NOTE_BLOCK.builtInRegistryHolder(),
            HTCatalystConversion.replace(Blocks.SCULK_SENSOR),
            false,
        )
        // Jukebox -> Sculk Shrieker
        builder.add(
            Blocks.JUKEBOX.builtInRegistryHolder(),
            HTCatalystConversion.replace(
                Blocks.SCULK_SHRIEKER
                    .defaultBlockState()
                    .setValue(SculkShriekerBlock.CAN_SUMMON, true),
            ),
            false,
        )
    }

    private fun ragiumCatalyst() {
        val builder: Builder<HTCatalystConversion, Block> = builder(HTCatalystConversion.RAGIUM_TYPE)

        // RS Block -> Raginite Dust
        builder.add(
            Tags.Blocks.STORAGE_BLOCKS_REDSTONE,
            HTCatalystConversion.drop(RagiumItems.Dusts.RAGINITE, 3),
            false,
        )
        // Copper Block -> Ragi-Alloy Ingot
        builder.add(
            Tags.Blocks.STORAGE_BLOCKS_COPPER,
            HTCatalystConversion.drop(RagiumItems.Ingots.RAGI_ALLOY, 3),
            false,
        )
        // Gold Block -> Advanced Ragi-Alloy Ingot
        builder.add(
            Tags.Blocks.STORAGE_BLOCKS_GOLD,
            HTCatalystConversion.drop(RagiumItems.Ingots.ADVANCED_RAGI_ALLOY, 3),
            false,
        )
        // Diamond Block -> Advanced Ragi-Alloy Ingot
        builder.add(
            Tags.Blocks.STORAGE_BLOCKS_DIAMOND,
            HTCatalystConversion.drop(RagiumItems.RawResources.RAGI_CRYSTAL, 3),
            false,
        )
        // Copy Dragon Egg
        builder.add(
            Blocks.DRAGON_EGG.builtInRegistryHolder(),
            HTCatalystConversion.drop(Blocks.DRAGON_EGG, 2),
            false,
        )

        // Bricks -> Ragi-Bricks
        builder.add(
            Blocks.BRICKS.builtInRegistryHolder(),
            HTCatalystConversion.replace(RagiumBlocks.RAGI_BRICK_SETS.base),
            false,
        )
    }
}
