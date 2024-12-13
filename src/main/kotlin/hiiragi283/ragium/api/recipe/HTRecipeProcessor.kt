package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTUnitResult
import net.minecraft.world.World

/**
 * Handler to process [net.minecraft.recipe.Recipe]
 * @see [hiiragi283.ragium.common.recipe.HTFurnaceRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.HTSmithingRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.HTStoneCuttingRecipeProcessor]
 */
fun interface HTRecipeProcessor {
    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): HTUnitResult
}
