package hiiragi283.ragium.api.extension

import net.minecraft.network.chat.Component
import net.minecraft.world.level.material.Fluid

//    Fluid    //

val Fluid.name: Component
    get() = fluidType.description
