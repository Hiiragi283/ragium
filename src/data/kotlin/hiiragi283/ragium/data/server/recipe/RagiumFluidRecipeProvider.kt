package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTWashingRecipeBuilder
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
        bathing()
        canning()
        mixing()
        washing()
    }

    //    Bathing    //

    @JvmStatic
    private fun bathing() {
        // Diamond + Raginite -> Ragi-Crystal
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND)
            fluidIngredients += inputCreator.molten(RagiumMaterialKeys.RAGINITE) { it * 6 }
            itemResults += resultCreator.material(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }
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
            HTItemHolderLike.of(Items.GLASS_BOTTLE),
            HTItemHolderLike.of(Items.EXPERIENCE_BOTTLE),
            HCFluids.EXPERIENCE,
            250,
        )
        // Honey Bottle
        fillAndEmpty(
            HTItemHolderLike.of(Items.GLASS_BOTTLE),
            HTItemHolderLike.of(Items.HONEY_BOTTLE),
            HCFluids.HONEY,
            250,
        )
        // Mushroom Stew
        fillAndEmpty(
            HTItemHolderLike.of(Items.BOWL),
            HTItemHolderLike.of(Items.MUSHROOM_STEW),
            HCFluids.MUSHROOM_STEW,
            250,
        )
        // Dragon Breath
        fillAndEmpty(
            HTItemHolderLike.of(Items.GLASS_BOTTLE),
            HTItemHolderLike.of(Items.DRAGON_BREATH),
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
        // Liquid Dyes
        for ((color: HTDefaultColor, content: HTFluidContent) in HCFluids.DYE) {
            HTChemicalRecipeBuilder.mixing(output) {
                itemIngredients += inputCreator.create(color.dyesTag)
                fluidIngredients += inputCreator.water(250)
                fluidResults += resultCreator.create(content, 250)
            }
        }
        // Latex + Sulfur + Carbon -> Rubber
        HTChemicalRecipeBuilder.mixing(output) {
            itemIngredients += inputCreator.create(HCItems.RAW_RUBBER)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.CARBON)

            itemResults += resultCreator.material(CommonParts.INGOT, CommonMaterialKeys.RUBBER, 4)
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
            fluidIngredients += inputCreator.water()
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
            itemIngredients += inputCreator.create(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST)
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
