package hiiragi283.ragium.api.recipe.base

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.enchantment.ItemEnchantments

/**
 * 機械レシピの抽象クラス
 */
abstract class HTMachineRecipeBase(private val group: String) : Recipe<HTMachineRecipeInput> {
    abstract val itemResults: List<HTItemResult>

    override fun assemble(input: HTMachineRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        itemResults.getOrNull(0)?.getItem(ItemEnchantments.EMPTY) ?: ItemStack.EMPTY

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group
}
