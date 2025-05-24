package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTTreeTap
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBlockInteraction
import hiiragi283.ragium.api.data.interaction.HTBreakBlockAction
import hiiragi283.ragium.api.data.interaction.HTDropItemBlockAction
import hiiragi283.ragium.api.data.interaction.HTPlaySoundBlockAction
import hiiragi283.ragium.api.data.interaction.HTReplaceBlockAction
import hiiragi283.ragium.api.extension.blockLookup
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
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
        treeTaps()
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

        fun register(
            tagKey: TagKey<Block>,
            ingredient: Ingredient,
            predicate: StatePropertiesPredicate?,
            vararg actions: HTBlockAction,
        ) {
            builder.add(
                tagKey,
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
            Tags.Blocks.ORES_REDSTONE,
            Ingredient.of(RagiumItems.RAGI_TICKET),
            null,
            HTReplaceBlockAction.update(RagiumBlocks.RAGINITE_ORES.stoneOre.get()),
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
        // Bloody
        register(
            Blocks.SOUL_SOIL,
            Ingredient.of(RagiumItems.BLOODY_TICKET),
            null,
            HTReplaceBlockAction.update(RagiumBlocks.CRIMSON_SOIL.get()),
        )
        // Eldritch
        register(
            Blocks.MOSS_BLOCK,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTReplaceBlockAction.update(Blocks.SCULK),
        )
        register(
            Blocks.VINE,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTBreakBlockAction(false),
            HTDropItemBlockAction(Blocks.SCULK_VEIN),
        )
        register(
            Tags.Blocks.STORAGE_BLOCKS_BONE_MEAL,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTReplaceBlockAction.update(Blocks.SCULK_CATALYST),
        )
        register(
            Blocks.SCULK_SHRIEKER,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTPlaySoundBlockAction(SoundEvents.SCULK_SHRIEKER_SHRIEK),
            HTReplaceBlockAction.update(
                Blocks.SCULK_SHRIEKER
                    .defaultBlockState()
                    .setValue(BlockStateProperties.CAN_SUMMON, true),
            ),
        )

        register(
            Tags.Blocks.COBBLESTONES,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTReplaceBlockAction.update(Blocks.END_STONE),
        )
        register(
            Blocks.LIGHTNING_ROD,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTReplaceBlockAction.update(Blocks.END_ROD),
        )
        register(
            Blocks.MELON,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTBreakBlockAction(false),
            HTDropItemBlockAction(Items.CHORUS_FRUIT, 3),
        )

        register(
            Tags.Blocks.OBSIDIANS_CRYING,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            null,
            HTReplaceBlockAction.update(RagiumBlocks.MYSTERIOUS_OBSIDIAN.get()),
        )
    }

    private fun treeTaps() {
        val builder: Builder<HTTreeTap, Fluid> = builder(RagiumDataMaps.TREE_TAP)

        builder.add(
            RagiumFluidContents.CRIMSON_SAP.commonTag,
            HTTreeTap(blockLookup.getOrThrow(BlockTags.CRIMSON_STEMS)),
            false,
        )
        builder.add(
            RagiumFluidContents.WARPED_SAP.commonTag,
            HTTreeTap(blockLookup.getOrThrow(BlockTags.WARPED_STEMS)),
            false,
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
