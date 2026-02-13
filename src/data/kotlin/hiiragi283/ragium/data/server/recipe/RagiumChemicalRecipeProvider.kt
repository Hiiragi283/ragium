package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

object RagiumChemicalRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        bathing()
        canning()
        centrifuging()
        mixing()
        washing()
        reacting()
    }

    //    Bathing    //

    @JvmStatic
    private fun bathing() {
        // Diamond + Raginite -> Ragi-Crystal
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND)
            fluidIngredients += inputCreator.molten(RagiumMaterialKeys.RAGINITE) { it * 6 }
            itemResults += resultCreator.material(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
    }

    //    Canning    //

    @JvmStatic
    private fun canning() {
        // Water Bottle
        HTItemAndFluidRecipeBuilder.canning(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(HTPotionHelper.createPotion(Items.POTION, Potions.WATER))
            time /= 4
            recipeId replace id("water_bottle")
        }
        // Experience
        HTItemAndFluidRecipeBuilder.canning(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(HCFluids.EXPERIENCE, 250)
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE)
        }
        // Honey Bottle
        HTItemAndFluidRecipeBuilder.canning(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(HCFluids.HONEY, 250)
            result = resultCreator.create(Items.HONEY_BOTTLE)
        }
        // Mushroom Stew
        HTItemAndFluidRecipeBuilder.canning(output) {
            itemIngredient = inputCreator.create(Items.BOWL)
            fluidIngredient = inputCreator.create(HCFluids.MUSHROOM_STEW, 250)
            result = resultCreator.create(Items.MUSHROOM_STEW)
        }
        // Dragon Breath
        HTItemAndFluidRecipeBuilder.canning(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(HCFluids.DRAGON_BREATH, 250)
            result = resultCreator.create(Items.DRAGON_BREATH)
        }
    }

    //    Centrifuging    //

    @JvmStatic
    private fun centrifuging() {
        // Magma Cream -> Slime Ball + Blaze Powder

        // Air -> 4x N2 + O2
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Eldritch Flux
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            fluidResults += resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }
        // Liquid Dyes
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYE) {
            HTChemicalRecipeBuilder.mixing(output) {
                itemIngredients += inputCreator.create(color.dyesTag)
                fluidIngredients += inputCreator.water(250)
                fluidResults += resultCreator.create(content, 250)
            }
        }
        // Latex + Sulfur -> Molten Rubber
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidIngredients += inputCreator.create(HCFluids.LATEX, 1000)

            fluidResults += resultCreator.molten(CommonMaterialKeys.RUBBER) { it * 2 }
            recipeId suffix "_with_sulfur"
        }
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON)
            fluidIngredients += inputCreator.create(HCFluids.LATEX, 1000)

            fluidResults += resultCreator.molten(CommonMaterialKeys.RUBBER) { it * 4 }
            recipeId suffix "_with_sulfur_and_carbon"
        }

        // 2x Liq H2 + Liq O2 -> 2x Rocket Fuel
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.LIQUID_HYDROGEN, 500)
            fluidIngredients += inputCreator.create(RagiumFluids.LIQUID_OXYGEN, 500)
            fluidResults += resultCreator.create(RagiumFluids.ROCKET_FUEL)
        }
        // Nitric Acid + Sulfuric Acid -> Mixture Acid
        HTChemicalRecipeBuilder.mixing(output) {
            fluidIngredients += inputCreator.create(RagiumFluids.NITRIC_ACID, 500)
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID, 500)
            fluidResults += resultCreator.create(RagiumFluids.MIXTURE_ACID)
        }

        waterMixing()

        eldritchMixing()
    }

    @JvmStatic
    private fun waterMixing() {
        // Cobblestone -> Mossy
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Tags.Items.COBBLESTONES_NORMAL)
            fluidIngredients += inputCreator.water(250)
            itemResults += resultCreator.create(Items.MOSSY_COBBLESTONE)
            time /= 2
        }
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Items.DIRT)
            fluidIngredients += inputCreator.water(250)
            itemResults += resultCreator.create(Items.MUD)
            time /= 2
        }
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Items.SPONGE)
            fluidIngredients += inputCreator.water(1000)
            itemResults += resultCreator.create(Items.WET_SPONGE)
            time /= 2
        }

        // Sawdust -> Paper
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
            fluidIngredients += inputCreator.water(125)
            itemResults += resultCreator.create(Items.PAPER)
            time /= 2
        }
    }

    @JvmStatic
    private fun eldritchMixing() {
        // Obsidian -> Crying Obsidian
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH)
            itemResults += resultCreator.create(Items.CRYING_OBSIDIAN)
        }
        // Amethyst Block -> Budding Amethyst
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.AMETHYST)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 9 }
            itemResults += resultCreator.create(Items.BUDDING_AMETHYST)
        }
        // Skeleton Skull -> Wither Skeleton Skull
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Items.SKELETON_SKULL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH)
            itemResults += resultCreator.create(Items.WITHER_SKELETON_SKULL)
        }

        // Trial Key -> Ominous Key
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Items.TRIAL_KEY)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 4 }
            itemResults += resultCreator.create(Items.OMINOUS_TRIAL_KEY)
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
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALTPETER, 2)
            fluidIngredients += inputCreator.create(RagiumFluids.SULFURIC_ACID)
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID, 2000)
            recipeId suffix "_from_saltpeter"
        }
        // 3x H2 + N2 -> 2x NH3
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
            fluidIngredients += inputCreator.create(RagiumFluids.HYDROGEN, 3000)
            fluidIngredients += inputCreator.create(RagiumFluids.NITROGEN)

            itemResults += resultCreator.material(CommonTagPrefixes.DUST, VanillaMaterialKeys.IRON)
            fluidResults += resultCreator.create(RagiumFluids.AMMONIA, 2000)
        }
        // NH3 + 2x O2 -> HNO3 + H2O
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM)
            fluidIngredients += inputCreator.create(RagiumFluids.AMMONIA)
            fluidIngredients += inputCreator.create(RagiumFluids.OXYGEN, 2000)

            itemResults += resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.PLATINUM)
            fluidResults += resultCreator.create(RagiumFluids.NITRIC_ACID)
            fluidResults += resultCreator.water()
            recipeId suffix "_from_ammonia"
        }

        // S + H2O -> H2SO4
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(Items.BLAZE_POWDER)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidIngredients += inputCreator.water(1000)

            itemResults += resultCreator.create(Items.BLAZE_POWDER)
            fluidResults += resultCreator.create(RagiumFluids.SULFURIC_ACID)
            recipeId suffix "_from_sulfur"
        }
    }
}
