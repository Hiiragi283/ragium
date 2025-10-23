package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.registry.impl.HTDeferredFluid
import hiiragi283.ragium.api.registry.impl.HTDeferredFluidRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredFluidType
import hiiragi283.ragium.api.registry.impl.HTDeferredFluidTypeRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredOnlyBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredOnlyBlockRegister
import hiiragi283.ragium.api.tag.createTagKey
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

/**
 * @see mekanism.common.registration.impl.FluidDeferredRegister
 */
class HTFluidContentRegister(modId: String) {
    private val fluidRegister = HTDeferredFluidRegister(modId)
    private val typeRegister = HTDeferredFluidTypeRegister(modId)
    private val blockRegister = HTDeferredOnlyBlockRegister(modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    val fluidEntries: Collection<HTDeferredFluid<*>> get() = fluidRegister.entries
    val typeEntries: Collection<HTDeferredFluidType<*>> get() = typeRegister.entries
    val blockEntries: Collection<HTDeferredOnlyBlock<*>> get() = blockRegister.entries
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
        // Fluid Type
        val typeHolder: HTDeferredFluidType<TYPE> = typeRegister.registerType(name, properties, typeFactory)
        // Fluid Holder
        val stillHolder: HTDeferredFluid<BaseFlowingFluid.Source> = HTDeferredFluid(fluidRegister.createId(name))
        // Bucket Item
        val bucket: HTDeferredItem<BucketItem> = itemRegister.registerItemWith(
            "${name}_bucket",
            stillHolder.get(),
            ::BucketItem,
        ) { it.stacksTo(1).craftRemainder(Items.BUCKET) }
        // Liquid Block
        val liquidBlock: HTDeferredOnlyBlock<LiquidBlock> = blockRegister.registerBlock(
            name,
            BlockBehaviour.Properties
                .of()
                .noCollission()
                .strength(100f)
                .noLootTable()
                .replaceable()
                .pushReaction(PushReaction.DESTROY)
                .liquid(),
        ) { prop: BlockBehaviour.Properties ->
            LiquidBlock(stillHolder.get(), prop)
        }
        // Fluid
        val (_, flowingHolder: HTDeferredFluid<BaseFlowingFluid.Flowing>) = fluidRegister.registerFluids(name, typeHolder) {
            it.bucket(bucket).block(liquidBlock)
        }
        // Content
        val content: HTFluidContent<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = ContentImpl(
            typeHolder,
            stillHolder,
            flowingHolder,
            liquidBlock,
            bucket,
        )
        contentCache.add(content)
        return content
    }

    private class ContentImpl<TYPE : FluidType, STILL : Fluid, FLOW : Fluid>(
        val typeHolder: HTDeferredFluidType<TYPE>,
        val stillHolder: HTDeferredFluid<STILL>,
        val flowHolder: HTDeferredFluid<FLOW>,
        val blockHolder: HTDeferredOnlyBlock<*>,
        val bucketHolder: HTDeferredItem<*>,
    ) : HTFluidContent<TYPE, STILL, FLOW> {
        override val commonTag: TagKey<Fluid> = Registries.FLUID.createTagKey(commonId(getPath()))

        override val bucketTag: TagKey<Item> = Registries.ITEM.createTagKey(commonId("buckets", getPath()))

        override fun getType(): TYPE = typeHolder.get()

        override fun getStill(): STILL = stillHolder.get()

        override fun getFlow(): FLOW = flowHolder.get()

        override fun getBlock(): Block = blockHolder.get()

        override fun getBucket(): Item = bucketHolder.get()

        override fun getId(): ResourceLocation = stillHolder.id
    }
}
