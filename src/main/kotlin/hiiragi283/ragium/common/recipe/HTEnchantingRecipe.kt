package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.item.enchantment.toInstances
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTViewRecipeInput
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

class HTEnchantingRecipe(
    val ingredient: HTItemIngredient,
    val contents: Either<Holder<Enchantment>, ItemEnchantments>,
    parameters: SubParameters,
) : HTProcessingRecipe<HTViewRecipeInput>(parameters) {
    constructor(
        ingredient: HTItemIngredient,
        holder: Holder<Enchantment>,
        parameters: SubParameters,
    ) : this(ingredient, Either.Left(holder), parameters)

    constructor(ingredient: HTItemIngredient, enchantments: ItemEnchantments, parameters: SubParameters) : this(
        ingredient,
        Either.Right(enchantments),
        parameters,
    )

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

    override fun matches(input: HTViewRecipeInput, level: Level): Boolean {
        val bool1: Boolean = expIngredient.test(input.getFluid(0))
        val bool2: Boolean = input.getItem(0).`is`(Items.BOOK)
        val bool3: Boolean = ingredient.test(input.getItem(1))
        return bool1 && bool2 && bool3
    }

    override fun assemble(input: HTViewRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var stack: ItemStack = input.getItem(0)
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
}
