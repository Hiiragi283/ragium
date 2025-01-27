package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.function.Function

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
                .dispatch(HTMachineRecipeCondition::codec) { codec: MapCodec<out HTMachineRecipeCondition> ->
                    ByteBufCodecs.fromCodec(codec.codec())
                }
    }

    val codec: MapCodec<out HTMachineRecipeCondition>
    val text: MutableComponent

    fun test(level: Level, pos: BlockPos): Boolean

    interface ItemBased : HTMachineRecipeCondition {
        val itemIngredient: Ingredient
    }

    interface FluidBased : HTMachineRecipeCondition {
        val fluidIngredient: FluidIngredient
    }
}
