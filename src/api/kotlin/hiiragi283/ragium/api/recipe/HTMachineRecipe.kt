package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level

abstract class HTMachineRecipe : Recipe<HTMachineInput> {
    protected abstract fun matches(input: HTMachineInput): Boolean

    protected abstract fun canProcess(input: HTMachineInput): Boolean

    abstract fun process(input: HTMachineInput)

    //    Recipe    //

    override fun matches(input: HTMachineInput, level: Level): Boolean = matches(input) && canProcess(input)

    @Deprecated("use process() instead")
    final override fun assemble(input: HTMachineInput, registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    @Deprecated("use process() instead")
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = throw UnsupportedOperationException()

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getGroup(): String = group
}
