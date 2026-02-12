package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFluidWithItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items

object RagiumCoolRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        freezing()
        solidifying()
    }

    @JvmStatic
    private fun freezing() {
        // Water -> Ice
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.water(1000)
            result += resultCreator.create(Items.ICE)
        }
        // Ice -> Packed Ice
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(Items.ICE, 6)
            result += resultCreator.create(Items.PACKED_ICE)
        }
        // Packed Ice -> Blue Ice
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(Items.PACKED_ICE, 6)
            result += resultCreator.create(Items.BLUE_ICE)
        }

        // Lava -> Obsidian
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.lava(1000)
            result += resultCreator.create(Items.OBSIDIAN)
        }

        // Honey -> Honey Block
        HTItemOrFluidRecipeBuilder.freezing(output) {
            ingredient += inputCreator.create(HCFluids.HONEY)
            result += resultCreator.create(Items.HONEY_BLOCK)
        }

        // X2 -> Liq X2
        mapOf(
            RagiumFluids.HYDROGEN to RagiumFluids.LIQUID_HYDROGEN,
            RagiumFluids.NITROGEN to RagiumFluids.LIQUID_NITROGEN,
            RagiumFluids.OXYGEN to RagiumFluids.LIQUID_OXYGEN,
        ).forEach { (gas: HTFluidContent, liquid: HTFluidContent) ->
            HTItemOrFluidRecipeBuilder.freezing(output) {
                ingredient += inputCreator.create(gas)
                result += resultCreator.create(liquid, 100)
            }
        }
    }

    @JvmStatic
    private fun solidifying() {
        // Latex -> Raw Rubber
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.create(HCFluids.LATEX, 1000)
            itemIngredient = inputCreator.create(HTMoldType.BALL)
            result = resultCreator.create(HCItems.RAW_RUBBER, 2)
        }

        // Glass -> Glass Bottle
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.molten(VanillaMaterialKeys.GLASS) { it / 4 }
            itemIngredient = inputCreator.create(HTMoldType.BALL)
            result = resultCreator.create(Items.GLASS_BOTTLE)
        }
        // Glass -> Pane
        HTFluidWithItemRecipeBuilder.solidifying(output) {
            fluidIngredient = inputCreator.molten(VanillaMaterialKeys.GLASS) { 375 }
            itemIngredient = inputCreator.create(HTMoldType.PLATE)
            result = resultCreator.create(Items.GLASS_PANE)
        }
    }
}
