package hiiragi283.lib.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import hiiragi283.lib.resource.toId
import hiiragi283.lib.resource.vanillaId
import java.util.Optional
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
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel

/**
 * Hiiragi Seriesで使用される[ModelProvider]の拡張クラスです。。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTModelProvider(output: PackOutput, modId: String) : ModelProvider(output, modId) {
    abstract override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators)

    //    Block    //

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
     * ブロックJSONを生成します。
     * @param block ブロックの提供元
     * @param modelId 使用するモデルのID
     */
    fun BlockModelGenerators.createAltModel(block: SupplierWithId<Block>, modelId: Identifier = block.blockId) {
        this.createSimple(block.get(), modelId)
    }

    /**
     * ハーフブロックのブロックJSONを生成します。
     * @since 26.1.3
     */
    fun BlockModelGenerators.createSlab(block: SupplierWithId<SlabBlock>, fullModel: Identifier, texture: Material = Material(block.blockId)) {
        this.createSlab(block, fullModel, texture, texture, texture)
    }

    /**
     * ハーフブロックのブロックJSONを生成します。
     * @since 26.1.3
     */
    fun BlockModelGenerators.createSlab(block: SupplierWithId<SlabBlock>, fullModel: Identifier, top: Material, side: Material, bottom: Material) {
        val slab: SlabBlock = block.get()
        val mapping: TextureMapping = TextureMapping().put(TextureSlot.TOP, top).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.SIDE, side)
        val modelId: Identifier = ModelTemplates.SLAB_BOTTOM.createBlock(block, mapping, modelOutput)

        blockStateOutput.accept(
            BlockModelGenerators.createSlab(
                slab,
                BlockModelGenerators.plainVariant(modelId),
                BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.createBlock(block, mapping, modelOutput)),
                BlockModelGenerators.plainVariant(fullModel),
            ),
        )
        registerSimpleItemModel(slab, modelId)
    }

    /**
     * 階段ブロックのブロックJSONを生成します。
     * @since 26.1.3
     */
    fun BlockModelGenerators.createStairs(block: SupplierWithId<StairBlock>, texture: Material = Material(block.blockId)) {
        this.createStairs(block, texture, texture, texture)
    }

    /**
     * 階段ブロックのブロックJSONを生成します。
     * @since 26.1.3
     */
    fun BlockModelGenerators.createStairs(block: SupplierWithId<StairBlock>, top: Material, side: Material, bottom: Material) {
        val stairs: StairBlock = block.get()
        val mapping: TextureMapping = TextureMapping().put(TextureSlot.TOP, top).put(TextureSlot.BOTTOM, bottom).put(TextureSlot.SIDE, side)
        val modelId: Identifier = ModelTemplates.STAIRS_STRAIGHT.createBlock(block, mapping, modelOutput)

        blockStateOutput.accept(
            BlockModelGenerators.createStairs(
                stairs,
                BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_INNER.createBlock(block, mapping, modelOutput)),
                BlockModelGenerators.plainVariant(modelId),
                BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_OUTER.createBlock(block, mapping, modelOutput)),
            ),
        )
        registerSimpleItemModel(stairs, modelId)
    }

    /**
     * 液体ブロックのブロックJSONを生成します。
     * @param fluidBlock 液体ブロックの提供元
     */
    fun BlockModelGenerators.createFluid(fluidBlock: SupplierWithId<Block>) {
        this.createAltModel(
            fluidBlock,
            HTModelTemplates.FLUID_BLOCK.createBlock(
                fluidBlock,
                TextureMapping.particle(Material(vanillaId(HTConstants.BLOCK, "water_still"))),
                this.modelOutput,
            ),
        )
    }

    //    Item    //

    /**
     * アイテムJSONを生成します。
     * @param item アイテムの提供元
     * @param layer モデルのテクスチャのパス
     * @param template 使用するモデルのテンプレート
     */
    fun ItemModelGenerators.generateFlatItem(item: SupplierWithId<Item>, layer: Identifier = item.itemId, template: ModelTemplate = ModelTemplates.FLAT_ITEM) {
        this.itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(this.createFlatItemModel(item, layer, template)))
    }

    /**
     * アイテムJSONを生成します。
     * @param item アイテムのIDの提供元
     * @param layers モデルのテクスチャのパス
     * @throws IllegalStateException [layers]のサイズが`0`または`4`以上の場合
     */
    fun ItemModelGenerators.generateLayeredItem(item: SupplierWithId<Item>, vararg layers: Identifier) {
        val (mapping: TextureMapping, template: ModelTemplate) = when (layers.size) {
            1 -> TextureMapping.layer0(Material(layers[0])) to ModelTemplates.FLAT_ITEM
            2 -> TextureMapping.layered(Material(layers[0]), Material(layers[1])) to ModelTemplates.TWO_LAYERED_ITEM
            3 -> TextureMapping.layered(Material(layers[0]), Material(layers[1]), Material(layers[2])) to ModelTemplates.THREE_LAYERED_ITEM
            else -> error("Cannot create item model with ${layers.size} layers")
        }
        this.itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(template.createItem(item, mapping, this.modelOutput)))
    }

    /**
     * アイテムのモデルJSONを生成します。
     * @param item アイテムのIDの提供元
     * @param layer モデルのテクスチャのパス
     * @param template 使用するモデルのテンプレート
     * @return モデルのパス
     */
    fun ItemModelGenerators.createFlatItemModel(
        item: HTIdLike,
        layer: Identifier = item.itemId,
        template: ModelTemplate = ModelTemplates.FLAT_ITEM,
    ): Identifier = template.createItem(item, TextureMapping.layer0(Material(layer)), this.modelOutput)

    /**
     * 液体入りバケツのアイテムJSONを登録します。
     * @param content 液体を保持するインスタンス
     * @param isDrip `true`の場合，溶岩バケツのようなテクスチャを割り当てる
     */
    fun ItemModelGenerators.generateBucketItem(content: HTFluidContent, isDrip: Boolean) {
        fun material(namespace: String, path: String): Optional<Material> = Optional.of(namespace.toId(HTConstants.ITEM, path).let(::Material))

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
                    Optional.empty(), // material(HTConstants.NEOFORGE, "mask/bucket_fluid_cover$suffix"),
                ),
                content.get(),
                content.getFluidType().isLighterThanAir,
                true,
                false,
            ),
        )
    }
}
