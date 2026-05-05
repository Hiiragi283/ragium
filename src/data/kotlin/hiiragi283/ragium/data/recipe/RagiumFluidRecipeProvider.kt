package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.VanillaColoredContents
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.data.recipe.builder.HTTankInteractionRecipeBuilder
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndFluidToItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        refining()
        mixing()
        washing()

        tankInteraction()
    }

    //    Refining    //

    @JvmStatic
    private fun refining() {
        waterRefining()
        expRefining()
        eldritchRefining()
    }

    @JvmStatic
    private fun waterRefining() {
        // Cobblestone -> Mossy
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Tags.Items.COBBLESTONES_NORMAL)
            fluidIngredient = inputCreator.water(250)
            result += resultCreator.create(Items.MOSSY_COBBLESTONE)
            time /= 2
        }
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.DIRT)
            fluidIngredient = inputCreator.water(250)
            result += resultCreator.create(Items.MUD)
            time /= 2
        }
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.SPONGE)
            fluidIngredient = inputCreator.water()
            result += resultCreator.create(Items.WET_SPONGE)
            time /= 2
        }

        // Sawdust -> Paper
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
            fluidIngredient = inputCreator.water(125)
            result += resultCreator.create(Items.PAPER)
            time /= 2
        }
    }

    @JvmStatic
    private fun expRefining() {
        // Quartz Block -> Ghast Tear
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ)
            fluidIngredient = inputCreator.create(HCFluids.EXPERIENCE, 500)
            result = resultCreator.create(Items.GHAST_TEAR)
            recipeId suffix "_from_quartz"
        }
        // Sulfur Dust -> Blaze Powder
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidIngredient = inputCreator.create(HCFluids.EXPERIENCE, 250)
            result = resultCreator.create(Items.BLAZE_POWDER)
            recipeId suffix "_from_sulfur"
        }
        // Leather -> Phantom Membrane
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Tags.Items.LEATHERS)
            fluidIngredient = inputCreator.create(HCFluids.EXPERIENCE, 250)
            result = resultCreator.create(Items.PHANTOM_MEMBRANE)
            recipeId suffix "_from_leather"
        }
        // Snowball -> Wind Charge
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.SNOWBALL)
            fluidIngredient = inputCreator.create(HCFluids.EXPERIENCE, 250)
            result = resultCreator.create(Items.WIND_CHARGE)
            recipeId suffix "_from_snowball"
        }
    }

    @JvmStatic
    private fun eldritchRefining() {
        fun eldritch(multiplier: Int): HTFluidIngredient = inputCreator.create(
            HiiragiCoreTags.Fluids.ELDRITCH,
            HTConst.INGOT_AMOUNT * multiplier,
        )

        // Obsidian -> Crying Obsidian
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            fluidIngredient = eldritch(1)
            result = resultCreator.create(Items.CRYING_OBSIDIAN)
        }
        // Amethyst Block -> Budding Amethyst
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST)
            fluidIngredient = eldritch(9)
            result = resultCreator.create(Items.BUDDING_AMETHYST)
        }
        // Skeleton Skull -> Wither Skeleton Skull
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.SKELETON_SKULL)
            fluidIngredient = eldritch(1)
            result = resultCreator.create(Items.WITHER_SKELETON_SKULL)
        }

        // Trial Key -> Ominous Key
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.TRIAL_KEY)
            fluidIngredient = eldritch(4)
            result = resultCreator.create(Items.OMINOUS_TRIAL_KEY)
        }

        // Wither Doll -> Wither Star
        HTItemAndFluidToItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(HCItems.WITHER_DOLL)
            fluidIngredient = eldritch(4)
            result = resultCreator.create(HCItems.WITHER_STAR)
        }
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Diamond + Raginite -> Ragi-Crystal
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND)
            fluidIngredient = inputCreator.molten(RagiumMaterialKeys.RAGINITE) { it * 6 }
            result += resultCreator.material(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
        // Liquid Dyes
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DyeContents) {
            // Dye + Water -> Liquid Dye
            HTMixingRecipeBuilder.create(output) {
                itemIngredients += inputCreator.create(color.dyesTag)
                fluidIngredient = inputCreator.water(250)
                result += resultCreator.create(content, 250)
            }
            // Liquid Dye -> Dye
            VanillaColoredContents.DYE[color]?.let { dye: ItemLike ->
                HTFreezingRecipeBuilder.create(output) {
                    ingredient = inputCreator.create(content, 250)
                    catalyst += HTBluePrintIngredient(0).toVanilla()
                    result = resultCreator.create(dye)
                }
            }
            // Gravel + Sand + Liquid Dye -> Concrete
            VanillaColoredContents.CONCRETE[color]?.let { concrete: ItemLike ->
                HTMixingRecipeBuilder.create(output) {
                    itemIngredients += inputCreator.create(Tags.Items.GRAVELS, 4)
                    itemIngredients += inputCreator.create(Tags.Items.SANDS, 4)
                    fluidIngredient = inputCreator.create(content, 250)
                    result += resultCreator.create(concrete, 8)
                }
                // Powder + Water -> Concrete
                VanillaColoredContents.CONCRETE_POWDER[color]?.let { powder ->
                    HTMixingRecipeBuilder.create(output) {
                        itemIngredients += inputCreator.create(powder)
                        fluidIngredient = inputCreator.water(125)
                        result += resultCreator.create(concrete)
                        time /= 8
                        recipeId suffix "_by_water"
                    }
                }
            }
        }
    }

    //    Washing    //

    @JvmStatic
    private fun washing() {
        // Gravel + Water -> Flint
        RagiumRecipeBuilder.washing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            results += resultCreator.create(Items.FLINT)
            results += resultCreator.create(Items.FLINT, chance = fraction(1, 3))
            time = 20 * 5
        }
        // Sand -> Quartz Dust + Borax Dust
        RagiumRecipeBuilder.washing(output) {
            ingredient = inputCreator.create(Tags.Items.SANDS)
            results += resultCreator.material(CommonParts.DUST, VanillaMaterialKeys.QUARTZ, chance = fraction(1, 2))
            results += resultCreator.material(CommonParts.DUST, RagiumMaterialKeys.BORAX, chance = fraction(1, 4))
        }
        // Ash + Water -> Carbon
        RagiumRecipeBuilder.washing(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH, 4)
            results += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.CARBON, 3)
            results += resultCreator.material(CommonParts.DUST, CommonMaterialKeys.CARBON, chance = fraction(1, 4))
            time = 20 * 5
        }
    }

    //    Tank Interaction    //

    @JvmStatic
    private fun tankInteraction() {
        HTTankInteractionRecipeBuilder.emptying(output) {
            ingredient = itemCreator.create(RagiumItems.MERCURY_BOTTLE)
            fluidResult = resultCreator.create(RagiumFluids.MERCURY, 250)
            itemResult = resultCreator.create(Items.GLASS_BOTTLE)
        }

        HTTankInteractionRecipeBuilder.filling(output) {
            itemIngredient = itemCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(RagiumFluids.MERCURY, 250)
            itemResult = resultCreator.create(RagiumItems.MERCURY_BOTTLE)
        }
    }
}
