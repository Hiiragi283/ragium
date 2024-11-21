package hiiragi283.ragium.api.recipe.processor

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.world.World

fun interface HTRecipeProcessor {
    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Boolean
}
