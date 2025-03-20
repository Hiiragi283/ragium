package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFermentingRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Vanilla
        ferment().itemOutput(Items.MOSSY_COBBLESTONE).itemInput(Tags.Items.COBBLESTONES_NORMAL).save(output)
        ferment().itemOutput(Items.MOSSY_STONE_BRICKS).itemInput(Items.STONE_BRICKS).save(output)

        ferment().itemOutput(Items.MYCELIUM).itemInput(Items.GRASS_BLOCK).save(output)

        ferment().itemOutput(Items.MUD).itemInput(Items.DIRT).save(output)

        ferment().itemOutput(Items.MOSS_BLOCK, 4).itemInput(Items.MOSS_BLOCK).saveSuffixed(output, "_from_block")
        ferment().itemOutput(Items.MOSS_BLOCK).itemInput(Items.MOSS_CARPET).saveSuffixed(output, "_from_carpet")

        ferment().itemOutput(Items.GLOWSTONE_DUST).itemInput(Items.GLOW_BERRIES).save(output)

        ferment().itemOutput(Items.FERMENTED_SPIDER_EYE).itemInput(Items.SPIDER_EYE).save(output)
    }
}
