package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.HTJeiRecipeType
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.item.RagiumItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

data object RagiumJeiRecipeTypes {
    // Mechanical
    @JvmField
    val ASSEMBLING: HTHolderJeiRecipeType<HTDoubleItemToItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.ASSEMBLING, RagiumItems.COAL_COKE)

    @JvmField
    val CRUSHING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.CRUSHING, RagiumItems.COAL_COKE)

    // Heat
    @JvmField
    val FREEZING: HTHolderJeiRecipeType<HTItemAndFluidToItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.FREEZING, RagiumItems.COAL_COKE)

    @JvmField
    val MELTING: HTHolderJeiRecipeType<HTItemToFluidRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.MELTING, RagiumItems.COAL_COKE)

    // Chemical
    @JvmField
    val ELECTROLYZING: HTHolderJeiRecipeType<RTElectrolyzingRecipe> = HTJeiRecipeType(RagiumRecipeTypes.ELECTROLYZING, RagiumItems.COAL_COKE)

    // Bio
    @JvmField
    val BREWING: HTHolderJeiRecipeType<HTItemOrFluidRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.BREWING, ItemStack(Items.BREWING_STAND))

    // Electronics

    // Arcane
}
