package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.CentrifugeRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.LaserRecipeBuilder
import rearth.oritech.api.recipe.OritechRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder
import rearth.oritech.api.recipe.PulverizerRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.ORITECH) {
    override fun buildRecipeInternal() {
        atomicForge()
        centrifuge()
        foundry()
        laser()
        particle()
        pulverize()
    }

    @JvmStatic
    private fun atomicForge() {
        AtomicForgeRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .result(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL))
            .time(20)
            .export(output, RagiumMaterialKeys.RAGI_CRYSTAL)

        atomicFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL)
        atomicFromData(RagiumMaterialRecipeData.NIGHT_METAL)
        atomicFromData(RagiumMaterialRecipeData.IRIDESCENTIUM)
    }

    @JvmStatic
    private fun centrifuge() {
        CentrifugeRecipeBuilder
            .build()
            .input(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
            .fluidOutput(RagiumFluidContents.ELDRITCH_FLUX)
    }

    @JvmStatic
    private fun foundry() {
        foundryFromData(RagiumMaterialRecipeData.RAGI_ALLOY)
        foundryFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY)

        foundryFromData(RagiumMaterialRecipeData.AZURE_STEEL)

        foundryFromData(RagiumMaterialRecipeData.DEEP_STEEL)
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

    @JvmStatic
    private fun pulverize() {
        // Vanilla
        pulverizerFromData(VanillaMaterialRecipeData.AMETHYST_DUST)
        pulverizerFromData(VanillaMaterialRecipeData.ECHO_DUST)
        pulverizerFromData(VanillaMaterialRecipeData.BLACKSTONE_DUST)
        // Ragium
        pulverizerFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
        pulverizerFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        pulverizerFromData(RagiumMaterialRecipeData.WARPED_ORE)
    }

    //    Extension    //

    fun OritechRecipeBuilder.input(prefix: HTPrefixLike, material: HTMaterialLike): OritechRecipeBuilder =
        input(prefix.itemTagKey(material))

    fun OritechRecipeBuilder.result(item: HTItemHolderLike, count: Int = 1): OritechRecipeBuilder = result(item.toStack(count))

    fun OritechRecipeBuilder.fluidInput(content: HTFluidContent<*, *, *>): OritechRecipeBuilder = fluidInput(content.commonTag)

    fun OritechRecipeBuilder.fluidOutput(content: HTFluidContent<*, *, *>): OritechRecipeBuilder = fluidOutput(content.get())

    fun OritechRecipeBuilder.export(output: RecipeOutput, material: HTMaterialLike) {
        export(output, material.asMaterialName())
    }

    @JvmStatic
    private fun atomicFromData(data: HTRecipeData) {
        builderFromData(data, AtomicForgeRecipeBuilder.build())
    }

    @JvmStatic
    private fun foundryFromData(data: HTRecipeData) {
        builderFromData(data, FoundryRecipeBuilder.build())
    }

    @JvmStatic
    private fun pulverizerFromData(data: HTRecipeData) {
        builderFromData(data, PulverizerRecipeBuilder.build())
    }

    @JvmStatic
    private fun builderFromData(data: HTRecipeData, builder: OritechRecipeBuilder) {
        // Inputs
        for ((ingredient: Ingredient, _) in data.getIngredients()) {
            builder.input(ingredient)
        }
        // Output
        builder.result(data.getOutputStacks())
        builder.export(output, data.getModifiedId().path)
    }
}
