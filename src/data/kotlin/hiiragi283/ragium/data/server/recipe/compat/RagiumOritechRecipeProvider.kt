package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.CentrifugeRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.LaserRecipeBuilder
import rearth.oritech.api.recipe.OritechRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.ORITECH) {
    override fun buildRecipeInternal() {
        atomicForge()
        centrifuge()
        foundry()
        laser()
        particle()
    }

    @JvmStatic
    private fun atomicForge() {
        AtomicForgeRecipeBuilder
            .build()
            .input(gemOrDust(VanillaMaterialKeys.DIAMOND))
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .result(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL))
            .time(20)
            .export(output, RagiumMaterialKeys.RAGI_CRYSTAL)
    }

    @JvmStatic
    private fun centrifuge() {
        CentrifugeRecipeBuilder
            .build()
            .input(gemOrDust(RagiumMaterialKeys.ELDRITCH_PEARL))
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX)
    }

    @JvmStatic
    private fun foundry() {
        FoundryRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .input(ingotOrDust(VanillaMaterialKeys.COPPER))
            .result(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY))
            .export(output, RagiumMaterialKeys.RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .input(ingotOrDust(VanillaMaterialKeys.GOLD))
            .result(RagiumItems.getIngot(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
            .export(output, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(VanillaMaterialKeys.AMETHYST))
            .input(gemOrDust(VanillaMaterialKeys.LAPIS))
            .result(RagiumItems.getGem(RagiumMaterialKeys.AZURE), 2)
            .export(output, "azure_shard")

        FoundryRecipeBuilder
            .build()
            .input(gemOrDust(RagiumMaterialKeys.AZURE))
            .input(ingotOrDust(VanillaMaterialKeys.IRON))
            .result(RagiumItems.getIngot(RagiumMaterialKeys.AZURE_STEEL))
            .export(output, RagiumMaterialKeys.AZURE_STEEL)

        FoundryRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL)
            .input(ingotOrDust(RagiumMaterialKeys.AZURE_STEEL))
            .result(RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL))
            .export(output, RagiumMaterialKeys.DEEP_STEEL)
    }

    @JvmStatic
    private fun laser() {
        LaserRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.ORE, VanillaMaterialKeys.REDSTONE)
            .result(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE))
            .export(output, RagiumMaterialKeys.RAGINITE)
    }

    @JvmStatic
    private fun particle() {
        ParticleCollisionRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN)
            .input(Items.DEEPSLATE)
            .result(RagiumItems.getScrap(RagiumMaterialKeys.DEEP_STEEL))
            .time(2500)
            .export(output, "deep_scrap")
    }

    //    Extension    //

    fun OritechRecipeBuilder.input(prefix: HTMaterialPrefix, material: HTMaterialLike): OritechRecipeBuilder =
        input(prefix.itemTagKey(material))

    fun OritechRecipeBuilder.result(item: HTItemHolderLike, count: Int = 1): OritechRecipeBuilder = result(item.toStack(count))

    fun OritechRecipeBuilder.fluidInput(content: HTFluidContent<*, *, *>): OritechRecipeBuilder = fluidInput(content.commonTag)

    fun OritechRecipeBuilder.fluidOutput(content: HTFluidContent<*, *, *>): OritechRecipeBuilder = fluidOutput(content.get())

    fun OritechRecipeBuilder.export(output: RecipeOutput, material: HTMaterialLike) {
        export(output, material.asMaterialName())
    }
}
