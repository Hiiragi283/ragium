package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumChemicalRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        bathing()
        mixing()
        washing()
        reacting()
    }

    //    Bathing    //

    @JvmStatic
    private fun bathing() {
        // Diamond + Raginite -> Ragi-Crystal
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND)
            fluidIngredient = inputCreator.molten(RagiumMaterialKeys.RAGINITE) { it * 6 }
            result = resultCreator.material(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }

        waterBathing()

        eldritchBathing()
    }

    @JvmStatic
    private fun waterBathing() {
        // Cobblestone -> Mossy
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Tags.Items.COBBLESTONES_NORMAL)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(Items.MOSSY_COBBLESTONE)
            time /= 2
        }
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.DIRT)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(Items.MUD)
            time /= 2
        }
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.SPONGE)
            fluidIngredient = inputCreator.water(1000)
            result = resultCreator.create(Items.WET_SPONGE)
            time /= 2
        }

        // Sawdust -> Paper
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
            fluidIngredient = inputCreator.water(125)
            result = resultCreator.create(Items.PAPER)
            time /= 2
        }
    }

    @JvmStatic
    private fun eldritchBathing() {
        // Obsidian -> Crying Obsidian
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            fluidIngredient = inputCreator.molten(HCMaterialKeys.ELDRITCH)
            result = resultCreator.create(Items.CRYING_OBSIDIAN)
        }
        // Amethyst Block -> Budding Amethyst
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.AMETHYST)
            fluidIngredient = inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 9 }
            result = resultCreator.create(Items.BUDDING_AMETHYST)
        }
        // Skeleton Skull -> Wither Skeleton Skull
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.SKELETON_SKULL)
            fluidIngredient = inputCreator.molten(HCMaterialKeys.ELDRITCH)
            result = resultCreator.create(Items.WITHER_SKELETON_SKULL)
        }

        // Trial Key -> Ominous Key
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.TRIAL_KEY)
            fluidIngredient = inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 4 }
            result = resultCreator.create(Items.OMINOUS_TRIAL_KEY)
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Eldritch Flux
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            fluidResults += resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }
        // Liquid Dyes
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYE) {
            HTMixingRecipeBuilder.create(output) {
                itemIngredients += inputCreator.create(color.dyesTag)
                fluidIngredients += inputCreator.water(250)
                fluidResults += resultCreator.create(content, 250)
            }
        }

        // Nitric Acid + Sulfuric Acid -> Mixture Acid
        HTMixingRecipeBuilder.create(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.NITRIC_ACID, 500)
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID, 500)
            fluidResults += resultCreator.create(RagiumFluids.MIXTURE_ACID)
        }
    }

    //    Washing    //

    @JvmStatic
    fun washing() {
        // Gravel + Water -> Flint
        HTWashingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(Tags.Items.GRAVELS)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(Items.FLINT)
            extraResults += HTChancedItemResult.create {
                result = resultCreator.create(Items.FLINT)
                chance = fraction(1, 3)
            }
            time = 20 * 5
        }

        // Ash + Water -> Carbon
        HTWashingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH, 4)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON, 3)
            time = 20 * 5
        }
    }

    //    Reacting    //

    @JvmStatic
    fun reacting() {
        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER, 2)
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID)
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID, 2000)
            recipeId suffix "_from_saltpeter"
        }
        // 3x H2 + N2 -> 2x NH3
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
            fluidIngredients += inputCreator.create(RagiumFluids.HYDROGEN, 3000)
            fluidIngredients += inputCreator.create(RagiumFluids.NITROGEN)

            itemResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
            fluidResults += resultCreator.create(RagiumFluids.AMMONIA, 2000)
        }
        // NH3 + 2x O2 -> HNO3 + H2O
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM)
            fluidIngredients += inputCreator.create(RagiumFluids.AMMONIA)
            fluidIngredients += inputCreator.create(RagiumFluids.OXYGEN, 2000)

            itemResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM)
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID)
            fluidResults += resultCreator.water()
            recipeId suffix "_from_ammonia"
        }

        // S + H2O -> H2SO4
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.BLAZE_POWDER)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidIngredients += inputCreator.water(1000)

            itemResults += resultCreator.create(Items.BLAZE_POWDER)
            fluidResults += resultCreator.create(RagiumFluids.SULFURIC_ACID)
            recipeId suffix "_from_sulfur"
        }
    }
}
