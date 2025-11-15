package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.data.recipes.builder.BaseHelpers
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTArcFurnaceRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * @see blusunrize.immersiveengineering.data.recipes.MultiblockRecipes
 */
object RagiumImmersiveRecipeProvider : HTRecipeProvider.Integration(RagiumConst.IMMERSIVE) {
    override fun buildRecipeInternal() {
        // Treated Planks
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(ItemTags.PLANKS),
                fluidCreator.fromTagKey(IETags.fluidCreosote, 125),
            ).addResult(resultHelper.item(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!))
            .save(output)
        // Redstone Acid
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(IETags.fluidRedstoneAcid, 1000))
            .save(output)

        alloy()
        crush()
        
        molten()
    }

    @JvmStatic
    private fun alloy() {
        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY)
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY)
        alloyFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL)

        alloyFromData(RagiumMaterialRecipeData.AZURE_SHARD)
        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL)

        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL)

        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL)
        alloyFromData(RagiumMaterialRecipeData.IRIDESCENTIUM)
    }
    
    @JvmStatic
    private fun crush() {
        crushFromData(RagiumMaterialRecipeData.RAGINITE_ORE)
        crushFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
        crushFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
        crushFromData(RagiumMaterialRecipeData.WARPED_ORE)
    }

    @JvmStatic
    private fun molten() {
        // Crimson
        crushFromData(RagiumMaterialRecipeData.CRIMSON_ORE)

        squeezeFromData(RagiumMaterialRecipeData.CRIMSON_SAP)
        refineFromData(RagiumMaterialRecipeData.CRIMSON_BLOOD)
        bottleFromData(RagiumMaterialRecipeData.CRIMSON_CRYSTAL)
        // Warped
        crushFromData(RagiumMaterialRecipeData.WARPED_ORE)

        squeezeFromData(RagiumMaterialRecipeData.WARPED_SAP)
        refineFromData(RagiumMaterialRecipeData.DEW_OF_THE_WARP)
        bottleFromData(RagiumMaterialRecipeData.WARPED_CRYSTAL)
        // Eldritch
    }

    //    Extension    //

    @JvmStatic
    private fun <T> BaseHelpers.ItemInput<T>.input(ingredient: SizedIngredient): T = input(ingredient.ingredient(), ingredient.count())

    @JvmStatic
    private fun alloyFromData(data: HTRecipeData) {
        val builder: HTArcFurnaceRecipeBuilder = HTArcFurnaceRecipeBuilder.builder()
        // Inputs
        for (ingredient: SizedIngredient in data.getSizedItemIngredients()) {
            builder.input(ingredient)
        }
        // Outputs
        for ((entry: Ior<Item, TagKey<Item>>, count: Int) in data.itemOutputs) {
            entry.map(
                { item: Item -> builder.output(item, count) },
                { tagKey: TagKey<Item> -> builder.output(tagKey, count) },
            )
        }
        builder.build(output, data.getModifiedId())
    }

    @JvmStatic
    private fun bottleFromData(data: HTRecipeData) {
        val builder: BottlingMachineRecipeBuilder = BottlingMachineRecipeBuilder.builder()
        // Input
        val fluidInput: HTRecipeData.InputEntry<Fluid> = data.fluidInputs.getOrNull(0) ?: return
        val fluidTag: TagKey<Fluid> = fluidInput
            .entry
            .right()
            .orElse(listOf())
            .getOrNull(0) ?: return
        builder.fluidInput(fluidTag, fluidInput.amount)
        // Output
        data.itemOutputs[0].let { (entry: Ior<Item, TagKey<Item>>, amount: Int, _) ->
            entry.map(
                { item: Item -> builder.output(item, amount) },
                { tagKey: TagKey<Item> -> builder.output(tagKey, amount) },
            )
        }
        builder.build(output, data.getModifiedId().withPrefix("bottling/"))
    }

    @JvmStatic
    private fun crushFromData(data: HTRecipeData) {
        val builder: CrusherRecipeBuilder = CrusherRecipeBuilder.builder()
        // Input
        builder.input(data.getIngredients()[0])
        // Outputs
        val outputs: List<HTRecipeData.OutputEntry<Item>> = data.itemOutputs
        for (i: Int in outputs.indices) {
            val (entry: Ior<Item, TagKey<Item>>, count: Int, chance: Float) = outputs[i]
            when (i) {
                0 -> entry.map(
                    { item: Item -> builder.output(item, count) },
                    { tagKey: TagKey<Item> -> builder.output(tagKey, count) },
                )

                else -> entry.map(
                    { item: Item -> builder.addSecondary(IngredientWithSize(Ingredient.of(item), count), chance) },
                    { tagKey: TagKey<Item> -> builder.addSecondary(IngredientWithSize(tagKey, count), chance) },
                )
            }
        }
        builder.build(output, data.getModifiedId().withPrefix("${RagiumConst.CRUSHING}/"))
    }

    @JvmStatic
    private fun refineFromData(data: HTRecipeData) {
        RefineryRecipeBuilder
            .builder()
            .input(data.getSizedFluidIngredients()[0])
            .input(data.getSizedFluidIngredients().getOrNull(1))
            .output(data.getFluidStacks()[0])
            .build(output, data.getModifiedId().withPrefix("${RagiumConst.REFINING}/"))
    }

    @JvmStatic
    private fun squeezeFromData(data: HTRecipeData, energy: Int = 6400) {
        SqueezerRecipeBuilder
            .builder()
            .output(data.getFluidStacks()[0])
            .input(data.getSizedItemIngredients()[0])
            .setEnergy(energy)
            .build(output, data.getModifiedId().withPrefix("squeezing/"))
    }
}
