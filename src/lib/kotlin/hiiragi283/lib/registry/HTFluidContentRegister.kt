package hiiragi283.lib.registry

import hiiragi283.lib.HTConstants
import hiiragi283.lib.fluid.HTVirtualFluid
import hiiragi283.lib.resource.SupplierWithKey
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.createTagKey
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
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
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Flowing
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Source
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.DispenseFluidContainer

/**
 * [液体][Fluid]を登録する[HTDeferredRegister]の補助クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTFluidContentRegister(modId: String) {
    private val fluidRegister: HTDeferredRegister<Fluid> = HTDeferredRegister(Registries.FLUID, modId)
    private val typeRegister = HTDeferredFluidTypeRegister(modId)
    private val blockRegister = HTDeferredBlockRegister(modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    /**
     * 登録された液体の一覧を取得します。
     */
    fun asFluidSequence(): Sequence<HTDeferredHolder<Fluid, *>> = fluidRegister.asSequence()

    /**
     * 登録された[FluidType]の一覧を取得します。
     */
    fun asTypeSequence(): Sequence<HTDeferredFluidType<*>> = typeRegister.asSequence()

    /**
     * 登録された液体ブロックの一覧を取得します。
     */
    fun asBlockSequence(): Sequence<HTDeferredBlock<*>> = blockRegister.asSequence()

    /**
     * 登録された液体入りバケツの一覧を取得します。
     */
    fun asItemSequence(): Sequence<HTDeferredItem<*>> = itemRegister.asSequence()

    private val contentsCache: MutableMap<ResourceKey<Fluid>, HTFluidContent> = mutableMapOf()

    /**
     * 登録された液体の[ResourceKey]の一覧
     */
    val keys: Set<ResourceKey<Fluid>> get() = contentsCache.keys

    /**
     * 登録された[HTFluidContent]の一覧
     */
    val entries: Collection<HTFluidContent> get() = contentsCache.values

    /**
     * [HTFluidContent]を取得します。
     * @param key 対応する液体の[ResourceKey]
     * @return 対応する[HTFluidContent]がない場合はnull`
     */
    operator fun get(key: ResourceKey<Fluid>): HTFluidContent? = contentsCache[key]

    /**
     * 登録された[HTFluidContent]の一覧を取得します。
     */
    fun asSequence(): Sequence<HTFluidContent> = entries.asSequence()

    fun addAlias(from: String, to: String) {
        typeRegister.addAlias(from, to)

        fluidRegister.addAlias(from, to)
        fluidRegister.addAlias("flowing_$from", "flowing_$to")

        blockRegister.addAlias(from, to)

        itemRegister.addAlias("${from}_bucket", "${to}_bucket")
    }

    /**
     * [IEventBus]に登録します。
     */
    fun register(eventBus: IEventBus) {
        fluidRegister.register(eventBus)
        typeRegister.register(eventBus)
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)

        eventBus.addListener { event: FMLCommonSetupEvent ->
            event.enqueueWork {
                for (item: HTDeferredItem<*> in asItemSequence()) {
                    DispenserBlock.registerBehavior(item, DispenseFluidContainer.getInstance())
                }
            }
        }
    }

    /**
     * 液体源のみの新しい液体を登録します。
     * @param name 液体のIDのパス
     * @param builderAction [VirtualBuilder]を初期化するブロック
     * @return 新しい[HTFluidContent]のインスタンス
     */
    inline fun registerVirtual(name: String, builderAction: VirtualBuilder.() -> Unit): HTFluidContent.Virtual = VirtualBuilder(name).apply(builderAction).build()

    /**
     * 液体流をもつの新しい液体を登録します。
     * @param name 液体のIDのパス
     * @param builderAction [FlowingBuilder]を初期化するブロック
     * @return 新しい[HTFluidContent]のインスタンス
     */
    inline fun registerFlowing(name: String, builderAction: FlowingBuilder.() -> Unit): HTFluidContent.Flowing = FlowingBuilder(name).apply(builderAction).build()

    //    Builder    //

    /**
     * [HTFluidContent]のビルダークラスです。
     * @param FLUID 液体のクラス
     * @param CONTENT 出力する[HTFluidContent]のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @HTBuilderMarker
    abstract inner class Builder<FLUID : Fluid, CONTENT : HTFluidContent>(protected val name: String) {
        // Required
        /**
         * [FluidType]のプロパティ
         */
        lateinit var properties: FluidType.Properties

        /**
         * [FluidType]をを作るブロック
         */
        var typeFactory: (FluidType.Properties) -> FluidType = ::FluidType

        /**
         * 液体入りバケツを作るブロック
         */
        var bucketFactory: ItemWithContextFactory<Fluid, Item> = ::BucketItem

        /**
         * 液体の共通タグ
         */
        var fluidTag: Identifier = HTConstants.COMMON.toId(name)

        /**
         * 液体入りバケツの共通タグ
         */
        var bucketTag: Identifier = HTConstants.COMMON.toId("buckets", name)
        // Optional

        fun build(): CONTENT {
            // Fluid Type
            val typeHolder: HTDeferredFluidType<FluidType> = typeRegister.registerType(name, properties.descriptionId("block.${typeRegister.namespace}.$name"), typeFactory)
            // Fluid Holder
            val sourceHolder: HTDeferredHolder<Fluid, FLUID> = HTDeferredHolder(fluidRegister.createKey(name))
            // Bucket Item
            val bucketHolder: HTSimpleDeferredItem = itemRegister.registerItem(
                "${name}_bucket",
                { bucketFactory(sourceHolder.get(), it) },
                { it.stacksTo(1).craftRemainder(Items.BUCKET) },
            )
            val content: CONTENT = createContent(typeHolder, sourceHolder, bucketHolder)
            contentsCache[sourceHolder.key] = content
            return content
        }

        protected abstract fun createContent(
            typeHolder: HTDeferredFluidType<FluidType>,
            sourceHolder: HTDeferredHolder<Fluid, FLUID>,
            bucketHolder: HTSimpleDeferredItem,
        ): CONTENT
    }

    /**
     * [HTFluidContent.Virtual]向けの[Builder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    inner class VirtualBuilder(name: String) : Builder<HTVirtualFluid, HTFluidContent.Virtual>(name) {
        override fun createContent(
            typeHolder: HTDeferredFluidType<FluidType>,
            sourceHolder: HTDeferredHolder<Fluid, HTVirtualFluid>,
            bucketHolder: HTSimpleDeferredItem,
        ): HTFluidContent.Virtual {
            // Content
            fluidRegister.register(name) { _ -> HTVirtualFluid(typeHolder, bucketHolder) }
            return HTFluidContent.Virtual(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createTagKey(fluidTag),
                Registries.ITEM.createTagKey(bucketTag),
            )
        }
    }

    /**
     * [HTFluidContent.Flowing]向けの[Builder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    inner class FlowingBuilder(name: String) : Builder<BaseFlowingFluid, HTFluidContent.Flowing>(name) {
        /**
         * 液体源を作るブロック
         */
        var sourceFactory: (BaseFlowingFluid.Properties) -> Source = BaseFlowingFluid::Source

        /**
         * 液体流を作るブロック
         */
        var flowingFactory: (BaseFlowingFluid.Properties) -> Flowing = BaseFlowingFluid::Flowing

        /**
         * 液体ブロックを作成するブロック
         *
         * `null`の場合，液体ブロックは作成されません。
         */
        var blockFactory: ((BaseFlowingFluid, BlockBehaviour.Properties) -> LiquidBlock)? = ::LiquidBlock

        /**
         * 液体ブロックのプロパティを初期化するブロック
         */
        var blockProperties: Identity<BlockBehaviour.Properties> = identity()

        override fun createContent(
            typeHolder: HTDeferredFluidType<FluidType>,
            sourceHolder: HTDeferredHolder<Fluid, BaseFlowingFluid>,
            bucketHolder: HTSimpleDeferredItem,
        ): HTFluidContent.Flowing {
            // Liquid Block
            val blockHolder: SupplierWithKey<Block, LiquidBlock>?
            if (blockFactory == null) {
                blockHolder = null
            } else {
                blockHolder = blockRegister.registerBlock(
                    name,
                    BlockBehaviour.Properties
                        .of()
                        .let(blockProperties)
                        .noCollision()
                        .strength(100f)
                        .noLootTable()
                        .replaceable()
                        .pushReaction(PushReaction.DESTROY)
                        .liquid(),
                ) { prop: BlockBehaviour.Properties -> blockFactory!!(sourceHolder.get(), prop) }
            }
            // Fluid
            val flowingHolder: HTDeferredHolder<Fluid, Flowing> = HTDeferredHolder(fluidRegister.createKey("flowing_$name"))
            val fluidProperties: BaseFlowingFluid.Properties = BaseFlowingFluid
                .Properties(typeHolder, sourceHolder, flowingHolder)
                .bucket(bucketHolder)
            blockHolder?.let(fluidProperties::block)
            fluidRegister.register(name) { _ -> sourceFactory(fluidProperties) }
            fluidRegister.register(flowingHolder.id.path) { _ -> flowingFactory(fluidProperties) }
            // Content
            return HTFluidContent.Flowing(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createTagKey(fluidTag),
                Registries.ITEM.createTagKey(bucketTag),
                flowingHolder,
                blockHolder,
            )
        }
    }
}
