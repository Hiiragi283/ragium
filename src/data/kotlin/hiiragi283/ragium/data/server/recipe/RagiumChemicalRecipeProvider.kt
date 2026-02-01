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
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

object RagiumChemicalRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        mixing()
        washing()

        reacting()
    }

    //    Mixing    //

    @JvmStatic
    private fun mixing() {
        // Diamond + Raginite -> Ragi-Crystal
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.DIAMOND)
            fluidIngredients += inputCreator.molten(RagiumMaterialKeys.RAGINITE) { it * 6 }
            result += resultCreator.material(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
        }

        // Eldritch Flux
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            result += resultCreator.molten(HCMaterialKeys.ELDRITCH)
        }

        // Creosote + Redstone -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result += resultCreator.create(RagiumFluids.LUBRICANT, 500)
            time /= 2
            recipeId suffix "_from_creosote_with_redstone"
        }
        // Creosote + Raginite -> Lubricant
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            fluidIngredients += inputCreator.create(RagiumFluids.CREOSOTE, 1000)
            result += resultCreator.create(RagiumFluids.LUBRICANT, 750)
            time /= 2
            recipeId suffix "_from_creosote_with_raginite"
        }

        waterMixing()

        eldritchMixing()
    }

    @JvmStatic
    private fun waterMixing() {
        // Cobblestone -> Mossy
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Tags.Items.COBBLESTONES_NORMAL)
            fluidIngredients += inputCreator.water(250)
            result += resultCreator.create(Items.MOSSY_COBBLESTONE)
            time /= 2
        }
        // XX Concrete Powder -> XX Concrete
        // Dirt + Water -> Mud
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.DIRT)
            fluidIngredients += inputCreator.water(250)
            result += resultCreator.create(Items.MUD)
            time /= 2
        }
        // XX Dead Coral -> XX Coral
        // Sponge -> Wet Sponge
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.SPONGE)
            fluidIngredients += inputCreator.water(1000)
            result += resultCreator.create(Items.WET_SPONGE)
            time /= 2
        }

        // Sawdust -> Paper
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
            fluidIngredients += inputCreator.water(125)
            result += resultCreator.create(Items.PAPER)
            time /= 2
        }
        // Bottle -> Water Bottle
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredients += inputCreator.water(250)
            result += resultCreator.create(HTPotionHelper.createPotion(Items.POTION, Potions.WATER))
            time /= 4
            recipeId replace id("water_bottle")
        }
    }

    @JvmStatic
    private fun eldritchMixing() {
        // Obsidian -> Crying Obsidian
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH)
            result += resultCreator.create(Items.CRYING_OBSIDIAN)
        }
        // Amethyst Block -> Budding Amethyst
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.AMETHYST)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 9 }
            result += resultCreator.create(Items.BUDDING_AMETHYST)
        }
        // Skeleton Skull -> Wither Skeleton Skull
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.SKELETON_SKULL)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH)
            result += resultCreator.create(Items.WITHER_SKELETON_SKULL)
        }

        // Trial Key -> Ominous Key
        HTMixingRecipeBuilder.create(output) {
            itemIngredients += inputCreator.create(Items.TRIAL_KEY)
            fluidIngredients += inputCreator.molten(HCMaterialKeys.ELDRITCH) { it * 4 }
            result += resultCreator.create(Items.OMINOUS_TRIAL_KEY)
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
