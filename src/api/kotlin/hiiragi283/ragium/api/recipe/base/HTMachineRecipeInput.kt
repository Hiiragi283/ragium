package hiiragi283.ragium.api.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 機械レシピのインプットを表すクラス
 * @param items アイテムのインプットの一覧
 * @param fluids 液体のインプットの一覧
 */
class HTMachineRecipeInput private constructor(
    val enchantments: ItemEnchantments,
    val items: List<ItemStack>,
    val fluids: List<FluidStack>,
) : RecipeInput {
    companion object {
        @JvmStatic
        fun of(enchantments: ItemEnchantments, items: List<ItemStack>, fluids: List<FluidStack>): HTMachineRecipeInput =
            HTMachineRecipeInput(enchantments, items.map(ItemStack::copy), fluids.map(FluidStack::copy))

        @JvmStatic
        fun of(enchantments: ItemEnchantments, vararg items: ItemStack): HTMachineRecipeInput = of(enchantments, listOf(*items), listOf())

        @JvmStatic
        fun of(enchantments: ItemEnchantments, vararg fluids: FluidStack): HTMachineRecipeInput =
            of(enchantments, listOf(), listOf(*fluids))

        @JvmStatic
        fun of(enchantments: ItemEnchantments, item: ItemStack, fluid: FluidStack): HTMachineRecipeInput =
            of(enchantments, listOf(item), listOf(fluid))
    }

    override fun getItem(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): FluidStack = fluids.getOrNull(index) ?: FluidStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() && fluids.isEmpty()
}
