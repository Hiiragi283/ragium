package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBreakBlockAction
import hiiragi283.ragium.api.data.interaction.HTDropItemBlockAction
import hiiragi283.ragium.api.data.interaction.HTPlaySoundBlockAction
import hiiragi283.ragium.api.data.interaction.HTReplaceBlockAction
import hiiragi283.ragium.api.extension.blockHolderSet
import hiiragi283.ragium.common.recipe.HTBlockInteractingRecipeImpl
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.common.Tags

object RagiumBlockInteractingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        blockGetter = provider.lookupOrThrow(Registries.BLOCK)

        raginite()
        azure()
        bloody()
        eldritchSculk()
        eldritchEnder()
    }

    private fun raginite() {
        register(
            RagiumAPI.id("raginite_ore"),
            Tags.Blocks.ORES_REDSTONE,
            Ingredient.of(RagiumItems.RAGI_TICKET_FAKE),
            HTReplaceBlockAction.update(RagiumBlocks.RAGINITE_ORES.stoneOre.get()),
        )
    }

    private fun azure() {
        fun shard(suffix: String, block: Block, count: Int) {
            register(
                RagiumAPI.id("azure_shard_from_").withSuffix(suffix),
                block,
                Ingredient.of(RagiumItems.AZURE_TICKET),
                HTBreakBlockAction(false),
                HTDropItemBlockAction(RagiumItems.AZURE_SHARD, count),
            )
        }
        shard("cluster", Blocks.AMETHYST_CLUSTER, 4)
        shard("large_bud", Blocks.LARGE_AMETHYST_BUD, 3)
        shard("medium_bud", Blocks.MEDIUM_AMETHYST_BUD, 2)
        shard("small_bud", Blocks.SMALL_AMETHYST_BUD, 1)
    }

    private fun bloody() {
        register(
            RagiumBlocks.CRIMSON_SOIL.id,
            Blocks.SOUL_SOIL,
            Ingredient.of(RagiumItems.BLOODY_TICKET),
            HTReplaceBlockAction.update(RagiumBlocks.CRIMSON_SOIL.get()),
        )
    }

    private fun eldritchSculk() {
        register(
            RagiumAPI.id("sculk_from_moss"),
            Blocks.MOSS_BLOCK,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTReplaceBlockAction.update(Blocks.SCULK),
        )
        register(
            RagiumAPI.id("sculk_vein_from_vine"),
            Blocks.VINE,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTBreakBlockAction(false),
            HTDropItemBlockAction(Blocks.SCULK_VEIN),
        )
        register(
            RagiumAPI.id("echo_shard_from_amethyst"),
            Blocks.AMETHYST_CLUSTER,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTBreakBlockAction(false),
            HTDropItemBlockAction(Items.ECHO_SHARD),
        )
        register(
            RagiumAPI.id("sculk_catalyst_from_bone_blocks"),
            Tags.Blocks.STORAGE_BLOCKS_BONE_MEAL,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTReplaceBlockAction.update(Blocks.SCULK_CATALYST),
        )
        register(
            RagiumAPI.id("activate_sculk_shrieker"),
            Blocks.SCULK_SHRIEKER,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTPlaySoundBlockAction(SoundEvents.SCULK_SHRIEKER_SHRIEK),
            HTReplaceBlockAction.update(
                Blocks.SCULK_SHRIEKER
                    .defaultBlockState()
                    .setValue(BlockStateProperties.CAN_SUMMON, true),
            ),
        )
    }

    private fun eldritchEnder() {
        register(
            RagiumAPI.id("end_stone_from_cobblestones"),
            Tags.Blocks.COBBLESTONES,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTReplaceBlockAction.update(Blocks.END_STONE),
        )
        register(
            RagiumAPI.id("end_rod_from_lightning_rod"),
            Blocks.LIGHTNING_ROD,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTReplaceBlockAction.update(Blocks.END_ROD),
        )
        register(
            RagiumAPI.id("chorus_fruit_from_melon"),
            Blocks.MELON,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTBreakBlockAction(false),
            HTDropItemBlockAction(Items.CHORUS_FRUIT, 3),
        )

        register(
            RagiumAPI.id("crying_to_mysterious"),
            Tags.Blocks.OBSIDIANS_CRYING,
            Ingredient.of(RagiumItems.ELDRITCH_TICKET),
            HTReplaceBlockAction.update(RagiumBlocks.MYSTERIOUS_OBSIDIAN.get()),
        )
    }

    //    Extensions    //

    @JvmStatic
    private lateinit var blockGetter: HolderGetter<Block>

    @JvmStatic
    private fun register(
        id: ResourceLocation,
        block: Block,
        ingredient: Ingredient,
        vararg actions: HTBlockAction,
    ) {
        save(
            id.withPrefix("interacting/"),
            HTBlockInteractingRecipeImpl(
                ingredient,
                blockHolderSet(block),
                listOf(*actions),
            ),
        )
    }

    @JvmStatic
    private fun register(
        id: ResourceLocation,
        tagKey: TagKey<Block>,
        ingredient: Ingredient,
        vararg actions: HTBlockAction,
    ) {
        save(
            id.withPrefix("interacting/"),
            HTBlockInteractingRecipeImpl(
                ingredient,
                blockGetter.getOrThrow(tagKey),
                listOf(*actions),
            ),
        )
    }
}
