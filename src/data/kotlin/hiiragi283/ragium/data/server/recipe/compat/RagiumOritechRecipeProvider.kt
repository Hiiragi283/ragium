package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider() {
    private fun gemOrDust(name: String): Ingredient = CompoundIngredient.of(
        Ingredient.of(itemTagKey(commonId(RagiumConst.DUSTS, name))),
        Ingredient.of(itemTagKey(commonId(RagiumConst.GEMS, name))),
    )

    private fun ingotOrDust(name: String): Ingredient = CompoundIngredient.of(
        Ingredient.of(itemTagKey(commonId(RagiumConst.DUSTS, name))),
        Ingredient.of(itemTagKey(commonId(RagiumConst.INGOTS, name))),
    )

    override fun buildRecipeInternal() {
        foundry()
        atomicForge()
        particle()
    }

    private fun foundry() {
        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(ingotOrDust("copper"))
            .result(RagiumItems.Ingots.RAGI_ALLOY.get())
            .time(160)
            .export(output, RagiumConst.RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(ingotOrDust("gold"))
            .result(RagiumItems.Ingots.ADVANCED_RAGI_ALLOY.get())
            .time(160)
            .export(output, RagiumConst.ADVANCED_RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust("amethyst"))
            .input(gemOrDust("lapis"))
            .result(RagiumItems.Gems.AZURE_SHARD.get(), 2)
            .time(160)
            .export(output, "azure_shard")

        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.GEMS_AZURE)
            .input(ingotOrDust("iron"))
            .result(RagiumItems.Ingots.AZURE_STEEL.get())
            .time(160)
            .export(output, RagiumConst.AZURE_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(RagiumItems.DEEP_SCRAP)
            .input(ingotOrDust(RagiumConst.AZURE_STEEL))
            .result(RagiumItems.Ingots.DEEP_STEEL.get())
            .time(160)
            .export(output, RagiumConst.DEEP_STEEL)

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
            .input(gemOrDust("diamond"))
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .result(RagiumItems.Gems.RAGI_CRYSTAL.get())
            .time(20)
            .export(output, RagiumConst.RAGI_CRYSTAL)
    }

    private fun particle() {
        ParticleCollisionRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_OBSIDIAN)
            .input(Items.DEEPSLATE)
            .result(RagiumItems.DEEP_SCRAP.get())
            .time(2500)
            .export(output, "deep_scrap")
    }
}
