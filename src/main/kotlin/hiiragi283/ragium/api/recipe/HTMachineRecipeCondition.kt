package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toRegistryStream
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.function.Function

/**
 * [HTMachineRecipe]に追加できる条件を表すインターフェース
 */
interface HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineRecipeCondition> = RagiumAPI.Registries.RECIPE_CONDITION
            .byNameCodec()
            .dispatch(HTMachineRecipeCondition::codec, Function.identity())

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineRecipeCondition> =
            ByteBufCodecs
                .registry(RagiumAPI.RegistryKeys.RECIPE_CONDITION)
                .dispatch(HTMachineRecipeCondition::codec, MapCodec<out HTMachineRecipeCondition>::toRegistryStream)
    }

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
