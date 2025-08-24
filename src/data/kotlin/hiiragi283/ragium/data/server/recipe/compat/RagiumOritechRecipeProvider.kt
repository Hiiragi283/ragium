package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.world.item.Items
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.CentrifugeFluidRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.OritechRecipeBuilder
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
            .input(gemOrDust(RagiumMaterialType.CRIMSON_CRYSTAL))
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag)
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX.get())
            .export(output, "eldritch_flux")

        CentrifugeFluidRecipeBuilder
            .build()
            .input(gemOrDust(RagiumMaterialType.WARPED_CRYSTAL))
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag)
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX.get())
            .export(output, "eldritch_flux_alt")
    }

    private fun foundry() {
        FoundryRecipeBuilder
            .build()
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .input(ingotOrDust(HTVanillaMaterialType.COPPER))
            .result(RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY).get())
            .export(output, RagiumConst.RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .input(ingotOrDust(HTVanillaMaterialType.GOLD))
            .result(RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY).get())
            .export(output, RagiumConst.ADVANCED_RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(HTVanillaMaterialType.AMETHYST))
            .input(gemOrDust(HTVanillaMaterialType.LAPIS))
            .result(RagiumItems.getGem(RagiumMaterialType.AZURE).get(), 2)
            .export(output, "azure_shard")

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(RagiumMaterialType.AZURE))
            .input(ingotOrDust(HTVanillaMaterialType.IRON))
            .result(RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL).get())
            .export(output, RagiumConst.AZURE_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(RagiumItems.DEEP_SCRAP)
            .input(ingotOrDust(RagiumMaterialType.AZURE_STEEL))
            .result(RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL).get())
            .export(output, RagiumConst.DEEP_STEEL)
    }

    private fun atomicForge() {
        AtomicForgeRecipeBuilder
            .build()
            .input(gemOrDust(HTVanillaMaterialType.DIAMOND))
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .result(RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL).get())
            .time(20)
            .export(output, RagiumConst.RAGI_CRYSTAL)
    }

    private fun particle() {
        ParticleCollisionRecipeBuilder
            .build()
            .input(HTItemMaterialVariant.DUST, HTVanillaMaterialType.OBSIDIAN)
            .input(Items.DEEPSLATE)
            .result(RagiumItems.DEEP_SCRAP.get())
            .time(2500)
            .export(output, "deep_scrap")
    }

    //    Extension    //

    fun OritechRecipeBuilder.input(variant: HTItemMaterialVariant, material: HTMaterialType): OritechRecipeBuilder =
        input(variant.itemTagKey(material))
}
