package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.getStackOrNull
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.LevelAccessor
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

abstract class HTChancedRecipe<INPUT : RecipeInput>(
    val result: HTItemResult,
    val extraResult: Optional<HTChancedItemResult>,
    final override val time: Int,
) : HTProcessingRecipe.Serializable<INPUT> {
    fun getExtraResultItem(level: LevelAccessor): ItemStack? = extraResult.getOrNull()?.getStackOrNull(level)

    final override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
