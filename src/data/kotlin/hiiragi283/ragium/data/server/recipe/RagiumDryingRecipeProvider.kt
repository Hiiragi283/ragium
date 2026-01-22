package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumDryingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Mud -> Clay + Water
        HTComplexRecipeBuilder
            .drying(inputCreator.create(Items.MUD))
            .setResult(itemResult.create(Items.CLAY))
            .setResult(fluidResult.water(250))
            .saveSuffixed(output, "_from_mud")
        // Crying Obsidian -> Obsidian + ???
        HTComplexRecipeBuilder
            .drying(inputCreator.create(Tags.Items.OBSIDIANS_CRYING))
            .setResult(itemResult.create(Items.OBSIDIAN))
            .setResult(createFluidResult(HCMaterialKeys.ELDRITCH, HTMaterialPropertyKeys.MOLTEN_FLUID))
            .setTime(20 * 20)
            .saveSuffixed(output, "_from_crying")
        // Sapling -> Dead Bush
        HTComplexRecipeBuilder
            .drying(inputCreator.create(ItemTags.SAPLINGS))
            .setResult(itemResult.create(Items.DEAD_BUSH))
            .setResult(fluidResult.water(125))
            .saveSuffixed(output, "_from_sapling")
        // Wet Sponge -> Sponge + Water
        HTComplexRecipeBuilder
            .drying(inputCreator.create(Items.WET_SPONGE))
            .setResult(itemResult.create(Items.SPONGE))
            .setResult(fluidResult.water(1000))
            .saveSuffixed(output, "_from_sponge")
        // Kelp -> Dried Kelp + Salt Water
        HTComplexRecipeBuilder
            .drying(inputCreator.create(Items.KELP))
            .setResult(itemResult.create(Items.DRIED_KELP))
            .saveSuffixed(output, "_from_kelp")

        // Slime -> Raw Rubber
        HTComplexRecipeBuilder
            .drying(inputCreator.create(Items.SLIME_BALL))
            .setResult(itemResult.create(HCItems.RAW_RUBBER))
            .saveSuffixed(output, "_from_slime")
    }
}
