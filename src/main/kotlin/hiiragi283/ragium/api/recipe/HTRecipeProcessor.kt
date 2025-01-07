package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.world.World

/**
 * レシピの処理を体系化するクラス
 * @see [hiiragi283.ragium.common.recipe.HTFurnaceRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.HTSmithingRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.HTStoneCuttingRecipeProcessor]
 */
fun interface HTRecipeProcessor {
    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Result<Unit>
}
