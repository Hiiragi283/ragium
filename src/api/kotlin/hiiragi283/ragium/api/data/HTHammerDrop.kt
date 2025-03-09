package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.base.HTItemOutput

data class HTHammerDrop(val output: HTItemOutput, val chance: Float, val replace: Boolean) {
    companion object {
        @JvmField
        val CODEC: Codec<HTHammerDrop> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTItemOutput.CODEC.fieldOf("output").forGetter(HTHammerDrop::output),
                    Codec.floatRange(0f, 1f).optionalFieldOf("chance", 1f).forGetter(HTHammerDrop::chance),
                    Codec.BOOL.fieldOf("replace").forGetter(HTHammerDrop::replace),
                ).apply(instance, ::HTHammerDrop)
        }
    }
}
