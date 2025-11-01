package hiiragi283.ragium.common.world.feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

class HTFilteredBlockConfiguration(val predicate: BlockPredicate, val provider: BlockStateProvider) : FeatureConfiguration {
    companion object {
        @JvmField
        val CODEC: Codec<HTFilteredBlockConfiguration> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    BlockPredicate.CODEC.fieldOf("predicate").forGetter(HTFilteredBlockConfiguration::predicate),
                    BlockStateProvider.CODEC.fieldOf("provider").forGetter(HTFilteredBlockConfiguration::provider),
                ).apply(instance, ::HTFilteredBlockConfiguration)
        }
    }
}
