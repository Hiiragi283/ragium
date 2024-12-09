package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTUnitResult
import net.minecraft.world.World

fun interface HTRecipeProcessor {
    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): HTUnitResult
}
