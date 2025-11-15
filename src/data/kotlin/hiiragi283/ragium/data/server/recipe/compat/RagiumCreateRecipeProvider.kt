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
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.integration.RagiumCreateAddon
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient

object RagiumCreateRecipeProvider : HTRecipeProvider.Integration(RagiumConst.CREATE) {
    override fun buildRecipeInternal() {
        crushing { createBuilder<CrushingRecipe>(AllRecipeTypes.CRUSHING, it) }
        mixing { createBuilder<MixingRecipe>(AllRecipeTypes.MIXING, it) }

        // Sandpaper
        mapOf(
            CommonMaterialPrefixes.GEM to RagiumMaterialKeys.RAGI_CRYSTAL,
            CommonMaterialPrefixes.INGOT to RagiumMaterialKeys.IRIDESCENTIUM,
        ).forEach { (prefix: HTPrefixLike, key: HTMaterialKey) ->
            HTShapelessRecipeBuilder
                .equipment(RagiumCreateAddon.getSandPaper(key))
                .addIngredient(Items.PAPER)
                .addIngredient(prefix, key)
                .save(output)
        }
        // Cardboard
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(AllTags.AllItemTags.PULPIFIABLE.tag, 4))
            .addIngredient(fluidCreator.water(250))
            .setResult(resultHelper.item(AllItems.PULP))
            .save(output)

        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(AllItems.PULP),
                resultHelper.item(AllItems.CARDBOARD),
            ).save(output)
    }

    @JvmStatic
    private fun <R : StandardProcessingRecipe<*>> createBuilder(
        recipeType: IRecipeTypeInfo,
        id: ResourceLocation,
    ): StandardProcessingRecipe.Builder<R> =
        StandardProcessingRecipe.Builder<R>(recipeType.getSerializer<StandardProcessingRecipe.Serializer<R>>().factory(), id)

    @JvmStatic
    private fun crushing(factory: IdToFunction<StandardProcessingRecipe.Builder<*>>) {
        /**
         * @see com.simibubi.create.api.data.recipe.CrushingRecipeGen.rawOre
         */
        fun fromData(data: HTRecipeData) {
            val builder: StandardProcessingRecipe.Builder<*> = factory.apply(data.getModifiedId())
            builder.duration(400)
            // Input
            builder.require(data.getSizedItemIngredients()[0].first)
            // Output
            for ((entry: Ior<Item, TagKey<Item>>, amount: Int, chance: Float) in data.itemOutputs) {
                val item: Item? = entry.getLeft()
                if (item != null) {
                    builder.output(chance, item, amount)
                }
            }
            builder.build(output)
        }

        fromData(RagiumMaterialRecipeData.RAGINITE_ORE)
        fromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)

        fromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        fromData(RagiumMaterialRecipeData.WARPED_ORE)
    }

    @JvmStatic
    private fun mixing(factory: IdToFunction<StandardProcessingRecipe.Builder<*>>) {
        fun fromData(data: HTRecipeData, heat: HeatCondition = HeatCondition.NONE) {
            val builder: StandardProcessingRecipe.Builder<*> = factory.apply(data.getModifiedId())
            // Inputs
            for ((ingredient: Ingredient, count: Int) in data.getSizedItemIngredients()) {
                repeat(count) {
                    builder.require(ingredient)
                }
            }
            // Output
            builder.output(data.getItemStacks()[0].first)
            builder.requiresHeat(heat)
            builder.build(output)
        }

        fromData(RagiumMaterialRecipeData.RAGI_ALLOY, HeatCondition.HEATED)
        fromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY, HeatCondition.HEATED)

        fromData(RagiumMaterialRecipeData.AZURE_SHARD, HeatCondition.HEATED)
        fromData(RagiumMaterialRecipeData.AZURE_STEEL, HeatCondition.HEATED)

        fromData(RagiumMaterialRecipeData.DEEP_STEEL, HeatCondition.SUPERHEATED)

        fromData(RagiumMaterialRecipeData.ELDRITCH_PEARL, HeatCondition.SUPERHEATED)
        fromData(RagiumMaterialRecipeData.ELDRITCH_PEARL_BULK, HeatCondition.SUPERHEATED)

        fromData(RagiumMaterialRecipeData.NIGHT_METAL, HeatCondition.HEATED)
        fromData(RagiumMaterialRecipeData.IRIDESCENTIUM, HeatCondition.SUPERHEATED)
    }
}
