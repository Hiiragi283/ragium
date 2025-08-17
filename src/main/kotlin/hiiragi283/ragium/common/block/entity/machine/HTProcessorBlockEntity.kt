package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.cache.HTRecipeCache
import hiiragi283.ragium.api.recipe.cache.HTSimpleRecipeCache
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTProcessorBlockEntity<I : RecipeInput, R : Recipe<I>>(
    private val recipeCache: HTRecipeCache<I, R>,
    protected val variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(variant, pos, state) {
    constructor(
        recipeType: RecipeType<R>,
        variant: HTMachineVariant,
        pos: BlockPos,
        state: BlockState,
    ) : this(HTSimpleRecipeCache(recipeType), variant, pos, state)

    final override val energyUsage: Int get() = variant.energyUsage

    private var lastInput: I? = null
    private var lastRecipe: R? = null

    final override fun serverTickPre(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // インプットに一致するレシピを探索する
        val input: I = createRecipeInput(level, pos)
        val lastRecipe: ResourceLocation? = recipeCache.lastRecipe
        val recipe: R = getMatchedRecipe(input, level) ?: return resetProgress()
        val recipeEnergy: Int = getRequiredEnergy(recipe)
        // レシピの進行度を確認する
        if (this.requiredEnergy != recipeEnergy) {
            this.requiredEnergy = recipeEnergy
            resetProgress()
        }
        if (recipeCache.lastRecipe != lastRecipe) {
            resetProgress()
        }
        this.lastInput = input
        this.lastRecipe = recipe
        // エネルギーを消費する
        if (!doProgress(network)) return TriState.DEFAULT
        // レシピを正常に扱えるか判定する
        if (!canProgressRecipe(level, input, recipe)) return TriState.FALSE
        return TriState.TRUE
    }

    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): I

    protected open fun getMatchedRecipe(input: I, level: ServerLevel): R? = recipeCache.getFirstRecipe(input, level)

    protected open fun getRequiredEnergy(recipe: R): Int = 2000 // TODO

    protected abstract fun canProgressRecipe(level: ServerLevel, input: I, recipe: R): Boolean

    final override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        result: TriState,
    ) {
        if (result.isTrue) {
            if (lastInput != null && lastRecipe != null) {
                serverTickPost(level, pos, state, lastInput!!, lastRecipe!!)
            }
            resetProgress()
        }
    }

    protected abstract fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: I,
        recipe: R,
    )

    protected fun resetProgress(): TriState {
        this.lastInput = null
        this.lastRecipe = null
        return TriState.FALSE
    }
}
