package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.extension.toStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

/**
 * 機械レシピの抽象クラス
 */
abstract class HTMachineRecipeBase(private val group: String) : Recipe<HTMachineRecipeInput> {
    abstract val itemOutputs: List<HTItemOutput>

    override fun assemble(input: HTMachineRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemOutputs.getOrNull(0)?.get() ?: ItemStack.EMPTY

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group

    protected abstract fun getRecipeType(): HTRecipeType<*>

    override fun getToastSymbol(): ItemStack = getRecipeType().machine.toStack()

    final override fun getSerializer(): RecipeSerializer<*> = getRecipeType().serializer

    final override fun getType(): RecipeType<*> = getRecipeType()
}
