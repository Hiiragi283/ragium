package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import hiiragi283.ragium.api.extension.itemTagKey
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
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * @see mekanism.common.registration.impl.FluidDeferredRegister
 */
class HTFluidContentRegister(modId: String) {
    private val fluidRegister: DeferredRegister<Fluid> = DeferredRegister.create(Registries.FLUID, modId)
    private val typeRegister: DeferredRegister<FluidType> =
        DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modId)
    private val blockRegister = HTBlockRegister(modId)
    private val itemRegister = HTItemRegister(modId)

    val fluidEntries: Collection<DeferredHolder<Fluid, out Fluid>>
        get() = fluidRegister.entries
    val typeEntries: Collection<DeferredHolder<FluidType, out FluidType>>
        get() = typeRegister.entries
    val blockEntries: List<DeferredBlock<*>> get() = blockRegister.entries
    val itemEntries: List<DeferredItem<*>> get() = itemRegister.entries

    private val contentCache: MutableList<HTFluidContent<*, *, *>> = mutableListOf()
    val contents: List<HTFluidContent<*, *, *>> get() = contentCache

    fun init(eventBus: IEventBus) {
        fluidRegister.register(eventBus)
        typeRegister.register(eventBus)
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)

        eventBus.addListener { event: FMLCommonSetupEvent ->
            event.enqueueWork {
                for (item: DeferredItem<*> in itemEntries) {
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
        val typeHolder: DeferredHolder<FluidType, TYPE> =
            typeRegister.register(name) { _: ResourceLocation -> typeFactory(properties) }

        val stillId: ResourceLocation = ResourceLocation.fromNamespaceAndPath(fluidRegister.namespace, name)
        val flowId: ResourceLocation = stillId.withPrefix("flowing_")
        val bucketId: ResourceLocation = stillId.withSuffix("_bucket")

        val fluidProp: BaseFlowingFluid.Properties = BaseFlowingFluid
            .Properties(
                typeHolder,
                DeferredHolder.create(Registries.FLUID, stillId),
                DeferredHolder.create(Registries.FLUID, flowId),
            ).bucket(DeferredItem.createItem(bucketId))
            .block(DeferredBlock.createBlock(stillId))
        val stillFluid: DeferredHolder<Fluid, BaseFlowingFluid.Source> =
            fluidRegister.register(stillId.path) { _: ResourceLocation -> BaseFlowingFluid.Source(fluidProp) }
        val flowFluid: DeferredHolder<Fluid, BaseFlowingFluid.Flowing> =
            fluidRegister.register(flowId.path) { _: ResourceLocation -> BaseFlowingFluid.Flowing(fluidProp) }

        val liquidBlock: DeferredBlock<LiquidBlock> = blockRegister.registerBlock(
            name,
            { prop: BlockBehaviour.Properties -> LiquidBlock(stillFluid.get(), prop) },
            BlockBehaviour.Properties
                .of()
                .noCollission()
                .strength(100f)
                .noLootTable()
                .replaceable()
                .pushReaction(PushReaction.DESTROY)
                .liquid(),
        )
        val bucket: DeferredItem<BucketItem> = itemRegister.registerItem(
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
        val typeHolder: DeferredHolder<FluidType, TYPE>,
        val stillHolder: DeferredHolder<Fluid, STILL>,
        val flowHolder: DeferredHolder<Fluid, FLOW>,
        val blockHolder: DeferredBlock<*>,
        val bucketHolder: DeferredItem<*>,
    ) : HTFluidContent<TYPE, STILL, FLOW> {
        override val id: ResourceLocation = stillHolder.id
        private val commonId: ResourceLocation = commonId(id.path)

        override val commonTag: TagKey<Fluid> = fluidTagKey(commonId)

        override val bucketTag: TagKey<Item> = itemTagKey(commonId.withPrefix("buckets/"))

        override fun getType(): TYPE = typeHolder.get()

        override fun getStill(): STILL = stillHolder.get()

        override fun getFlow(): FLOW = flowHolder.get()

        override fun getBlock(): Block = blockHolder.get()

        override fun getBucket(): Item = bucketHolder.get()
    }
}
