package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.item.enchantment.buildEnchantments
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import org.apache.commons.lang3.math.Fraction

class HTEnchantingRecipe(
    val ingredient: HTItemIngredient,
    val enchantments: ItemEnchantments,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTEnchantingRecipe.Input>(time, exp) {
    companion object {
        @JvmStatic
        fun createExpIngredient(enchantments: ItemEnchantments): HTFluidIngredient {
            val expCost: Int = HTExperienceHelper.getTotalMaxCost(enchantments)
            return SizedFluidIngredient
                .of(HCFluids.EXPERIENCE.fluidTag, HTExperienceHelper.fluidAmountFromExp(expCost))
                .let(::HTFluidIngredient)
        }
    }

    constructor(ingredient: HTItemIngredient, holder: Holder<Enchantment>, time: Int, exp: Fraction) : this(
        ingredient,
        buildEnchantments { set(holder, holder.value().maxLevel) },
        time,
        exp,
    )

    val instances: List<EnchantmentInstance> =
        enchantments.entrySet().map { (holder: Holder<Enchantment>, level: Int) -> EnchantmentInstance(holder, level) }
    val expIngredient: HTFluidIngredient by lazy { createExpIngredient(enchantments) }

    override fun matches(input: Input, level: Level): Boolean {
        val bool1: Boolean = expIngredient.test(input.fluid)
        val bool2: Boolean = input.left.`is`(Items.BOOK)
        val bool3: Boolean = ingredient.test(input.right)
        return bool1 && bool2 && bool3
    }

    override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack {
        var stack: ItemStack = input.left.copy()
        stack = stack.item.applyEnchantments(stack, instances)
        return stack
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = createEnchantedBook(enchantments)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()

    @JvmRecord
    data class Input(val fluid: FluidStack, val left: ItemStack, val right: ItemStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = fluid

        override fun getFluidSize(): Int = 1

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> left
            1 -> right
            else -> ItemStack.EMPTY
        }

        override fun size(): Int = 2
    }
}
