package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTDefinitionRecipeBuilder
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.recipe.custom.HTBucketExtractingRecipe
import hiiragi283.ragium.common.recipe.custom.HTBucketFillingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

object RagiumFluidRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        extracting()
        infusing()
        solidifying()

        biomass()
        bottle()
        crudeOil()
        crystal()
        exp()
        ragium()
        sap()
    }

    private fun biomass() {
        // Biomass -> Ethanol
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .fluidOutput(RagiumFluidContents.FUEL, 500)
            .fluidInput(RagiumFluidContents.BIOMASS)
            .saveSuffixed(output, "_from_biomass")
        // Ethanol + Plant Oil -> Fuel + Glycerol
    }

    private fun bottle() {
        // Exp Bottle
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.EXPERIENCE_BOTTLE)
            .itemInput(Items.GLASS_BOTTLE)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluidContents.EXPERIENCE, 250)
            .itemInput(Items.EXPERIENCE_BOTTLE)
            .saveSuffixed(output, "_from_exp")
        // Honey Bottle
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.HONEY_BOTTLE)
            .itemInput(Items.GLASS_BOTTLE)
            .fluidInput(RagiumFluidContents.HONEY, 250)
            .save(output)

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluidContents.HONEY, 250)
            .itemInput(Items.HONEY_BOTTLE)
            .saveSuffixed(output, "_from_honey")

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.HONEY)
            .itemInput(Items.HONEY_BLOCK)
            .saveSuffixed(output, "_from_block")
        // Water Bottle
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(createPotionStack(Potions.WATER, 3))
            .itemInput(Items.GLASS_BOTTLE, 3)
            .waterInput()
            .save(output, RagiumAPI.id("water_bottle"))

        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(Fluids.WATER, 333)
            .itemInput(
                DataComponentIngredient.of(
                    false,
                    DataComponents.POTION_CONTENTS,
                    PotionContents(Potions.WATER),
                    Items.POTION,
                ),
            ).saveSuffixed(output, "_from_water")
    }

    private fun crudeOil() {
        // Coal -> Crude Oil
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 125)
            .itemInput(HTTagPrefixes.GEM, VanillaMaterials.COAL)
            .saveSuffixed(output, "_from_coal")
        // Soul XX -> Crude Oil
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.CRUDE_OIL, 500)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .saveSuffixed(output, "_from_soul")

        // Crude Oil -> Naphtha + Tar
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.TAR)
            .fluidOutput(RagiumFluidContents.NAPHTHA, 750)
            .fluidInput(RagiumFluidContents.CRUDE_OIL)
            .save(output, RagiumAPI.id("naphtha_from_crude_oil"))
        // Naphtha -> Fuel + Sulfur
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.Dusts.SULFUR)
            .fluidOutput(RagiumFluidContents.FUEL, 750)
            .fluidInput(RagiumFluidContents.NAPHTHA)
            .save(output, RagiumAPI.id("fuel_from_naphtha"))

        // Tar -> Aromatic Compound
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.AROMATIC_COMPOUND, 200)
            .itemInput(RagiumItems.TAR)
            .saveSuffixed(output, "_from_tar")
        // Aromatic Compound + Sand -> TNT
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.TNT, 8)
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumFluidContents.AROMATIC_COMPOUND, 200)
            .save(output)
    }

    private fun crystal() {
        // Quartz
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.QUARTZ, 2)
            .itemInput(HTTagPrefixes.DUST, VanillaMaterials.QUARTZ)
            .waterInput(250)
            .saveSuffixed(output, "_from_water")
        // Amethyst
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.AMETHYST_SHARD, 2)
            .itemInput(HTTagPrefixes.DUST, VanillaMaterials.AMETHYST)
            .waterInput(250)
            .saveSuffixed(output, "_from_water")
    }

    private fun exp() {
        // Golden Apple
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .itemInput(Items.GOLDEN_APPLE)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 8000)
            .save(output)
        // Exp Berries
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(RagiumItems.EXP_BERRIES)
            .itemInput(Tags.Items.FOODS_BERRY)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 1000)
            .save(output)
        // Blaze Powder
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.BLAZE_POWDER)
            .itemInput(HTTagPrefixes.DUST, CommonMaterials.SULFUR)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
        // Wind Charge
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.WIND_CHARGE)
            .itemInput(Items.SNOWBALL)
            .fluidInput(RagiumFluidContents.EXPERIENCE, 250)
            .save(output)
    }

    private fun sap() {
        // XX Log -> Wood Dust + Sap
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluidContents.SAP, 100)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .saveSuffixed(output, "_from_log")
        // Sap -> Slime Ball
        /*HTMachineRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.SLIME_BALL)
            .fluidInput(RagiumFluidContents.SAP.commonTag, 1000)
            .saveSuffixed(output, "_from_sap")*/

        // Crimson Stem -> Wood Dust + Crimson Sap
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluidContents.CRIMSON_SAP, 100)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .saveSuffixed(output, "_from_crimson")
        // Crimson Sap -> Sap + Crimson Crystal
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.RawResources.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 100)
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.BLAZE_POWDER)
            .addIngredient(HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.CRIMSON_CRYSTAL)
            .save(output)

        // Warped Stem -> Wood Dust + Warped Sap
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(RagiumItems.Dusts.WOOD, 4)
            .fluidOutput(RagiumFluidContents.WARPED_SAP, 100)
            .itemInput(ItemTags.WARPED_STEMS)
            .saveSuffixed(output, "_from_warped")
        // Warped Sap -> Sap + Warped Crystal
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.REFINING)
            .itemOutput(RagiumItems.RawResources.WARPED_CRYSTAL)
            .fluidOutput(RagiumFluidContents.SAP, 100)
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag, 1000)
            .save(output)
        // Crimson Crystal -> Blaze Powder
        HTCookingRecipeBuilder
            .blasting(Items.ENDER_PEARL)
            .addIngredient(HTTagPrefixes.STORAGE_BLOCK, RagiumMaterials.WARPED_CRYSTAL)
            .save(output)
    }

    private fun ragium() {
        // Raginite -> 20 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 20)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGINITE)
            .saveSuffixed(output, "_from_raginite")
        // Advanced Ragi-Alloy -> 100 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 100)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGI_ALLOY)
            .saveSuffixed(output, "_from_alloy")
        // Advanced Ragi-Alloy -> 125 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 125)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.ADVANCED_RAGI_ALLOY)
            .saveSuffixed(output, "_from_advanced_alloy")
        // Ragi-Crystal -> 250 mb
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.MOLTEN_RAGIUM, 250)
            .itemInput(HTTagPrefixes.DUST, RagiumMaterials.RAGI_CRYSTAL)
            .saveSuffixed(output, "_from_crystal")

        // Ragium Essence
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(RagiumItems.RAGIUM_ESSENCE)
            .itemInput(HTTagPrefixes.DUST, VanillaMaterials.QUARTZ)
            .fluidInput(RagiumFluidContents.MOLTEN_RAGIUM)
            .save(output)
    }

    //    Extracting    //

    private fun extracting() {
        // Magma Block -> Cobblestone + Lava
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .itemOutput(Items.COBBLESTONE)
            .fluidOutput(Fluids.LAVA, 100)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_magma_block")

        // Exp Berries -> Liquid Exp
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.EXTRACTING)
            .fluidOutput(RagiumFluidContents.EXPERIENCE, 50)
            .itemInput(RagiumItems.EXP_BERRIES)
            .saveSuffixed(output, "_from_berries")

        save(
            RagiumAPI.id("extracting/buckets"),
            HTBucketExtractingRecipe,
        )
        save(
            RagiumAPI.id("infusing/buckets"),
            HTBucketFillingRecipe,
        )
    }

    //    Infusing    //

    private fun infusing() {
        // Dirt -> Mud
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.MUD)
            .itemInput(Items.DIRT)
            .waterInput(250)
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(Items.CLAY)
            .itemInput(RagiumBlocks.SILT)
            .waterInput(250)
            .saveSuffixed(output, "_from_silt")

        // Milk + Snow -> Ice Cream
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.INFUSING)
            .itemOutput(RagiumItems.ICE_CREAM)
            .itemInput(Items.SNOWBALL)
            .milkInput(250)
            .save(output)
    }

    //    Solidifying    //

    private fun solidifying() {
        // Water -> Ice
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.SOLIDIFYING)
            .itemOutput(Items.ICE)
            .waterInput()
            .catalyst(RagiumItemTags.MOLDS_BLOCK)
            .save(output)
        // Water -> Snowball
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.SOLIDIFYING)
            .itemOutput(Items.SNOWBALL)
            .waterInput(250)
            .catalyst(RagiumItemTags.MOLDS_BALL)
            .save(output)

        // Honey -> Honey Block
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.SOLIDIFYING)
            .itemOutput(Items.HONEY_BLOCK)
            .fluidInput(RagiumFluidContents.HONEY)
            .catalyst(RagiumItemTags.MOLDS_BLOCK)
            .save(output)

        // Sap -> Slimeball
        HTDefinitionRecipeBuilder(RagiumRecipeSerializers.SOLIDIFYING)
            .itemOutput(Items.SLIME_BALL)
            .fluidInput(RagiumFluidContents.SAP)
            .catalyst(RagiumItemTags.MOLDS_BALL)
            .save(output)
    }
}
