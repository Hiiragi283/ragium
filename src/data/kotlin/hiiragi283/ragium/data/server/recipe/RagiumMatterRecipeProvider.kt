package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.data.recipe.HTDuplicatingRecipeBuilder
import hiiragi283.ragium.common.recipe.special.HTEnchantedBookDuplicatingRecipe
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags

object RagiumMatterRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Wood
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.LOGS)
            requiredMatter = 4
        }
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.PLANKS)
            requiredMatter = 1
        }
        // Stone
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.STONES)
            requiredMatter = 1
        }
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.COBBLESTONES)
            requiredMatter = 1
        }
        // Dirt
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.DIRT)
            requiredMatter = 1
        }
        // Gravel
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.GRAVELS)
            requiredMatter = 2
        }
        // Sand
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.SANDS)
            requiredMatter = 1
        }
        // Obsidian
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.OBSIDIANS_CRYING)
            requiredMatter = 64 * 8
        }
        HTDuplicatingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.OBSIDIANS_NORMAL)
            requiredMatter = 64
        }

        // Enchanted Book
        save(id(RagiumConst.DUPLICATING, "enchanted_book"), HTEnchantedBookDuplicatingRecipe)
        // Potion
    }
}
