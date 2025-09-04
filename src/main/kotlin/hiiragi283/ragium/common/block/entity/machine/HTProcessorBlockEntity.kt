package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.recipe.HTSimpleRecipeCache
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTProcessorBlockEntity<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
    private val recipeCache: HTRecipeCache<INPUT, RECIPE>,
    protected val variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(variant, pos, state) {
    constructor(
        recipeType: RecipeType<RECIPE>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : this(HTSimpleRecipeCache(recipeType), variant, pos, state)

    final override val energyUsage: Int get() = variant.energyUsage

    override fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): Boolean {
        // インプットに一致するレシピを探索する
        val input: INPUT = createRecipeInput(level, pos)
        val recipe: RECIPE = getMatchedRecipe(input, level) ?: return false
        val recipeEnergy: Int = getRequiredEnergy(recipe)
        // レシピの進行度を確認する
        if (this.requiredEnergy != recipeEnergy) {
            this.requiredEnergy = recipeEnergy
        }
        // エネルギーを消費する
        if (!doProgress(network)) return false
        // レシピを正常に扱えるか判定する
        if (!canProgressRecipe(level, input, recipe)) return false
        // レシピを実行する
        completeRecipe(level, pos, state, input, recipe)
        return true
    }

    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): INPUT

    protected open fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)

    protected open fun getRequiredEnergy(recipe: RECIPE): Int = variant.energyUsage * 20 * 10

    protected abstract fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean

    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: INPUT,
        recipe: RECIPE,
    )
}
