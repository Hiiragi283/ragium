package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.world.World

/**
 * レシピの処理を体系化するクラス
 * @see [HTMachineRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.processor.HTFurnaceRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.processor.HTSmithingRecipeProcessor]
 * @see [hiiragi283.ragium.common.recipe.processor.HTStoneCuttingRecipeProcessor]
 */
fun interface HTRecipeProcessor {
    fun process(world: World, tier: HTMachineTier): Result<Unit>
}
