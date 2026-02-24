package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.common.material.ColoredMaterials
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BannerPatternLayers

/**
 * @see net.minecraft.world.item.crafting.BannerDuplicateRecipe
 */
data class HTBannerCopyingRecipe(val color: HTDefaultColor) : HTItemAndItemRecipe.Serializable {
    val banner: HTSimpleDeferredItem = ColoredMaterials.BANNER[color]!!

    fun hasNonEmptyPattern(stack: ItemStack): Boolean = stack
        .getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
        .layers()
        .isNotEmpty()

    override fun testFirstItem(stack: ItemStack): Boolean = stack.`is`(banner)

    override fun testSecondItem(stack: ItemStack): Boolean = testFirstItem(stack) && hasNonEmptyPattern(stack)

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack {
        val parentStack: ItemStack = input.second.copy()
        if (!hasNonEmptyPattern(parentStack)) return input.first.copyWithCount(1)
        return parentStack
    }

    override fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int> = 1 to 0

    override val time: Int = 100

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BANNER_COPYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PRINTING.get()
}
