package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

interface HTTankInteractingRecipe : HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val TYPE: HTDeferredRecipeType<RecipeInput, HTTankInteractingRecipe> =
            HTDeferredRecipeType(RagiumAPI.id(RagiumConst.TANK_INTERACTION))
    }

    fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack>

    fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack

    @Deprecated("Not used")
    override fun test(input: RecipeInput): Boolean = false

    @Deprecated("Not used")
    override fun assemble(input: RecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getType(): RecipeType<*> = TYPE.get()
}
