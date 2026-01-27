package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.monad.Either
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTDryingRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumDryingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Mud -> Clay + Water
        HTDryingRecipeBuilder.create(output) {
            ingredient = Either.Left(inputCreator.create(Items.MUD))
            result += resultCreator.create(Items.CLAY)
            result += resultCreator.water(250)
            recipeId suffix "_from_mud"
        }
        // Crying Obsidian -> Obsidian + ???
        HTDryingRecipeBuilder.create(output) {
            ingredient = Either.Left(inputCreator.create(Tags.Items.OBSIDIANS_CRYING))
            result += resultCreator.create(Items.OBSIDIAN)
            result += resultCreator.molten(HCMaterialKeys.ELDRITCH)
            time *= 2
            recipeId suffix "_from_crying"
        }
        // Sapling -> Dead Bush
        HTDryingRecipeBuilder.create(output) {
            ingredient = Either.Left(inputCreator.create(ItemTags.SAPLINGS))
            result += resultCreator.create(Items.DEAD_BUSH)
            result += resultCreator.water(125)
            recipeId suffix "_from_sapling"
        }
        // Wet Sponge -> Sponge + Water
        HTDryingRecipeBuilder.create(output) {
            ingredient = Either.Left(inputCreator.create(Items.WET_SPONGE))
            result += resultCreator.create(Items.SPONGE)
            result += resultCreator.water()
            recipeId suffix "_from_sponge"
        }
        // Kelp -> Dried Kelp + Salt Water
        HTDryingRecipeBuilder.create(output) {
            ingredient = Either.Left(inputCreator.create(Items.KELP))
            result += resultCreator.create(Items.DRIED_KELP)
        }

        // Slime -> Raw Rubber
        HTDryingRecipeBuilder.create(output) {
            ingredient = Either.Left(inputCreator.create(Items.SLIME_BALL))
            result += resultCreator.create(HCItems.RAW_RUBBER)
            recipeId suffix "_from_slime"
        }
    }
}
