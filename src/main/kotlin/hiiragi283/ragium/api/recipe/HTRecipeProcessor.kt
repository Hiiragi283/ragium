package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.server.level.ServerLevel

fun interface HTRecipeProcessor {
    fun process(level: ServerLevel, tier: HTMachineTier): Result<Unit>
}
