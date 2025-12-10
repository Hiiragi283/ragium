package hiiragi283.ragium.common.recipe.custom

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.item.component.filter
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.util.HTExperienceHelper
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments

object HTCopyEnchantingRecipe : HTCombineRecipe {
    @JvmField
    val RECIPE_ID: ResourceLocation = RagiumAPI.id(RagiumConst.ENCHANTING, "copy_from_book")

    override fun getLeftRequiredCount(): Int = 1

    override fun getRightRequiredCount(): Int = 0

    override fun getRequiredAmount(input: HTRecipeInput): Int {
        val tool: ImmutableItemStack = input.items[0]?.copyWithAmount(1) ?: return 0
        val book: ImmutableItemStack = input.items[1] ?: return 0
        return getFilteredEnchantments(tool, book)
            .let(HTExperienceHelper::getTotalMaxCost)
            .let(HTExperienceHelper::fluidAmountFromExp)
    }

    override fun test(left: ImmutableItemStack, right: ImmutableItemStack, fluid: ImmutableFluidStack): Boolean {
        val tool: ImmutableItemStack = left.copyWithAmount(1) ?: return false
        return !getFilteredEnchantments(tool, right).isEmpty
    }

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? {
        val tool: ImmutableItemStack = input.items[0]?.copyWithAmount(1) ?: return null
        val book: ImmutableItemStack = input.items[1] ?: return null
        return tool.plus(EnchantmentHelper.getComponentType(tool.unwrap()), getFilteredEnchantments(tool, book))
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COPY_ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()

    private fun getFilteredEnchantments(tool: ImmutableItemStack, book: ImmutableItemStack): ItemEnchantments = book
        .get(DataComponents.STORED_ENCHANTMENTS)
        ?.filter(tool.unwrap())
        ?: ItemEnchantments.EMPTY
}
