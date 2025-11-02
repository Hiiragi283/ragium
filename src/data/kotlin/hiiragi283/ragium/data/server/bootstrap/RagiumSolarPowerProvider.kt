package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTSolarPower
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

object RagiumSolarPowerProvider : RegistrySetBuilder.RegistryBootstrap<HTSolarPower> {
    override fun run(context: BootstrapContext<HTSolarPower>) {
        register(context, Blocks.BEACON, 4f)
        register(context, Blocks.SHROOMLIGHT, 1f)
        register(context, CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GLOWSTONE, 1f)
        register(context, CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.GILDIUM, 4f)
        register(context, Tags.Blocks.CLUSTERS, 2f)
    }

    @JvmStatic
    private fun register(
        context: BootstrapContext<HTSolarPower>,
        block: Block,
        power: Float,
        builderAction: LocationPredicate.Builder.() -> Unit = {},
    ) {
        context.register(
            RagiumAPI.SOLAR_POWER_KEY.createKey(block.toHolderLike().getId().let(RagiumAPI::wrapId)),
            HTSolarPower(
                LocationPredicate.Builder
                    .location()
                    .setBlock(BlockPredicate.Builder.block().of(block))
                    .apply(builderAction)
                    .build(),
                power,
            ),
        )
    }

    @JvmStatic
    private fun register(
        context: BootstrapContext<HTSolarPower>,
        prefix: HTMaterialPrefix,
        material: HTMaterialLike,
        power: Float,
        builderAction: LocationPredicate.Builder.() -> Unit = {},
    ) {
        register(context, prefix.blockTagKey(material), power, builderAction)
    }

    @JvmStatic
    private fun register(
        context: BootstrapContext<HTSolarPower>,
        block: TagKey<Block>,
        power: Float,
        builderAction: LocationPredicate.Builder.() -> Unit = {},
    ) {
        context.register(
            RagiumAPI.SOLAR_POWER_KEY.createKey(block.location().let(RagiumAPI::wrapId)),
            HTSolarPower(
                LocationPredicate.Builder
                    .location()
                    .setBlock(BlockPredicate.Builder.block().of(block))
                    .apply(builderAction)
                    .build(),
                power,
            ),
        )
    }
}
