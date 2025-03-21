package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.registry.HTRecipeType
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

abstract class HTMachineRecipe(val recipeType: HTRecipeType<HTMachineInput, *>) : Recipe<HTMachineInput> {
    protected abstract fun matches(input: HTMachineInput): Boolean

    abstract fun canProcess(input: HTMachineInput): Boolean

    abstract fun process(input: HTMachineInput)

    abstract fun getDefinition(): DataResult<HTRecipeDefinition>

    //    Recipe    //

    override fun matches(input: HTMachineInput, level: Level): Boolean = matches(input)

    @Deprecated("use process() instead")
    final override fun assemble(input: HTMachineInput, registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    @Deprecated("use process() instead")
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group

    final override fun getSerializer(): RecipeSerializer<*> = recipeType.serializer

    final override fun getType(): RecipeType<*> = recipeType
}
