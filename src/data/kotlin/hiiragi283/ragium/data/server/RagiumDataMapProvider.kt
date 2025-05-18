package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBlockInteraction
import hiiragi283.ragium.api.data.interaction.HTBreakBlockAction
import hiiragi283.ragium.api.data.interaction.HTDropItemBlockAction
import hiiragi283.ragium.api.data.interaction.HTReplaceBlockAction
import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.*
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
class RagiumDataMapProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(output, provider) {
    private lateinit var blockLookup: HolderGetter<Block>

    override fun gather(provider: HolderLookup.Provider) {
        blockLookup = provider.blockLookup()

        fuels()
        blockInteractions()
        // mekanism()
    }

    private fun fuels() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        builder.add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 8 * 4), false)
    }

    private fun blockInteractions() {
        val builder: Builder<HTBlockInteraction, Block> = builder(RagiumDataMaps.BLOCK_INTERACTION)

        fun register(
            block: DeferredBlock<*>,
            ingredient: Ingredient,
            predicate: StatePropertiesPredicate?,
            vararg actions: HTBlockAction,
        ) {
            builder.add(
                block,
                HTBlockInteraction(
                    ingredient,
                    Optional.ofNullable(predicate),
                    listOf(*actions),
                ),
                false,
            )
        }

        fun register(
            block: Block,
            ingredient: Ingredient,
            predicate: StatePropertiesPredicate?,
            vararg actions: HTBlockAction,
        ) {
            builder.add(
                block.builtInRegistryHolder(),
                HTBlockInteraction(
                    ingredient,
                    Optional.ofNullable(predicate),
                    listOf(*actions),
                ),
                false,
            )
        }

        // Raginite
        register(
            Blocks.REDSTONE_ORE,
            Ingredient.of(RagiumItems.RAGI_TICKET),
            null,
            HTReplaceBlockAction.update(RagiumBlocks.RAGINITE_ORES[HTOreVariant.OVERWORLD].get()),
        )
        register(
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Ingredient.of(RagiumItems.RAGI_TICKET),
            null,
            HTReplaceBlockAction.update(RagiumBlocks.RAGINITE_ORES[HTOreVariant.DEEPSLATE].get()),
        )
        register(
            Blocks.LANTERN,
            Ingredient.of(RagiumItems.RAGI_TICKET),
            null,
            HTBreakBlockAction(false),
            HTDropItemBlockAction(RagiumItems.RAGI_LANTERN),
        )

        // Azure
        val amethysts: Map<Block, Int> = mapOf(
            Blocks.AMETHYST_CLUSTER to 4,
            Blocks.LARGE_AMETHYST_BUD to 3,
            Blocks.MEDIUM_AMETHYST_BUD to 2,
            Blocks.SMALL_AMETHYST_BUD to 1,
        )
        for ((block: Block, count: Int) in amethysts) {
            register(
                block,
                Ingredient.of(RagiumItems.AZURE_TICKET),
                null,
                HTBreakBlockAction(false),
                HTDropItemBlockAction(RagiumItems.AZURE_SHARD, count),
            )
        }

        // Deep
        register(
            Blocks.REINFORCED_DEEPSLATE,
            Ingredient.of(RagiumItems.DEEP_TICKET),
            null,
            HTDropItemBlockAction(RagiumItems.DEEP_STEEL_INGOT, 3),
            HTReplaceBlockAction.update(Blocks.DEEPSLATE),
        )
        register(
            Blocks.SCULK_SHRIEKER,
            Ingredient.of(RagiumItems.DEEP_TICKET),
            null,
            HTReplaceBlockAction.update(
                Blocks.SCULK_SHRIEKER
                    .defaultBlockState()
                    .setValue(BlockStateProperties.CAN_SUMMON, true),
            ),
        )
    }

    /*private fun mekanism() {
        val builder: Builder<ChemicalSolidTag, Chemical> = builder(MekanismDataMapTypes.INSTANCE.chemicalSolidTag())

        builder.add(
            RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry,
            ChemicalSolidTag(RagiumItemTags.ORES_RAGINITE),
            false,
        )
    }*/
}
