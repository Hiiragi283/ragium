package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.extension.toStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

/**
 * 機械レシピの抽象クラス
 */
abstract class HTMachineRecipeBase(private val group: String) : Recipe<HTMachineRecipeInput> {
    abstract fun isValidOutput(): Boolean

    protected abstract fun matches(input: HTMachineRecipeInput): Boolean

    protected abstract fun getRecipeType(): HTRecipeType<*>

    //    Recipe    //

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean {
        if (!isValidOutput()) return false
        return matches(input)
    }

    final override fun assemble(input: HTMachineRecipeInput, registries: HolderLookup.Provider): ItemStack =
        throw UnsupportedOperationException()

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group

    override fun getToastSymbol(): ItemStack = getRecipeType().machine.toStack()

    final override fun getSerializer(): RecipeSerializer<*> = getRecipeType().serializer

    final override fun getType(): RecipeType<*> = getRecipeType()
}
