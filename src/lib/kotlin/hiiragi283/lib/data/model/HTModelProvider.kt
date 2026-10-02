package hiiragi283.lib.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.HTDecorationContent
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTSimpleValueWithKey
import hiiragi283.lib.resource.HTValueWithKey
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import hiiragi283.lib.resource.toId
import hiiragi283.lib.resource.vanillaId
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel
import java.util.Optional

/**
 * Hiiragi Seriesで使用される[ModelProvider]の拡張クラスです。。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTModelProvider(output: PackOutput, modId: String) : ModelProvider(output, modId) {
    abstract override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators)

    //    Block    //

    /**
     * @since 26.1.8
     */
    fun BlockModelGenerators.plainVariant(
        block: HTSimpleValueWithKey<*>,
        template: ModelTemplate,
        mapping: TextureMapping
    ): MultiVariant = BlockModelGenerators.plainVariant(template.createBlock(block, mapping, this.modelOutput))

    /**
     * ブロックJSONを生成します。
     * @param block ブロックのインスタンス
     * @param template 生成するモデルのテンプレート
     * @param mapping テクスチャのマッピング
     * @since 26.1.8
     */
    fun BlockModelGenerators.createSimple(
        block: HTSimpleValueWithKey<Block>,
        template: ModelTemplate,
        mapping: TextureMapping
    ) {
        this.createSimple(block.getOrThrow(), this.plainVariant(block, template, mapping))
    }

    /**
     * ブロックJSONを生成します。
     * @param block ブロックのインスタンス
     * @param modelId 使用するモデルのID
     */
    fun BlockModelGenerators.createSimple(block: Block, modelId: Identifier) {
        this.createSimple(block, BlockModelGenerators.plainVariant(modelId))
    }

    /**
     * ブロックJSONを生成します。
     * @param block ブロックの提供元
     * @param variant マルチパート形式のヴァリアント
     */
    fun BlockModelGenerators.createSimple(block: Block, variant: MultiVariant) {
        this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, variant))
    }

    /**
     * @since 26.1.8
     */
    fun BlockModelGenerators.createDecoration(content: HTDecorationContent) {
        val fullBlockId: Identifier = content.base.idOrThrow.blockId
        val texture = Material(fullBlockId)

        this.createSlab(content.slab, fullBlockId, texture, texture, texture)
        content.stairs?.let { this.createStairs(it, texture, texture, texture) }
        content.wall?.let { this.createWall(it, texture) }
    }

    /**
     * ハーフブロックのブロックJSONを生成します。
     */
    fun BlockModelGenerators.createSlab(
        block: HTValueWithKey<Block, SlabBlock>,
        fullModel: Identifier,
        top: Material,
        side: Material,
        bottom: Material
    ) {
        val slab: SlabBlock = block.getOrThrow()
        val mapping: TextureMapping = TextureMapping().put(
            TextureSlot.TOP,
            top
        ).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.SIDE, side)
        val modelId: Identifier = ModelTemplates.SLAB_BOTTOM.createBlock(block, mapping, modelOutput)

        blockStateOutput.accept(
            BlockModelGenerators.createSlab(
                slab,
                BlockModelGenerators.plainVariant(modelId),
                this.plainVariant(block, ModelTemplates.SLAB_TOP, mapping),
                BlockModelGenerators.plainVariant(fullModel)
            )
        )
        registerSimpleItemModel(slab, modelId)
    }

    /**
     * 階段ブロックのブロックJSONを生成します。
     */
    fun BlockModelGenerators.createStairs(
        block: HTValueWithKey<Block, StairBlock>,
        top: Material,
        side: Material,
        bottom: Material
    ) {
        val stairs: StairBlock = block.getOrThrow()
        val mapping: TextureMapping = TextureMapping()
            .put(TextureSlot.TOP, top)
            .put(TextureSlot.BOTTOM, bottom)
            .put(TextureSlot.SIDE, side)
        val modelId: Identifier = ModelTemplates.STAIRS_STRAIGHT.createBlock(block, mapping, modelOutput)

        blockStateOutput.accept(
            BlockModelGenerators.createStairs(
                stairs,
                this.plainVariant(block, ModelTemplates.STAIRS_INNER, mapping),
                BlockModelGenerators.plainVariant(modelId),
                this.plainVariant(block, ModelTemplates.STAIRS_OUTER, mapping)
            )
        )
        registerSimpleItemModel(stairs, modelId)
    }

    /**
     * 壁ブロックのブロックJSONを生成します。
     * @since 26.1.8
     */
    fun BlockModelGenerators.createWall(block: HTValueWithKey<Block, WallBlock>, wall: Material) {
        val mapping: TextureMapping = TextureMapping().put(TextureSlot.WALL, wall)
        this.blockStateOutput.accept(
            BlockModelGenerators.createWall(
                block.getOrThrow(),
                this.plainVariant(block, ModelTemplates.WALL_POST, mapping),
                this.plainVariant(block, ModelTemplates.WALL_LOW_SIDE, mapping),
                this.plainVariant(block, ModelTemplates.WALL_TALL_SIDE, mapping)
            )
        )
        this.registerSimpleItemModel(
            block.getOrThrow(),
            ModelTemplates.WALL_INVENTORY.createBlock(block, mapping, this.modelOutput)
        )
    }

    /**
     * 液体ブロックのブロックJSONを生成します。
     * @param fluidBlock 液体ブロックの提供元
     */
    fun BlockModelGenerators.createFluid(fluidBlock: HTSimpleValueWithKey<Block>) {
        this.createSimple(
            fluidBlock,
            HTModelTemplates.FLUID_BLOCK,
            TextureMapping.particle(Material(vanillaId(HTConstants.BLOCK, "water_still")))
        )
    }

    //    Item    //

    /**
     * アイテムJSONを生成します。
     * @param item アイテムの提供元
     * @param layer モデルのテクスチャのパス
     * @param template 使用するモデルのテンプレート
     */
    fun ItemModelGenerators.generateFlatItem(
        item: HTSimpleValueWithKey<Item>,
        layer: Identifier = item.idOrThrow.itemId,
        template: ModelTemplate = ModelTemplates.FLAT_ITEM
    ) {
        this.itemModelOutput.accept(
            item.getOrThrow(),
            ItemModelUtils.plainModel(this.createFlatItemModel(item, layer, template))
        )
    }

    /**
     * アイテムJSONを生成します。
     * @param item アイテムのIDの提供元
     * @param layers モデルのテクスチャのパス
     * @throws IllegalStateException [layers]のサイズが`0`または`4`以上の場合
     */
    fun ItemModelGenerators.generateLayeredItem(item: HTSimpleValueWithKey<Item>, vararg layers: Identifier) {
        val (mapping: TextureMapping, template: ModelTemplate) = when (layers.size) {
            1 -> TextureMapping.layer0(Material(layers[0])) to ModelTemplates.FLAT_ITEM

            2 -> TextureMapping.layered(Material(layers[0]), Material(layers[1])) to ModelTemplates.TWO_LAYERED_ITEM

            3 -> TextureMapping.layered(Material(layers[0]), Material(layers[1]), Material(layers[2])) to
                ModelTemplates.THREE_LAYERED_ITEM

            else -> error("Cannot create item model with ${layers.size} layers")
        }
        this.itemModelOutput.accept(
            item.getOrThrow(),
            ItemModelUtils.plainModel(template.createItem(item, mapping, this.modelOutput))
        )
    }

    /**
     * アイテムのモデルJSONを生成します。
     * @param item アイテムのIDの提供元
     * @param layer モデルのテクスチャのパス
     * @param template 使用するモデルのテンプレート
     * @return モデルのパス
     */
    fun ItemModelGenerators.createFlatItemModel(
        item: HTSimpleValueWithKey<*>,
        layer: Identifier = item.idOrThrow.itemId,
        template: ModelTemplate = ModelTemplates.FLAT_ITEM
    ): Identifier = template.createItem(item, TextureMapping.layer0(Material(layer)), this.modelOutput)

    /**
     * 液体入りバケツのアイテムJSONを登録します。
     * @param content 液体を保持するインスタンス
     * @param isDrip `true`の場合，溶岩バケツのようなテクスチャを割り当てる
     */
    fun ItemModelGenerators.generateBucketItem(content: HTFluidContent, isDrip: Boolean) {
        fun material(namespace: String, path: String): Optional<Material> =
            Optional.of(namespace.toId(HTConstants.ITEM, path).let(::Material))

        val suffix: String = when (isDrip) {
            true -> "_drip"
            false -> ""
        }

        this.itemModelOutput.accept(
            content.bucketHolder.get(),
            DynamicFluidContainerModel.Unbaked(
                DynamicFluidContainerModel.Textures(
                    material(HTConstants.MINECRAFT, "bucket"),
                    material(HTConstants.MINECRAFT, "bucket"),
                    material(HTConstants.NEOFORGE, "mask/bucket_fluid$suffix"),
                    Optional.empty() // material(HTConstants.NEOFORGE, "mask/bucket_fluid_cover$suffix"),
                ),
                content.getOrThrow(),
                content.getFluidType().isLighterThanAir,
                true,
                false
            )
        )
    }
}
