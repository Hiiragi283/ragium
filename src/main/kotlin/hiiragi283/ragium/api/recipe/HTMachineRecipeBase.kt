package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

/**
 * 機械レシピの抽象クラス
 */
abstract class HTMachineRecipeBase(private val group: String) : Recipe<HTMachineRecipeInput> {
    final override fun assemble(input: HTMachineRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group
}
