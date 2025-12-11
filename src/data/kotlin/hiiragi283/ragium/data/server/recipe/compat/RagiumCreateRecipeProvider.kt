package hiiragi283.ragium.data.server.recipe.compat

import com.simibubi.create.AllItems
import com.simibubi.create.AllRecipeTypes
import com.simibubi.create.AllTags
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe
import com.simibubi.create.content.kinetics.mixer.MixingRecipe
import com.simibubi.create.content.processing.recipe.HeatCondition
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.function.IdToFunction
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.variant.HTOreVariant
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.VanillaMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.SizedIngredient

object RagiumCreateRecipeProvider : HTRecipeProvider.Integration(RagiumConst.CREATE) {
    override fun buildRecipeInternal() {
        crushing { createBuilder<CrushingRecipe>(AllRecipeTypes.CRUSHING, it) }
        mixing { createBuilder<MixingRecipe>(AllRecipeTypes.MIXING, it) }

        // Sandpaper
        HTShapelessRecipeBuilder
            .create(RagiumIntegrationItems.getSandPaper(RagiumMaterialKeys.RAGI_CRYSTAL))
            .addIngredient(Items.PAPER)
            .addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .setCategory(CraftingBookCategory.EQUIPMENT)
            .save(output)
        // Cardboard
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(AllTags.AllItemTags.PULPIFIABLE.tag, 4))
            .addIngredient(fluidCreator.water(250))
            .setResult(resultHelper.item(AllItems.PULP))
            .save(output)

        compressingTo(HTMoldType.PLATE, itemCreator.fromItem(AllItems.PULP), resultHelper.item(AllItems.CARDBOARD))
    }

    @JvmStatic
    private fun <R : StandardProcessingRecipe<*>> createBuilder(
        recipeType: IRecipeTypeInfo,
        id: ResourceLocation,
    ): StandardProcessingRecipe.Builder<R> =
        StandardProcessingRecipe.Builder<R>(recipeType.getSerializer<StandardProcessingRecipe.Serializer<R>>().factory(), id)

    @JvmStatic
    private fun crushing(factory: IdToFunction<StandardProcessingRecipe.Builder<*>>) {
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key: HTMaterialKey, ore: HTItemHolderLike) ->
            val basePrefix: HTMaterialPrefix = getDefaultPrefix(key) ?: return@forEach
            val result: ItemLike = RagiumItems.MATERIALS[basePrefix, key] ?: return@forEach
            val count: Float = when (key) {
                RagiumMaterialKeys.RAGINITE -> 6.5f
                else -> 1.75f
            }

            factory
                .apply(ore.getId())
                .require(ore)
                .duration(250)
                .output(result, count.toInt())
                .output(count - (count.toInt()), result)
                .output(0.75f, AllItems.EXP_NUGGET.get(), 1)
                .output(0.125f, variant.baseStone)
                .build(output)
        }

        fun fromData(data: HTRecipeData) {
            val builder: StandardProcessingRecipe.Builder<*> = factory.apply(data.getModifiedId())
            // Input
            builder.require(data.getIngredients()[0])
            // Outputs
            for ((stack: ItemStack) in data.getItemStacks()) {
                builder.output(stack)
            }
            builder.build(output)
        }

        // Vanilla
        fromData(VanillaMaterialRecipeData.AMETHYST_DUST)
        fromData(VanillaMaterialRecipeData.ECHO_DUST)

        // Ragium
        fromData(RagiumMaterialRecipeData.AZURE_DUST)

        fromData(FoodMaterialRecipeData.MINCED_MEAT)
    }

    @JvmStatic
    private fun mixing(factory: IdToFunction<StandardProcessingRecipe.Builder<*>>) {
        fun fromData(data: HTRecipeData, heat: HeatCondition = HeatCondition.NONE) {
            val builder: StandardProcessingRecipe.Builder<*> = factory.apply(data.getModifiedId())
            // Inputs
            for (ingredient: SizedIngredient in data.getSizedItemIngredients()) {
                repeat(ingredient.count()) {
                    builder.require(ingredient.ingredient())
                }
            }
            data.getSizedFluidIngredients().forEach(builder::require)
            // Output
            for ((stack: ItemStack) in data.getItemStacks()) {
                builder.output(stack)
            }
            data.getFluidStacks().forEach(builder::output)

            builder.requiresHeat(heat)
            builder.build(output)
        }

        fromData(RagiumMaterialRecipeData.RAGI_ALLOY, HeatCondition.HEATED)
        fromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY, HeatCondition.HEATED)

        fromData(RagiumMaterialRecipeData.AZURE_SHARD, HeatCondition.HEATED)
        fromData(RagiumMaterialRecipeData.AZURE_STEEL, HeatCondition.HEATED)

        fromData(RagiumMaterialRecipeData.DEEP_STEEL, HeatCondition.SUPERHEATED)

        fromData(RagiumMaterialRecipeData.ELDRITCH_FLUX, HeatCondition.SUPERHEATED)

        fromData(RagiumMaterialRecipeData.NIGHT_METAL, HeatCondition.HEATED)
        fromData(RagiumMaterialRecipeData.IRIDESCENT_POWDER, HeatCondition.SUPERHEATED)

        fromData(RagiumMaterialRecipeData.BLACK_RUBBER_SHEET, HeatCondition.HEATED)
    }
}
