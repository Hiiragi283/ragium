package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTTankInteractingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.blueprint
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.recipe.special.HTPotionBottleInteractionRecipe
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        mixing()
        refining()
        tankInteraction()
        washing()
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Eldritch Flux
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            result += resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }
    }

    //    Refining    //

    @JvmStatic
    private fun refining() {
        // Diamond + Raginite -> Ragi-Crystal
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND)
            ingredient += inputCreator.molten(RagiumMaterialKeys.RAGINITE) { it * 6 }
            result += resultCreator.material(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
        // Liquid Dyes
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYE) {
            // Dye + Water -> Liquid Dye
            HTItemOrFluidRecipeBuilder.refining(output) {
                ingredient += inputCreator.create(color.dyesTag)
                ingredient += inputCreator.water(250)
                result += resultCreator.create(content, 250)
            }
            // Liquid Dye -> Dye
            val dye: HTSimpleItemHolderLike = ColoredMaterials.DYE[color] ?: continue
            HTFreezingRecipeBuilder.create(output) {
                itemIngredient = inputCreator.blueprint(0)
                fluidIngredient = inputCreator.create(content, 250)
                result = resultCreator.create(dye)
            }
        }

        waterRefining()
        expRefining()
        eldritchRefining()
    }

    @JvmStatic
    private fun waterRefining() {
        // Cobblestone -> Mossy
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Tags.Items.COBBLESTONES_NORMAL)
            ingredient += inputCreator.water(250)
            result += resultCreator.create(Items.MOSSY_COBBLESTONE)
            time /= 2
        }
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Items.DIRT)
            ingredient += inputCreator.water(250)
            result += resultCreator.create(Items.MUD)
            time /= 2
        }
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Items.SPONGE)
            ingredient += inputCreator.water()
            result += resultCreator.create(Items.WET_SPONGE)
            time /= 2
        }

        // Sawdust -> Paper
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
            ingredient += inputCreator.water(125)
            result += resultCreator.create(Items.PAPER)
            time /= 2
        }
    }

    @JvmStatic
    private fun expRefining() {
        // Quartz Block -> Ghast Tear
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ)
            ingredient += inputCreator.create(HCFluids.EXPERIENCE, 500)
            result += resultCreator.create(Items.GHAST_TEAR)
            recipeId suffix "_from_quartz"
        }
        // Sulfur Dust -> Blaze Powder
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            ingredient += inputCreator.create(HCFluids.EXPERIENCE, 250)
            result += resultCreator.create(Items.BLAZE_POWDER)
            recipeId suffix "_from_sulfur"
        }
        // Leather -> Phantom Membrane
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Tags.Items.LEATHERS)
            ingredient += inputCreator.create(HCFluids.EXPERIENCE, 250)
            result += resultCreator.create(Items.PHANTOM_MEMBRANE)
            recipeId suffix "_from_leather"
        }
        // Snowball -> Wind Charge
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Items.SNOWBALL)
            ingredient += inputCreator.create(HCFluids.EXPERIENCE, 250)
            result += resultCreator.create(Items.WIND_CHARGE)
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
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            ingredient += eldritch(1)
            result += resultCreator.create(Items.CRYING_OBSIDIAN)
        }
        // Amethyst Block -> Budding Amethyst
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST)
            ingredient += eldritch(9)
            result += resultCreator.create(Items.BUDDING_AMETHYST)
        }
        // Skeleton Skull -> Wither Skeleton Skull
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Items.SKELETON_SKULL)
            ingredient += eldritch(1)
            result += resultCreator.create(Items.WITHER_SKELETON_SKULL)
        }

        // Trial Key -> Ominous Key
        HTItemOrFluidRecipeBuilder.refining(output) {
            ingredient += inputCreator.create(Items.TRIAL_KEY)
            ingredient += eldritch(4)
            result += resultCreator.create(Items.OMINOUS_TRIAL_KEY)
        }
    }

    //    Tank Interaction    //

    @JvmStatic
    private fun tankInteraction() {
        val glassBottle: HTItemHolderLike<Item> = Items.GLASS_BOTTLE.toLike()
        // Experience
        tankInteraction(glassBottle, Items.EXPERIENCE_BOTTLE.toLike(), HCFluids.EXPERIENCE)
        // Honey Bottle
        tankInteraction(glassBottle, Items.HONEY_BOTTLE.toLike(), HCFluids.HONEY)
        // Mushroom Stew
        tankInteraction(Items.BOWL.toLike(), Items.MUSHROOM_STEW.toLike(), HCFluids.MUSHROOM_STEW)
        // Dragon Breath
        tankInteraction(glassBottle, Items.DRAGON_BREATH.toLike(), HCFluids.DRAGON_BREATH)
        // Potion Bottle
        save(id(RagiumConst.TANK_INTERACTION, "potion_bottle"), HTPotionBottleInteractionRecipe)
        // Mercury
        tankInteraction(glassBottle, RagiumItems.MERCURY_BOTTLE, RagiumFluids.MERCURY)

        // Sponge
        tankInteraction(Items.SPONGE.toLike(), Items.WET_SPONGE.toLike(), VanillaFluidContents.WATER, HTConst.DEFAULT_FLUID_AMOUNT)
    }

    @JvmStatic
    private fun tankInteraction(
        empty: HTSimpleItemHolderLike,
        filled: HTSimpleItemHolderLike,
        content: HTFluidContent,
        amount: Int = 250,
    ) {
        HTTankInteractingRecipeBuilder.create(output) {
            this.emptyContainer = empty
            this.filledContainer = filled
            this.fluid = content
            this.amount = amount
            this.fluidTag = content.fluidTag
        }
    }

    //    Washing    //

    @JvmStatic
    private fun washing() {
        // Gravel + Water -> Flint
        HTWashingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(Tags.Items.GRAVELS)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(Items.FLINT)
            extraResult += resultCreator.create(Items.FLINT) to fraction(1, 3)
            time = 20 * 5
        }

        // Ash + Water -> Carbon
        HTWashingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH, 4)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.material(CommonParts.DUST, CommonMaterialKeys.CARBON, 3)
            time = 20 * 5
        }
    }
}
