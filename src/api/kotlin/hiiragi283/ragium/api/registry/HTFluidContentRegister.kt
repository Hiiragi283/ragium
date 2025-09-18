package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.createTagKey
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.DispenseFluidContainer
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * @see mekanism.common.registration.impl.FluidDeferredRegister
 */
class HTFluidContentRegister(modId: String) {
    private val fluidRegister: HTDeferredRegister<Fluid> = HTDeferredRegister(Registries.FLUID, modId)
    private val typeRegister: HTDeferredRegister<FluidType> = HTDeferredRegister(NeoForgeRegistries.Keys.FLUID_TYPES, modId)
    private val blockRegister: HTDeferredRegister<Block> = HTDeferredRegister(Registries.BLOCK, modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    val fluidEntries: Collection<HTDeferredHolder<Fluid, *>>
        get() = fluidRegister.entries
    val typeEntries: Collection<HTDeferredHolder<FluidType, *>>
        get() = typeRegister.entries
    val blockEntries: Collection<HTDeferredHolder<Block, *>> get() = blockRegister.entries
    val itemEntries: Collection<HTDeferredItem<*>> get() = itemRegister.entries

    private val contentCache: MutableList<HTFluidContent<*, *, *>> = mutableListOf()
    val contents: List<HTFluidContent<*, *, *>> get() = contentCache

    fun init(eventBus: IEventBus) {
        fluidRegister.register(eventBus)
        typeRegister.register(eventBus)
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)

        eventBus.addListener { event: FMLCommonSetupEvent ->
            event.enqueueWork {
                for (item: HTDeferredItem<*> in itemEntries) {
                    DispenserBlock.registerBehavior(item, DispenseFluidContainer.getInstance())
                }
            }
        }
    }

    fun register(
        name: String,
        properties: FluidType.Properties,
    ): HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(name, properties, ::FluidType)

    fun <TYPE : FluidType> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
    ): HTFluidContent<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> {
        val typeHolder: HTDeferredHolder<FluidType, TYPE> =
            typeRegister.register(name) { _: ResourceLocation -> typeFactory(properties) }

        val stillId: ResourceLocation = fluidRegister.createId(name)
        val flowId: ResourceLocation = stillId.withPrefix("flowing_")
        val bucketId: ResourceLocation = stillId.withSuffix("_bucket")

        val fluidProp: BaseFlowingFluid.Properties = BaseFlowingFluid
            .Properties(
                typeHolder,
                HTDeferredHolder.createSimple(Registries.FLUID, stillId),
                HTDeferredHolder.createSimple(Registries.FLUID, flowId),
            ).bucket(HTDeferredItem(bucketId))
            .block(HTDeferredHolder(Registries.BLOCK, stillId))
        val stillFluid: HTDeferredHolder<Fluid, BaseFlowingFluid.Source> =
            fluidRegister.register(stillId.path) { _: ResourceLocation -> BaseFlowingFluid.Source(fluidProp) }
        val flowFluid: HTDeferredHolder<Fluid, BaseFlowingFluid.Flowing> =
            fluidRegister.register(flowId.path) { _: ResourceLocation -> BaseFlowingFluid.Flowing(fluidProp) }

        val liquidBlock: HTDeferredHolder<Block, *> = blockRegister.register(
            name,
            { _: ResourceLocation ->
                LiquidBlock(
                    stillFluid.get(),
                    BlockBehaviour.Properties
                        .of()
                        .noCollission()
                        .strength(100f)
                        .noLootTable()
                        .replaceable()
                        .pushReaction(PushReaction.DESTROY)
                        .liquid(),
                )
            },
        )
        val bucket: HTDeferredItem<BucketItem> = itemRegister.registerItem(
            bucketId.path,
            { prop: Item.Properties -> BucketItem(stillFluid.get(), prop) },
            Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET),
        )

        val content: ContentImpl<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = ContentImpl(
            typeHolder,
            stillFluid,
            flowFluid,
            liquidBlock,
            bucket,
        )
        contentCache.add(content)
        return content
    }

    private class ContentImpl<TYPE : FluidType, STILL : Fluid, FLOW : Fluid>(
        val typeHolder: HTDeferredHolder<FluidType, TYPE>,
        val stillHolder: HTDeferredHolder<Fluid, STILL>,
        val flowHolder: HTDeferredHolder<Fluid, FLOW>,
        val blockHolder: HTDeferredHolder<Block, *>,
        val bucketHolder: HTDeferredItem<*>,
    ) : HTFluidContent<TYPE, STILL, FLOW> {
        private val commonId: ResourceLocation = commonId(getPath())

        override val commonTag: TagKey<Fluid> = Registries.FLUID.createTagKey(commonId)

        override val bucketTag: TagKey<Item> = Registries.ITEM.createTagKey(commonId.withPrefix("buckets/"))

        override fun getType(): TYPE = typeHolder.get()

        override fun getStill(): STILL = stillHolder.get()

        override fun getFlow(): FLOW = flowHolder.get()

        override fun getBlock(): Block = blockHolder.get()

        override fun getBucket(): Item = bucketHolder.get()

        override fun getId(): ResourceLocation = stillHolder.id
    }
}
