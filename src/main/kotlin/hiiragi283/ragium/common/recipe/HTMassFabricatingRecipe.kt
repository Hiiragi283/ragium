package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.ItemStack

@JvmRecord
data class HTMassFabricatingRecipe(val stack: ItemStack, val point: Int) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMassFabricatingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    ItemStack.CODEC.fieldOf(HTConst.ITEM).forGetter(HTMassFabricatingRecipe::stack),
                    ExtraCodecs.POSITIVE_INT.fieldOf("point").forGetter(HTMassFabricatingRecipe::point),
                ).apply(instance, ::HTMassFabricatingRecipe)
        }
    }
}
