package hiiragi283.ragium.api.extension

import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

//    RecipeInput    //

val RecipeInput.indices: IntRange get() = (0..<this.size())

//    IItemHandler    //

val IItemHandler.slotRange: IntRange get() = (0..<slots)

//    IFluidHandler    //

val IFluidHandler.tankRange: IntRange get() = (0..<tanks)
