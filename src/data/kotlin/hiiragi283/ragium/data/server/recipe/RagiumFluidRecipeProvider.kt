package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.blueprint
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.recipe.special.HTBucketDrainingRecipe
import hiiragi283.ragium.common.recipe.special.HTBucketFillingRecipe
import hiiragi283.ragium.common.recipe.special.HTPotionDrainingRecipe
import hiiragi283.ragium.common.recipe.special.HTPotionFillingRecipe
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        canning()
        mixing()
        refining()
        washing()
    }

    //    Canning    //

    @JvmStatic
    private fun canning() {
        save(id(RagiumConst.CANNING, "bucket_draining"), HTBucketDrainingRecipe)
        save(id(RagiumConst.CANNING, "bucket_filling"), HTBucketFillingRecipe)

        save(id(RagiumConst.CANNING, "potion_draining"), HTPotionDrainingRecipe)
        save(id(RagiumConst.CANNING, "potion_filling"), HTPotionFillingRecipe)

        // Water Bottle
        HTItemOrFluidRecipeBuilder.canning(output) {
            ingredient += inputCreator.create(Items.GLASS_BOTTLE)
            ingredient += inputCreator.water(250)
            result += resultCreator.create(HTPotionHelper.createPotion(Items.POTION, Potions.WATER))
            time /= 4
            recipeId replace id("water_bottle")
        }
        // Experience
        fillAndEmpty(
            Items.GLASS_BOTTLE.toLike(),
            Items.EXPERIENCE_BOTTLE.toLike(),
            HCFluids.EXPERIENCE,
            250,
        )
        // Honey Bottle
        fillAndEmpty(
            Items.GLASS_BOTTLE.toLike(),
            Items.HONEY_BOTTLE.toLike(),
            HCFluids.HONEY,
            250,
        )
        // Mushroom Stew
        fillAndEmpty(
            Items.BOWL.toLike(),
            Items.MUSHROOM_STEW.toLike(),
            HCFluids.MUSHROOM_STEW,
            250,
        )
        // Dragon Breath
        fillAndEmpty(
            Items.GLASS_BOTTLE.toLike(),
            Items.DRAGON_BREATH.toLike(),
            HCFluids.DRAGON_BREATH,
            250,
        )
    }

    @JvmStatic
    private fun fillAndEmpty(
        empty: HTItemHolderLike<*>,
        filled: HTItemHolderLike<*>,
        fluid: HTFluidContent,
        amount: Int,
    ) {
        // Empty -> Filled
        HTItemOrFluidRecipeBuilder.canning(output) {
            ingredient += inputCreator.create(empty)
            ingredient += inputCreator.create(fluid, amount)
            result += resultCreator.create(filled)
            recipeId suffix "_from_${empty.path}"
        }
        // Filled -> Empty
        HTItemOrFluidRecipeBuilder.canning(output) {
            ingredient += inputCreator.create(filled)
            result += resultCreator.create(empty)
            result += resultCreator.create(fluid, amount)
            recipeId suffix "_from_${filled.path}"
        }
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
        // Latex + Sulfur + Carbon -> Rubber
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(HCItems.RAW_RUBBER)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON)

            itemResults += resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.RUBBER, 4)
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

    //    Washing    //

    @JvmStatic
    fun washing() {
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
