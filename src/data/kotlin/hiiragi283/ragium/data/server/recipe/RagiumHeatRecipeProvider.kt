package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumHeatRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        vanilla()
        hiiragi()
    }

    @JvmStatic
    private fun vanilla() {
        // Water
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK)
            result = resultCreator.water(1000)
            time = 20 * 5
            recipeId suffix "_from_snow_block"
        }

        meltAndSolidify(Items.SNOWBALL, VanillaFluidContents.WATER, 250, HTMoldType.BALL, "snowball", 20)
        meltAndSolidify(Items.ICE, VanillaFluidContents.WATER, 1000, HTMoldType.BLOCK, "ice")

        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.water(250)
            result = resultCreator.create(HTPotionHelper.createPotion(Items.POTION, Potions.WATER))
            time /= 4
            recipeId replace id("water_bottle")
        }
        // Lava
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES))
            result = resultCreator.lava(125)
            time = 20 * 30
            recipeId suffix "_from_stones"
        }
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(Tags.Items.NETHERRACKS)
            result = resultCreator.lava(125)
            recipeId suffix "_from_netherrack"
        }
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(Items.MAGMA_BLOCK)
            result = resultCreator.lava(250)
            recipeId suffix "_from_magma"
        }

        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.lava(1000)
            itemIngredient = inputCreator.create(HTMoldType.BLOCK)
            result = resultCreator.create(Items.OBSIDIAN)
        }
    }

    @JvmStatic
    private fun hiiragi() {
        // Experience
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(HCFluids.EXPERIENCE, 250)
            result = resultCreator.create(Items.EXPERIENCE_BOTTLE)
        }
        // Honey
        meltAndSolidify(Items.HONEY_BLOCK, HCFluids.HONEY, 1000, HTMoldType.BLOCK, "block")

        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(HCFluids.HONEY, 250)
            result = resultCreator.create(Items.HONEY_BOTTLE)
        }
        // Mushroom Stew
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.BOWL)
            fluidIngredient = inputCreator.create(HCFluids.MUSHROOM_STEW, 250)
            result = resultCreator.create(Items.MUSHROOM_STEW)
        }
        // Dragon Breath
        HTFluidWithItemRecipeBuilder.bathing(output) {
            itemIngredient = inputCreator.create(Items.GLASS_BOTTLE)
            fluidIngredient = inputCreator.create(HCFluids.DRAGON_BREATH, 250)
            result = resultCreator.create(Items.DRAGON_BREATH)
        }

        // Latex
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.create(HCFluids.LATEX, 1000)
            itemIngredient = inputCreator.create(HTMoldType.BALL)
            result = resultCreator.create(HCItems.RAW_RUBBER, 2)
        }
        // Meat
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(Items.ROTTEN_FLESH)
            result = resultCreator.molten(RagiumMaterialKeys.MEAT)
            recipeId suffix "_from_rotten"
        }
        // Glass
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.create(HCFluids.MOLTEN_GLASS, 250)
            itemIngredient = inputCreator.create(HTMoldType.BALL)
            result = resultCreator.create(Items.GLASS_BOTTLE)
        }

        meltAndSolidify(
            inputCreator.create(Tags.Items.GLASS_PANES),
            resultCreator.create(Items.GLASS_PANE),
            HCFluids.MOLTEN_GLASS,
            375,
            HTMoldType.PLATE,
            "pane",
        )
        // Eldritch
        for (i: Int in (0..4)) {
            HTSingleRecipeBuilder.melting(output) {
                ingredient = inputCreator.create(
                    false,
                    Items.OMINOUS_BOTTLE,
                ) { expect(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, i) }
                result = resultCreator.molten(HCMaterialKeys.ELDRITCH) { it * (i + 1) }
                recipeId suffix "/$i"
            }
        }
    }

    //    Extensions    //

    @JvmStatic
    private fun meltAndSolidify(
        item: ItemLike,
        fluid: HTFluidContent,
        amount: Int,
        mold: HTMoldType,
        suffix: String,
        time: Int = 20 * 10,
    ) {
        meltAndSolidify(
            inputCreator.create(item),
            resultCreator.create(item),
            fluid,
            amount,
            mold,
            suffix,
            time,
        )
    }

    @JvmStatic
    private fun meltAndSolidify(
        input: HTItemIngredient,
        result: HTItemResult,
        fluid: HTFluidContent,
        amount: Int,
        mold: HTMoldType,
        suffix: String,
        time: Int = 20 * 10,
    ) {
        // Melting
        HTSingleRecipeBuilder.melting(output) {
            this.ingredient = input
            this.result = resultCreator.create(fluid, amount)
            this.time = time
            recipeId suffix "_from_$suffix"
        }
        // Solidify
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            this.fluidIngredient = inputCreator.create(fluid, amount)
            this.itemIngredient = inputCreator.create(mold)
            this.result = result
            this.time = time
        }
    }
}
