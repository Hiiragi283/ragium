package hiiragi283.ragium.data.server.integration

import com.enderio.base.common.init.EIOBlocks
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTEnderIORecipeProvider : HTRecipeProvider.Modded(IntegrationMods.EIO_BASE) {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Enderman Skull
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(Tags.Items.ENDER_PEARLS, 4)
            .itemOutput(EIOBlocks.ENDERMAN_HEAD)
            .save(output)
    }
}
