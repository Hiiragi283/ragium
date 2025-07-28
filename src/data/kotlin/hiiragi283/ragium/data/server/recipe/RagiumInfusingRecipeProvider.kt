package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTInfusingRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumInfusingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        HTInfusingRecipeBuilder()
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .addIngredient(Items.GOLDEN_APPLE)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.HEART_OF_THE_SEA)
            .addIngredient(RagiumItems.ELDER_HEART)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.REINFORCED_DEEPSLATE)
            .addIngredient(Items.DEEPSLATE)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.GILDED_BLACKSTONE)
            .addIngredient(Items.BLACKSTONE)
            .setCost(5)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.BUDDING_AMETHYST)
            .addIngredient(Items.AMETHYST_BLOCK)
            .setCost(15)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.CHORUS_FLOWER)
            .addIngredient(ItemTags.SMALL_FLOWERS)
            .setCost(15 * 2)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.DRAGON_EGG)
            .addIngredient(Tags.Items.EGGS)
            .setCost(15 * 4)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.HEAVY_CORE)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_DEEP_STEEL)
            .setCost(15 * 2)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(Items.OMINOUS_TRIAL_KEY)
            .addIngredient(Items.TRIAL_KEY)
            .setCost(15 * 2)
            .save(output)

        HTInfusingRecipeBuilder()
            .itemOutput(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .addIngredient(RagiumItems.ELDRITCH_ORB)
            .setCost(15 * 2)
            .save(output)
    }
}
