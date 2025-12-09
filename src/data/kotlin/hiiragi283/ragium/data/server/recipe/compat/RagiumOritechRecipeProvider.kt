package hiiragi283.ragium.data.server.recipe.compat

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import rearth.oritech.api.recipe.AtomicForgeRecipeBuilder
import rearth.oritech.api.recipe.CoolerRecipeBuilder
import rearth.oritech.api.recipe.FoundryRecipeBuilder
import rearth.oritech.api.recipe.LaserRecipeBuilder
import rearth.oritech.api.recipe.OritechRecipeBuilder
import rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder
import rearth.oritech.api.recipe.PulverizerRecipeBuilder

object RagiumOritechRecipeProvider : HTRecipeProvider.Integration(RagiumConst.ORITECH) {
    override fun buildRecipeInternal() {
        atomicForge()
        cooler()
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

        AtomicForgeRecipeBuilder
            .build()
            .input(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .input(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            .input(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            .result(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL))
            .time(20)
            .export(output, RagiumMaterialKeys.ELDRITCH_PEARL)

        atomicFromData(RagiumMaterialRecipeData.NIGHT_METAL)
    }

    @JvmStatic
    private fun cooler() {
        coolerFromData(FoodMaterialRecipeData.CHOCOLATE_INGOT)

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            CoolerRecipeBuilder
                .build()
                .fluidInput(data.molten.getFluidTag())
                .result(RagiumItems.getGem(data))
                .export(output, data)
        }
    }

    @JvmStatic
    private fun foundry() {
        foundryFromData(RagiumMaterialRecipeData.RAGI_ALLOY)
        foundryFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY)

        foundryFromData(RagiumMaterialRecipeData.AZURE_SHARD)
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
            .input(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO)
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
        // Ragium
        pulverizerFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
        pulverizerFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        pulverizerFromData(RagiumMaterialRecipeData.WARPED_ORE)
    }

    //    Extension    //

    fun OritechRecipeBuilder.input(prefix: HTPrefixLike, material: HTMaterialLike): OritechRecipeBuilder =
        input(prefix.itemTagKey(material))

    fun OritechRecipeBuilder.result(item: HTItemHolderLike, count: Int = 1): OritechRecipeBuilder = result(item.toStack(count))

    fun OritechRecipeBuilder.export(output: RecipeOutput, material: HTMaterialLike) {
        export(output, material.asMaterialName())
    }

    @JvmStatic
    private fun atomicFromData(data: HTRecipeData) {
        builderFromData(data, AtomicForgeRecipeBuilder.build())
    }

    @JvmStatic
    private fun coolerFromData(data: HTRecipeData) {
        builderFromData(data, CoolerRecipeBuilder.build())
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
        data.getIngredients().forEach(builder::input)

        data.fluidInputs.getOrNull(0)?.let { (entry: Either<List<Fluid>, List<TagKey<Fluid>>>, amount: Int) ->
            entry
                .mapBoth(List<Fluid>::first, List<TagKey<Fluid>>::first)
                .map(
                    { builder.fluidInput(it, amount / 1000f) },
                    { builder.fluidInput(it, amount / 1000f) },
                )
        }
        // Outputs
        for ((stack: ItemStack) in data.getItemStacks()) {
            builder.result(stack)
        }

        data.fluidOutputs.getOrNull(0)?.let { (entry: Ior<Fluid, TagKey<Fluid>>, amount: Int, _) ->
            val fluid: Fluid = entry.getLeft() ?: return@let
            builder.fluidOutput(fluid, amount / 1000f)
        }

        builder.export(output, data.getModifiedId().path)
    }
}
