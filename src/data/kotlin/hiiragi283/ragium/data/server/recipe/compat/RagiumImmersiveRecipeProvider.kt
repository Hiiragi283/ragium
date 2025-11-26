package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.common.register.IEItems
import blusunrize.immersiveengineering.data.recipes.builder.AlloyRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.ArcFurnaceRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.BaseHelpers
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.MixerRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTBasicFluidContent
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack

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
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(IETags.fluidRedstoneAcid, 1000))
            .save(output)
        // Hemp Fiber
        cropAndSeed(IEItems.Misc.HEMP_SEEDS, IEItems.Ingredients.HEMP_FIBER)

        alloy()
        crush()

        molten()

        misc()
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
        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val base: TagKey<Item>? = data.base
            val sap: HTBasicFluidContent? = data.sap
            // Base -> Sap
            if (base != null && sap != null) {
                SqueezerRecipeBuilder
                    .builder()
                    .output(sap.getFluid(), RagiumConst.LOG_TO_SAP)
                    .input(base)
                    .setEnergy(6400)
                    .build(output, sap.getIdWithPrefix("squeezing/"))
            }
            // Sap -> Molten
            val molten: HTBasicFluidContent = data.molten
            if (sap != null) {
                RefineryRecipeBuilder
                    .builder()
                    .input(sap.getFluidTag(), 1000)
                    .output(molten.getFluid(), RagiumConst.SAP_TO_MOLTEN)
                    .build(output, molten.getIdWithPrefix("${RagiumConst.MIXING}/"))
            }
            // Molten -> Gem
            val gemTag: TagKey<Item> = CommonMaterialPrefixes.GEM.itemTagKey(data)
            BottlingMachineRecipeBuilder
                .builder()
                .fluidInput(molten.getFluidTag(), 1000)
                .input(HTMoldType.GEM)
                .output(gemTag)
                .build(output, gemTag.location().withPrefix("bottling/"))
        }
        // Eldritch
        mixerFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX_CRIMSON)
        mixerFromData(RagiumMaterialRecipeData.ELDRITCH_FLUX_WARPED)
    }

    @JvmStatic
    private fun misc() {
        bottleFromData(FoodMaterialRecipeData.CHOCOLATE_INGOT)
    }

    //    Extension    //

    @JvmStatic
    private fun <T> BaseHelpers.ItemInput<T>.input(ingredient: SizedIngredient): T = input(ingredient.ingredient(), ingredient.count())

    @JvmStatic
    private fun ArcFurnaceRecipeBuilder.additive(ingredient: SizedIngredient): ArcFurnaceRecipeBuilder =
        additive(IngredientWithSize(ingredient.ingredient(), ingredient.count()))

    @JvmStatic
    private fun HTRecipeData.addFluidInput(index: Int, action: (TagKey<Fluid>, Int) -> Unit) {
        val fluidInput: HTRecipeData.InputEntry<Fluid> = this.fluidInputs.getOrNull(index) ?: return
        val fluidTag: TagKey<Fluid> = fluidInput
            .entry
            .right()
            .orElse(listOf())
            .getOrNull(0) ?: return
        action(fluidTag, fluidInput.amount)
    }

    @JvmStatic
    private fun alloyFromData(data: HTRecipeData) {
        fun <T : BaseHelpers.ItemOutput<*>> addOutputs(builder: T): T {
            for ((entry: Ior<Item, TagKey<Item>>, count: Int) in data.itemOutputs) {
                entry.map(
                    { item: Item -> builder.output(item, count) },
                    { tagKey: TagKey<Item> -> builder.output(tagKey, count) },
                )
            }
            return builder
        }

        val ingredients: List<SizedIngredient> = data.getSizedItemIngredients()
        if (ingredients.size <= 2) {
            // Alloy Kiln
            val builder: AlloyRecipeBuilder = AlloyRecipeBuilder.builder()
            for (ingredient: SizedIngredient in ingredients) {
                builder.input(ingredient)
            }
            addOutputs(builder)
                .setTime(200)
                .build(output, data.getModifiedId().withPrefix("${RagiumConst.ALLOYING}/"))
        }
        // Arc Furnace
        val builder1: ArcFurnaceRecipeBuilder = ArcFurnaceRecipeBuilder.builder()
        for (i: Int in ingredients.indices) {
            val ingredient: SizedIngredient = ingredients[i]
            if (i == 0) {
                builder1.input(ingredient)
            } else {
                builder1.additive(ingredient)
            }
        }
        addOutputs(builder1)
            .setTime(200)
            .setEnergy(102400)
            .build(output, data.getModifiedId().withPrefix("arc_furnace/"))
    }

    @JvmStatic
    private fun bottleFromData(data: HTRecipeData) {
        val builder: BottlingMachineRecipeBuilder = BottlingMachineRecipeBuilder.builder()
        // Input
        data.addFluidInput(0, builder::fluidInput)
        data.catalyst?.let(builder::input)
        // Output
        data.itemOutputs[0].let { (entry: Ior<Item, TagKey<Item>>, amount: Int, _) ->
            entry.map(
                { item: Item -> builder.output(item, amount) },
                { tagKey: TagKey<Item> -> builder.output(tagKey, amount) },
            )
        }
        data.catalyst?.let(builder::output)

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
    private fun mixerFromData(data: HTRecipeData) {
        val builder: MixerRecipeBuilder = MixerRecipeBuilder.builder()
        // Inputs
        data.addFluidInput(0, builder::fluidInput)
        for (ingredient: SizedIngredient in data.getSizedItemIngredients()) {
            builder.input(ingredient)
        }
        // Output
        val fluidOutput: FluidStack = data.getFluidStacks()[0]
        builder.output(fluidOutput.fluid, fluidOutput.amount)
        builder.build(output, data.getModifiedId().withPrefix("${RagiumConst.REFINING}/"))
    }
}
