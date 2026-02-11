package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.item.enchantment.toInstances
import hiiragi283.core.api.monad.Either
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
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTEnchantingRecipe(
    val book: HTItemIngredient,
    val ingredient: HTItemIngredient,
    val contents: Either<Holder<Enchantment>, ItemEnchantments>,
    parameters: SubParameters,
) : HTProcessingRecipe<HTEnchantingRecipe.Input>(parameters) {
    val expIngredient: HTFluidIngredient by lazy {
        val amount: Int = contents
            .map(
                { holder: Holder<Enchantment> ->
                    val enchantment: Enchantment = holder.value()
                    enchantment.getMaxCost(enchantment.maxLevel)
                },
                HTExperienceHelper::getTotalMaxCost,
            ).let(HTExperienceHelper::fluidAmountFromExp)
        HTIngredientCreator.create(HCFluids.EXPERIENCE, amount)
    }

    fun createEnchBook(): ItemStack = contents.map(::createEnchantedBook, ::createEnchantedBook)

    //    HTProcessingRecipe    //

    override fun matches(input: Input, level: Level): Boolean {
        val bool1: Boolean = expIngredient.test(input.fluid)
        val bool2: Boolean = book.testOnlyType(input.book)
        val bool3: Boolean = ingredient.test(input.item)
        return bool1 && bool2 && bool3
    }

    override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack {
        var stack: ItemStack = input.book
        val instances: List<EnchantmentInstance> = contents
            .map(
                { holder: Holder<Enchantment> ->
                    listOf(EnchantmentInstance(holder, holder.value().maxLevel))
                },
                { enchantments: ItemEnchantments ->
                    enchantments.toInstances()
                },
            )
        stack = stack.item.applyEnchantments(stack, instances)
        return stack
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = createEnchBook()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()

    @JvmRecord
    data class Input(val book: ItemStack, val item: ItemStack, val fluid: FluidStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = fluid

        override fun getFluidSize(): Int = 0

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> book
            1 -> item
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 2
    }
}
