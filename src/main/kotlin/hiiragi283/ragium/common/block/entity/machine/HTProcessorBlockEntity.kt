package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
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
    recipeType: RecipeType<R>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(type, pos, state) {
    private val recipeCache: HTRecipeCache<I, R> = HTRecipeCache(recipeType)
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
        if (usedEnergy < requiredEnergy) {
            usedEnergy += network.extractEnergy(energyUsage, false)
        }
        if (usedEnergy < requiredEnergy) return TriState.DEFAULT
        usedEnergy -= requiredEnergy
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
