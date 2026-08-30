package hiiragi283.lib.data.pack

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.logging.LogUtils
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Encoder
import com.mojang.serialization.JsonOps
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.data.model.HTTexturedModelProvider
import hiiragi283.lib.data.model.ModelOutput
import hiiragi283.lib.registry.asSupplier
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import hiiragi283.ragium.api.RagiumAPI
import kotlin.jvm.optionals.getOrDefault
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelOutput
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher
import net.minecraft.client.renderer.item.ClientItem
import net.minecraft.client.renderer.item.ItemModel
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import org.slf4j.Logger

data object HTDynamicResourceRegister {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    fun addToData(id: Identifier, json: JsonElement) {
        HTDynamicResourcePack.addToData(id.withSuffix(".json"), json.toString().toByteArray())
    }

    /**
     * [Encoder]に基づいてデータを追加します。
     * @param id 追加するデータのパス
     * @param codec データの変換に使用するコーデック
     * @param value 追加するデータ
     */
    @JvmStatic
    fun <T : Any> addToData(id: Identifier, codec: Encoder<T>, value: T, ops: DynamicOps<JsonElement> = JsonOps.INSTANCE) {
        codec.encodeStart(ops, value)
            .ifError { LOGGER.error(it.message()) }
            .ifSuccess { addToData(id, it) }
    }

    // Language
    @JvmStatic
    inline fun addLang(langType: HTLangType, consumer: (HTLangType, (String, String) -> Unit) -> Unit) {
        val map: MutableMap<String, String> = sortedMapOf()
        consumer(langType, map::put)
        val root = JsonObject()
        map.forEach(root::addProperty)
        addToData(RagiumAPI.id("lang", langType.name), root)
    }

    // Model
    @JvmField
    val MODEL_OUTPUT: ModelOutput = { modelId: Identifier, instance: ModelInstance -> addToData(modelId.withPrefix("models/"), instance.get()) }

    @JvmField
    val BLOCK_MODEL_GENERATOR = BlockModelGenerators(
        { generator: BlockModelDefinitionGenerator ->
            addToData(
                generator.block().asSupplier().getId().withPrefix("blockstates/"),
                BlockStateModelDispatcher.CODEC,
                generator.create(),
            )
        },
        object : ItemModelOutput {
            override fun accept(item: Item, generator: ItemModel.Unbaked, properties: ClientItem.Properties) {
                this.register(item, ClientItem(generator, properties))
            }

            override fun copy(donor: Item, acceptor: Item): Unit = Unit // TODO

            override fun register(item: Item, clientItem: ClientItem) {
                addToData(item.asSupplier().getId().withPrefix("items/"), ClientItem.CODEC, clientItem)
            }

            override fun register(identifier: Identifier, clientItem: ClientItem) {
                addToData(identifier, ClientItem.CODEC, clientItem)
            }
        },
        MODEL_OUTPUT,
    )

    @JvmStatic
    fun addModel(provider: HTTexturedModelProvider, value: HTIdLike): Identifier = provider.create(value, MODEL_OUTPUT)

    @JvmStatic
    fun addBlockModel(template: ModelTemplate, block: HTIdLike, texture: TextureMapping): Identifier = template.create(
        block.blockId.withSuffix(template.suffix.getOrDefault("")),
        texture,
        MODEL_OUTPUT,
    )

    @JvmStatic
    fun addItemModel(template: ModelTemplate, item: HTIdLike, texture: TextureMapping): Identifier = template.create(
        item.itemId.withSuffix(template.suffix.getOrDefault("")),
        texture,
        MODEL_OUTPUT,
    )
}
