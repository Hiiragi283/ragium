package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.common.fluid.HTFlowableFluid.Flowing
import hiiragi283.ragium.common.fluid.HTFlowableFluid.Still
import hiiragi283.ragium.common.util.blockSettings
import hiiragi283.ragium.common.util.itemSettings
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

class HTFluidContent private constructor(val settings: HTFlowableFluid.Settings) {
    companion object {
        @JvmStatic
        fun create(id: Identifier, builderAction: HTFlowableFluid.Settings.() -> Unit = {}): HTFluidContent =
            HTFluidContent(HTFlowableFluid.Settings().apply(builderAction)).apply {
                register(settings, id)
            }
    }

    lateinit var still: Still
        private set
    lateinit var flowing: Flowing
        private set
    lateinit var block: Block
        private set
    lateinit var bucketItem: Item
        private set

    fun register(settings: HTFlowableFluid.Settings, id: Identifier) {
        // still
        still = Registry.register(Registries.FLUID, id, Still(settings))
        settings.still = this.still
        // flowing
        flowing = Registry.register(Registries.FLUID, id.withPrefixedPath("flowing_"), Flowing(settings))
        settings.flowing = this.flowing
        // block if absent
        if (!settings.hasBlock) {
            block =
                Registry.register(
                    Registries.BLOCK,
                    id,
                    FluidBlock(
                        still,
                        blockSettings()
                            .replaceable()
                            .noCollision()
                            .strength(100.0f)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .dropsNothing()
                            .liquid()
                            .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY),
                    ),
                )
            settings.block = this.block
        } else {
            this.block = settings.block
        }
        // bucket if absent
        if (!settings.hasBucket) {
            bucketItem =
                Registry.register(
                    Registries.ITEM,
                    id.withSuffixedPath("_bucket"),
                    BucketItem(
                        still,
                        itemSettings()
                            .recipeRemainder(Items.BUCKET)
                            .maxCount(1),
                    ),
                )
            settings.bucketItem = this.bucketItem
        } else {
            this.bucketItem = settings.bucketItem
        }
    }

    fun registerAttributes(attributeHandler: FluidVariantAttributeHandler) {
        FluidVariantAttributes.register(still, attributeHandler)
    }
}
