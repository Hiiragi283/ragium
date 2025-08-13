package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.world.item.Items
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.CentrifugeFluidRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.ORITECH) {
    override fun buildRecipeInternal() {
        centrifuge()
        foundry()
        atomicForge()
        particle()
    }

    private fun centrifuge() {
        CentrifugeFluidRecipeBuilder
            .build()
            .input(gemOrDust(RagiumConst.CRIMSON_CRYSTAL))
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag)
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX.get())
            .export(output, "eldritch_flux")

        CentrifugeFluidRecipeBuilder
            .build()
            .input(gemOrDust(RagiumConst.WARPED_CRYSTAL))
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag)
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX.get())
            .export(output, "eldritch_flux_alt")
    }

    private fun foundry() {
        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(ingotOrDust("copper"))
            .result(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY).get())
            .export(output, RagiumConst.RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(ingotOrDust("gold"))
            .result(RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY).get())
            .export(output, RagiumConst.ADVANCED_RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust("amethyst"))
            .input(gemOrDust("lapis"))
            .result(RagiumItems.getGem(RagiumMaterialType.AZURE).get(), 2)
            .export(output, "azure_shard")

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(RagiumConst.AZURE))
            .input(ingotOrDust("iron"))
            .result(RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL).get())
            .export(output, RagiumConst.AZURE_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(RagiumItems.DEEP_SCRAP)
            .input(ingotOrDust(RagiumConst.AZURE_STEEL))
            .result(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL).get())
            .export(output, RagiumConst.DEEP_STEEL)
    }

    private fun atomicForge() {
        AtomicForgeRecipeBuilder
            .build()
            .input(gemOrDust("diamond"))
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE)
            .result(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL).get())
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
