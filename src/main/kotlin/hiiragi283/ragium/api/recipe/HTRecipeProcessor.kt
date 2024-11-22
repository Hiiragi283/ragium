package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.world.World

fun interface HTRecipeProcessor {
    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): DataResult<Unit>
}
