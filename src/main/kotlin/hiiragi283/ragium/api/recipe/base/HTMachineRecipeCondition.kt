package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

interface HTMachineRecipeCondition {
    val codec: MapCodec<out HTMachineRecipeCondition>

    /**
     * JEI上での説明文
     */
    val text: Component

    /**
     * 指定した[level]と[pos]が条件を満たしているか判定します。
     * @param level 機械のワールド
     * @param pos 機械の座標
     */
    fun test(level: Level, pos: BlockPos): Boolean

    /**
     * [Ingredient]をベースとした[HTMachineRecipeCondition]
     */
    interface ItemBased : HTMachineRecipeCondition {
        val itemIngredient: Ingredient
    }

    /**
     * [FluidIngredient]をベースとした[HTMachineRecipeCondition]
     */
    interface FluidBased : HTMachineRecipeCondition {
        val fluidIngredient: FluidIngredient
    }
}
