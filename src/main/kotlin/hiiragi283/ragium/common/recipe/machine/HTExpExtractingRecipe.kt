package hiiragi283.ragium.common.recipe.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level

data object HTExpExtractingRecipe : HTItemWithCatalystRecipe {
    override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? = input
        .item(0)
        ?.unwrap()
        ?.let(EnchantmentHelper::getEnchantmentsForCrafting)
        ?.let(HTExperienceHelper::getTotalMinCost)
        ?.let(HTExperienceHelper::fluidAmountFromExp)
        ?.let(RagiumFluidContents.EXPERIENCE::toImmutableStack)

    /**
     * @see net.minecraft.world.inventory.GrindstoneMenu
     */
    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? {
        val tool: ImmutableItemStack = input.item(0)?.copyWithAmount(1) ?: return null
        return when {
            tool.isOf(Items.ENCHANTED_BOOK) -> ImmutableItemStack.of(Items.BOOK)
            else -> tool.minus(EnchantmentHelper.getComponentType(tool.unwrap()))
        }
    }

    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testItem(0) { stack: ImmutableItemStack -> EnchantmentHelper.canStoreEnchantments(stack.unwrap()) }
        val bool2: Boolean = input.testItem(1) { stack: ImmutableItemStack -> stack.isOf(Items.GRINDSTONE) }
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXP_EXTRACTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()

    override fun getRequiredCount(stack: ImmutableItemStack): Int = 1
}
