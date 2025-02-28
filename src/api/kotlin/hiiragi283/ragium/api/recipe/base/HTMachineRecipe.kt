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
abstract class HTMachineRecipe(private val group: String) : Recipe<HTMachineRecipeContext> {
    protected abstract fun matches(context: HTMachineRecipeContext): Boolean

    abstract fun canProcess(context: HTMachineRecipeContext): Result<Unit>

    abstract fun process(context: HTMachineRecipeContext)

    protected abstract fun getRecipeType(): HTRecipeType<*>

    //    Recipe    //

    override fun matches(input: HTMachineRecipeContext, level: Level): Boolean = matches(input)

    final override fun assemble(input: HTMachineRecipeContext, registries: HolderLookup.Provider): ItemStack =
        throw UnsupportedOperationException()

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group

    override fun getToastSymbol(): ItemStack = getRecipeType().machine.toStack()

    final override fun getSerializer(): RecipeSerializer<*> = getRecipeType().serializer

    final override fun getType(): RecipeType<*> = getRecipeType()
}
