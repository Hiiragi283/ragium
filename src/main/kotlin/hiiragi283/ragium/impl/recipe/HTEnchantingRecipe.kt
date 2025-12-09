package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.item.createEnchantedBook
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.impl.recipe.base.HTBasicCombineRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.fluids.FluidStack

class HTEnchantingRecipe(itemIngredients: Pair<HTItemIngredient, HTItemIngredient>, val holder: Holder<Enchantment>) :
    HTBasicCombineRecipe(itemIngredients) {
    fun getRequiredExpFluid(): Int {
        val enchantment: Enchantment = holder.value()
        return HTExperienceHelper.fluidAmountFromExp(enchantment.getMaxCost(enchantment.maxLevel))
    }

    override fun testFluid(input: HTMultiRecipeInput): Boolean {
        val stack: FluidStack = input.getFluid(0)
        return RagiumFluidContents.EXPERIENCE.isOf(stack) && stack.amount >= getRequiredExpFluid()
    }

    override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = when (test(input)) {
        true -> createEnchantedBook(holder)
        false -> null
    }?.toImmutable()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()

    override fun isIncomplete(): Boolean {
        val (left: HTItemIngredient, right: HTItemIngredient) = itemIngredients
        val bool1: Boolean = left.hasNoMatchingStacks()
        val bool2: Boolean = right.hasNoMatchingStacks()
        val bool3: Boolean = !holder.isBound
        return bool1 || bool2 || bool3
    }

    override fun getRequiredAmount(input: HTMultiRecipeInput, stack: ImmutableFluidStack): Int = getRequiredExpFluid()
}
