package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTFluidRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

interface HTItemOrFluidRecipe :
    HTProcessingRecipe<HTItemAndFluidRecipeInput>,
    HTFluidRecipe<HTItemAndFluidRecipeInput> {
    override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean = getPredicate().map(
        { it.test(input.item) },
        { it.test(input.fluid) },
        { matchItem: Boolean, matchFluid: Boolean -> matchItem && matchFluid },
    )

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>>

    fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<Int, Int>
}
