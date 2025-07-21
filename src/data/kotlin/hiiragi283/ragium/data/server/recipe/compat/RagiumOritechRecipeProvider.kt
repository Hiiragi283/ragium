package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        foundry()
        atomicForge()
        particle()
    }

    private fun foundry() {
        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(Tags.Items.INGOTS_COPPER)
            .result(RagiumItems.RAGI_ALLOY_INGOT.get())
            .time(160)
            .export(output, RagiumConstantValues.RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(Tags.Items.INGOTS_GOLD)
            .result(RagiumItems.ADVANCED_RAGI_ALLOY_INGOT.get())
            .time(160)
            .export(output, RagiumConstantValues.ADVANCED_RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(Tags.Items.GEMS_AMETHYST)
            .input(Tags.Items.GEMS_LAPIS)
            .result(RagiumItems.AZURE_SHARD.get())
            .time(160)
            .export(output, "azure_shard")

        FoundryRecipeBuilder
            .build()
            .input(RagiumItems.AZURE_SHARD)
            .input(Tags.Items.INGOTS_IRON)
            .result(RagiumItems.AZURE_STEEL_INGOT.get())
            .time(160)
            .export(output, RagiumConstantValues.AZURE_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(RagiumItems.DEEP_SCRAP)
            .input(RagiumCommonTags.Items.INGOTS_AZURE_STEEL)
            .result(RagiumItems.DEEP_STEEL_INGOT.get())
            .time(160)
            .export(output, RagiumConstantValues.DEEP_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.STORAGE_BLOCKS_CRIMSON_CRYSTAL)
            .input(RagiumCommonTags.Items.STORAGE_BLOCKS_WARPED_CRYSTAL)
            .result(RagiumItems.ELDRITCH_ORB.get(), 6)
            .time(160)
            .export(output, "eldritch_orb")
    }

    private fun atomicForge() {
        AtomicForgeRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .result(RagiumItems.RAGI_CRYSTAL.get())
            .time(20)
            .export(output, RagiumConstantValues.RAGI_CRYSTAL)
    }

    private fun particle() {
        ParticleCollisionRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_OBSIDIAN)
            .input(Items.DEEPSLATE)
            .result(RagiumItems.DEEP_SCRAP.get())
            .export(output, "deep_scrap")
    }
}
