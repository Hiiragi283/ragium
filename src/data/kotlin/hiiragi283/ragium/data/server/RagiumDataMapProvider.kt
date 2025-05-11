package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBlockInteraction
import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.data.HTBreakBlockAction
import hiiragi283.ragium.common.data.HTDropItemBlockAction
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.chemical.Chemical
import mekanism.api.datamaps.chemical.ChemicalSolidTag
import mekanism.common.registries.MekanismDataMapTypes
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
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
        mekanism()
    }

    private fun fuels() {
        val builder: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        builder.add(RagiumItems.RAGI_COKE, FurnaceFuel(200 * 16), false)
    }

    private fun blockInteractions() {
        val builder: Builder<HTBlockInteraction, Block> = builder(RagiumDataMaps.BLOCK_INTERACTION)

        fun register(
            block: Block,
            tagKey: TagKey<Item>,
            predicate: StatePropertiesPredicate?,
            vararg actions: HTBlockAction,
        ) {
            builder.add(
                block.builtInRegistryHolder(),
                HTBlockInteraction(
                    Ingredient.of(tagKey),
                    Optional.ofNullable(predicate),
                    listOf(*actions),
                ),
                false,
            )
        }

        val amethysts: Map<Block, Int> = mapOf(
            Blocks.AMETHYST_CLUSTER to 4,
            Blocks.LARGE_AMETHYST_BUD to 3,
            Blocks.MEDIUM_AMETHYST_BUD to 2,
            Blocks.SMALL_AMETHYST_BUD to 1,
        )
        for ((block: Block, count: Int) in amethysts) {
            register(
                block,
                Tags.Items.GEMS_LAPIS,
                null,
                HTBreakBlockAction(false),
                HTDropItemBlockAction(RagiumItems.AZURE_SHARD, count),
            )
        }
    }

    private fun mekanism() {
        val builder: Builder<ChemicalSolidTag, Chemical> = builder(MekanismDataMapTypes.INSTANCE.chemicalSolidTag())

        builder.add(
            RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry,
            ChemicalSolidTag(RagiumItemTags.ORES_RAGINITE),
            false,
        )
    }
}
