package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.common.fluid.HTFlowableFluid.Flowing
import hiiragi283.ragium.common.fluid.HTFlowableFluid.Still
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.client.render.RenderLayer
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
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

    private var generateBlockState: Boolean = false
    private var generateBucketModel: Boolean = false

    fun register(settings: HTFlowableFluid.Settings, id: Identifier) {
        // still
        still = Registry.register(Registries.FLUID, id, Still(settings))
        settings.still = this.still
        // flowing
        flowing = Registry.register(Registries.FLUID, id.withPrefixedPath("flowing_"), Flowing(settings))
        settings.flowing = this.flowing
        // block if absent
        if (!settings.hasBlock) {
            block = Registry.register(
                Registries.BLOCK,
                id,
                FluidBlock(
                    still,
                    AbstractBlock.Settings.create()
                        .replaceable()
                        .noCollision()
                        .strength(100.0f)
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .dropsNothing()
                        .liquid()
                        .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
                )
            )
            settings.block = this.block
            generateBlockState = true
        } else {
            this.block = settings.block
        }
        // bucket if absent
        if (!settings.hasBucket) {
            bucketItem = Registry.register(
                Registries.ITEM,
                id.withSuffixedPath("_bucket"),
                BucketItem(
                    still,
                    Item.Settings()
                        .recipeRemainder(Items.BUCKET)
                        .maxCount(1)
                )
            )
            settings.bucketItem = this.bucketItem
            generateBucketModel = true
        } else {
            this.bucketItem = settings.bucketItem
        }
    }

    fun registerAttributes(attributeBuilder: HTFluidVariantAttributeHandler.() -> Unit = {}) {
        registerAttributes(HTFluidVariantAttributeHandler.create(still, attributeBuilder))
    }

    fun registerAttributes(attributeHandler: FluidVariantAttributeHandler) {
        // register fluid attribute
        FluidVariantAttributes.register(still, attributeHandler)
    }

    //    Client    //

    fun registerClient(stillTex: Identifier, flowingTex: Identifier = stillTex, color: Int = -1) {
        registerClient(SimpleFluidRenderHandler(stillTex, flowingTex, color))
    }

    fun registerClient(renderHandler: FluidRenderHandler) {
        // register render handler
        FluidRenderHandlerRegistry.INSTANCE.register(still, flowing, renderHandler)
        // register render layers
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still, flowing)
    }

    //    Data Gen    //

    fun generateBlockState(generator: BlockStateModelGenerator) {
        if (generateBlockState) generator.registerSimpleState(block)
    }

    fun generateBucketModel(generator: ItemModelGenerator) {
        if (generateBucketModel) generator.register(bucketItem, Models.GENERATED)
    }
}
