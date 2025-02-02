package hiiragi283.ragium.common.recipe.condition

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.world.level.Level

class HTDummyCondition(override val text: Component) : HTMachineRecipeCondition {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTDummyCondition> =
            ComponentSerialization.CODEC.fieldOf("text").xmap(::HTDummyCondition, HTDummyCondition::text)
    }

    override val codec: MapCodec<out HTMachineRecipeCondition> = CODEC

    override fun test(level: Level, pos: BlockPos): Boolean = true
}
