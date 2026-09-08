package hiiragi283.ragium.client.integration.jei

import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.HTJeiRecipeType
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import hiiragi283.ragium.api.recipe.RTRefiningRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

data object RagiumJeiRecipeTypes {
    // Mechanical
    @JvmField
    val ASSEMBLING: HTHolderJeiRecipeType<HTDoubleItemToItemRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.ASSEMBLING, RagiumBlocks.ASSEMBLER)

    @JvmField
    val COMPRESSING: HTHolderJeiRecipeType<HTItemToRecipe.BasicItem> =
        HTJeiRecipeType(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.CRUSHING, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val DRAINING: HTHolderJeiRecipeType<HTItemToItemAndFluidRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.DRAINING, ItemStack(Items.GLASS_BOTTLE))

    @JvmField
    val FILLING: HTHolderJeiRecipeType<HTItemAndFluidToRecipe.BasicItem> =
        HTJeiRecipeType(RagiumRecipeTypes.FILLING, ItemStack(Items.GLASS_BOTTLE))

    // Heat
    @JvmField
    val ALLOYING: HTHolderJeiRecipeType<HTDoubleItemToItemRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.ALLOYING, ItemStack(Items.FURNACE))

    @JvmField
    val FREEZING: HTHolderJeiRecipeType<HTFluidToRecipe.BasicItem> =
        HTJeiRecipeType(RagiumRecipeTypes.FREEZING, RagiumBlocks.FREEZER)

    @JvmField
    val MELTING: HTHolderJeiRecipeType<HTItemToRecipe.BasicFluid> =
        HTJeiRecipeType(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val PYROLYZING: HTHolderJeiRecipeType<HTItemToItemAndFluidRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.PYROLYZING, RagiumItems.COAL_COKE)

    @JvmField
    val REFINING: HTHolderJeiRecipeType<RTRefiningRecipe> = HTJeiRecipeType(RagiumRecipeTypes.REFINING, RagiumItems.TAR)

    // Chemical
    @JvmField
    val BATHING: HTHolderJeiRecipeType<HTItemAndFluidToRecipe.BasicItem> =
        HTJeiRecipeType(RagiumRecipeTypes.BATHING, RagiumBlocks.CHEMICAL_BATH)

    @JvmField
    val ELECTROLYZING: HTHolderJeiRecipeType<RTElectrolyzingRecipe> =
        HTJeiRecipeType(RagiumRecipeTypes.ELECTROLYZING, ItemStack(Items.LIGHTNING_ROD))

    @JvmField
    val MIXING: HTHolderJeiRecipeType<HTItemAndFluidToRecipe.BasicFluid> =
        HTJeiRecipeType(RagiumRecipeTypes.MIXING, ItemStack(Items.BREEZE_ROD))

    @JvmField
    val REACTING: HTHolderJeiRecipeType<RTReactingRecipe> =
        HTJeiRecipeType(RagiumRecipeTypes.REACTING, ItemStack(Items.GOLDEN_APPLE))

    // Bio
    @JvmField
    val BREWING: HTHolderJeiRecipeType<HTItemAndFluidToRecipe.BasicFluid> =
        HTJeiRecipeType(RagiumRecipeTypes.BREWING, RagiumBlocks.BREWERY)

    @JvmField
    val PLANTING: HTHolderJeiRecipeType<HTItemToDoubleItemRecipe.Basic> =
        HTJeiRecipeType(RagiumRecipeTypes.PLANTING, ItemStack(Items.FLOWER_POT))

    // Electronics

    // Arcane
}
