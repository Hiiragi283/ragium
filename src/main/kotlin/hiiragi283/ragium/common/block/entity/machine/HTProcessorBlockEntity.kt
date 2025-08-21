package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTPlaySoundBlockEntity
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.recipe.HTSimpleRecipeCache
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTProcessorBlockEntity<I : RecipeInput, R : Recipe<I>>(
    private val recipeCache: HTRecipeCache<I, R>,
    protected val variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(variant, pos, state),
    HTPlaySoundBlockEntity {
    constructor(
        recipeType: RecipeType<R>,
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
        val input: I = createRecipeInput(level, pos)
        val recipe: R = getMatchedRecipe(input, level) ?: return false
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

    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): I

    protected open fun getMatchedRecipe(input: I, level: ServerLevel): R? = recipeCache.getFirstRecipe(input, level)

    protected open fun getRequiredEnergy(recipe: R): Int = 2000 // TODO

    protected abstract fun canProgressRecipe(level: ServerLevel, input: I, recipe: R): Boolean

    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: I,
        recipe: R,
    )

    //    HTPlaySoundBlockEntity    //

    override fun playSound(level: Level, pos: BlockPos) {
        if (ticks % 20 == 0) {
            Minecraft.getInstance().soundManager.play(createSound(level.random, pos))
        }
    }

    protected abstract fun createSound(random: RandomSource, pos: BlockPos): SoundInstance

    protected fun createSound(
        sound: SoundEvent,
        random: RandomSource,
        pos: BlockPos,
        volume: Float = 1f,
        pitch: Float = 1f,
    ): SoundInstance = SimpleSoundInstance(sound, SoundSource.BLOCKS, volume, pitch, random, pos)
}
