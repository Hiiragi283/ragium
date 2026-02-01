package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.item.createEnchantedBook
import hiiragi283.core.api.item.enchantment.buildEnchantments
import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.item.getItemStack
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

class HTEnchantingRecipe(val ingredient: HTItemIngredient, val enchantments: ItemEnchantments, parameters: SubParameters) :
    HTViewProcessingRecipe(parameters) {
    companion object {
        @JvmStatic
        fun createExpIngredient(enchantments: ItemEnchantments): HTFluidIngredient = HTIngredientCreator.create(
            HCFluids.EXPERIENCE,
            HTExperienceHelper.getTotalMaxCost(enchantments).let(HTExperienceHelper::fluidAmountFromExp),
        )
    }

    constructor(ingredient: HTItemIngredient, holder: Holder<Enchantment>, parameters: SubParameters) : this(
        ingredient,
        buildEnchantments { set(holder, holder.value().maxLevel) },
        parameters,
    )

    val instances: List<EnchantmentInstance> =
        enchantments.entrySet().map { (holder: Holder<Enchantment>, level: Int) -> EnchantmentInstance(holder, level) }
    val expIngredient: HTFluidIngredient by lazy { createExpIngredient(enchantments) }

    override fun matches(input: HTViewRecipeInput, level: Level): Boolean {
        val bool1: Boolean = expIngredient.test(input.getFluidView(0))
        val bool2: Boolean = input.getItemView(0).getResource()?.isOf(Items.BOOK) ?: false
        val bool3: Boolean = ingredient.test(input.getItemView(1))
        return bool1 && bool2 && bool3
    }

    override fun assemble(input: HTViewRecipeInput, registries: HolderLookup.Provider): ItemStack {
        var stack: ItemStack = input.getItemView(0).getItemStack()
        stack = stack.item.applyEnchantments(stack, instances)
        return stack
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = createEnchantedBook(enchantments)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()
}
