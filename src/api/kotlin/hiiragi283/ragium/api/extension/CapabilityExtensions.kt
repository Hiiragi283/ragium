package hiiragi283.ragium.api.extension

import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

//    IItemHandler    //

val IItemHandler.slotRange: IntRange get() = (0 until slots)

//    IFluidHandler    //

val IFluidHandler.tankRange: IntRange get() = (0 until tanks)
