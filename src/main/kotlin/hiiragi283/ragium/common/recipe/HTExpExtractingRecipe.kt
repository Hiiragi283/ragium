package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.EnchantmentHelper

data object HTExpExtractingRecipe : HTItemWithCatalystRecipe {
    override fun assembleFluid(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? = EnchantmentHelper
        .getEnchantmentsForCrafting(input.getItem(0))
        .let(HTExperienceHelper::getTotalMinCost)
        .let(HTExperienceHelper::fluidAmountFromExp)
        .let(RagiumFluidContents.EXPERIENCE::toImmutableStack)

    override fun test(input: HTMultiRecipeInput): Boolean =
        EnchantmentHelper.canStoreEnchantments(input.getItem(0)) && input.getItem(1).`is`(Items.GRINDSTONE)

    /**
     * @see net.minecraft.world.inventory.GrindstoneMenu
     */
    override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? {
        val tool: ItemStack = input.getItem(0).copyWithCount(1)
        return when {
            tool.`is`(Items.ENCHANTED_BOOK) -> ImmutableItemStack.of(Items.BOOK)
            else -> tool.toImmutable()?.minus(EnchantmentHelper.getComponentType(tool))
        }
    }

    override fun isIncomplete(): Boolean = false

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXP_EXTRACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()

    override fun getRequiredCount(stack: ImmutableItemStack): Int = 1
}
