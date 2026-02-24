package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.LevelAccessor

interface HTChancedRecipe<INPUT : RecipeInput> : HTProcessingRecipe<INPUT> {
    fun assembleExtraItem(input: INPUT, level: LevelAccessor): ItemStack = assembleExtraItem(input, level.registryAccess(), level.random)

    fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider, random: RandomSource): ItemStack =
        assembleExtraItem(input, registries, random.nextFloat())

    fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider, chance: Float): ItemStack

    //    Serializable    //

    interface Serializable<INPUT : RecipeInput> :
        HTChancedRecipe<INPUT>,
        HTSerializableRecipe<INPUT>
}
