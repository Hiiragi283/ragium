package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumInfusingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        createInfusing()
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .addIngredient(Items.GOLDEN_APPLE)
            .setCost(15 * 4)
            .save(output)

        createInfusing()
            .itemOutput(Items.HEART_OF_THE_SEA)
            .addIngredient(RagiumItems.ELDER_HEART)
            .setCost(15 * 4)
            .save(output)

        createInfusing()
            .itemOutput(Items.REINFORCED_DEEPSLATE)
            .addIngredient(Items.DEEPSLATE)
            .setCost(15 * 4)
            .save(output)

        createInfusing()
            .itemOutput(Items.GILDED_BLACKSTONE)
            .addIngredient(Items.BLACKSTONE)
            .setCost(5)
            .save(output)

        createInfusing()
            .itemOutput(Items.BUDDING_AMETHYST)
            .addIngredient(Items.AMETHYST_BLOCK)
            .setCost(15)
            .save(output)

        createInfusing()
            .itemOutput(Items.CHORUS_FLOWER)
            .addIngredient(ItemTags.SMALL_FLOWERS)
            .setCost(15 * 2)
            .save(output)

        createInfusing()
            .itemOutput(Items.DRAGON_EGG)
            .addIngredient(Tags.Items.EGGS)
            .setCost(15 * 4)
            .save(output)

        createInfusing()
            .itemOutput(Items.HEAVY_CORE)
            .addIngredient(RagiumCommonTags.Items.STORAGE_BLOCKS_DEEP_STEEL)
            .setCost(15 * 2)
            .save(output)

        createInfusing()
            .itemOutput(Items.OMINOUS_TRIAL_KEY)
            .addIngredient(Items.TRIAL_KEY)
            .setCost(15 * 2)
            .save(output)

        createInfusing()
            .itemOutput(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .addIngredient(RagiumItems.ELDRITCH_ORB)
            .setCost(15 * 2)
            .save(output)
    }
}
