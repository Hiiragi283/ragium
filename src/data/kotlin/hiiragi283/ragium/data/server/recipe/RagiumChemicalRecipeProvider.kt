package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.fraction
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
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
            itemIngredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND)
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
        }
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.DIRT)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(Items.MUD)
        }
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.SPONGE)
            fluidIngredient = inputCreator.water(1000)
            result = resultCreator.create(Items.WET_SPONGE)
        }

        // Sawdust -> Paper
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
            fluidIngredient = inputCreator.water(125)
            result = resultCreator.create(Items.PAPER)
        }
        // Bottle -> Water Bottle
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(HTPotionHelper.createPotion(Items.POTION, Potions.WATER))
            recipeId replace id("water_bottle")
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
            itemIngredient = inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            result = resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }

        // Creosote + Redstone -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result = resultCreator.create(RagiumFluids.LUBRICANT, 500)
            recipeId suffix "_from_creosote_with_redstone"
        }
        // Creosote + Raginite -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredient = inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result = resultCreator.create(RagiumFluids.LUBRICANT, 750)
            recipeId suffix "_from_creosote_with_raginite"
        }
    }

    //    Washing    //

    @JvmStatic
    fun washing() {
        // Gravel + Water -> Flint
        HTChancedRecipeBuilder.washing(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS) to inputCreator.water(250)
            result = resultCreator.create(Items.FLINT)
            chancedResults += resultCreator.create(Items.FLINT) to fraction(1, 3)
            time = 20 * 5
        }
    }

    //    Reacting    //

    @JvmStatic
    fun reacting() {
    }
}
