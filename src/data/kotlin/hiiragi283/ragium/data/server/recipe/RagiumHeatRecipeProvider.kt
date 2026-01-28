package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumHeatRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Water
        HTSingleRecipeBuilder.melting(output) {
            ingredient = inputCreator.create(Items.SNOW_BLOCK)
            result = resultCreator.water(1000)
            time = 20 * 5
            recipeId suffix "_from_snow_block"
        }

        meltAndSolidify(
            inputCreator.create(Items.SNOWBALL),
            resultCreator.create(Items.SNOWBALL),
            VanillaFluidContents.WATER,
            250,
            HTMoldType.BALL,
            "snowball",
            20,
        )
        meltAndSolidify(
            inputCreator.create(Items.ICE),
            resultCreator.create(Items.ICE),
            VanillaFluidContents.WATER,
            1000,
            HTMoldType.BLOCK,
            "ice",
        )
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
        meltAndSolidify(
            inputCreator.create(Tags.Items.GLASS_PANES),
            resultCreator.create(Items.GLASS_PANE),
            HCFluids.MOLTEN_GLASS,
            375,
            HTMoldType.PLATE,
            "pane",
        )
    }

    @JvmStatic
    private fun meltAndSolidify(
        input: HTItemIngredient,
        result: HTItemResult,
        fluid: HTFluidContent<*, *, *>,
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
