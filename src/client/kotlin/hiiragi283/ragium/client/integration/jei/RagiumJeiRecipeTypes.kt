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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

data object RagiumJeiRecipeTypes {
    // Mechanical
    @JvmField
    val ASSEMBLING: HTHolderJeiRecipeType<HTDoubleItemToItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.ASSEMBLING, ItemStack(Items.CRAFTER))

    @JvmField
    val CRUSHING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.CRUSHING, ItemStack(Items.FLINT))

    @JvmField
    val CUTTING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.CUTTING, ItemStack(Items.IRON_AXE))

    // Heat
    @JvmField
    val FREEZING: HTHolderJeiRecipeType<HTItemAndFluidToItemRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.FREEZING, ItemStack(Items.ICE))

    @JvmField
    val MELTING: HTHolderJeiRecipeType<HTItemToFluidRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.MELTING, ItemStack(Items.MAGMA_BLOCK))

    // Chemical
    @JvmField
    val ELECTROLYZING: HTHolderJeiRecipeType<RTElectrolyzingRecipe> = HTJeiRecipeType(RagiumRecipeTypes.ELECTROLYZING, ItemStack(Items.LIGHTNING_ROD))

    // Bio
    @JvmField
    val BREWING: HTHolderJeiRecipeType<HTItemOrFluidRecipe.Basic> = HTJeiRecipeType(RagiumRecipeTypes.BREWING, ItemStack(Items.BREWING_STAND))

    // Electronics

    // Arcane
}
