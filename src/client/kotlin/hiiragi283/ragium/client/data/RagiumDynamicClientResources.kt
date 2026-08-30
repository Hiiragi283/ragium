package hiiragi283.ragium.client.data

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.lib.data.model.HTModelTemplates
import hiiragi283.lib.data.model.HTTexturedModelProvider
import hiiragi283.lib.data.pack.HTDynamicResourceRegister
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.columnPart
import hiiragi283.lib.material.forEachPart
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.property.HTPartPropertyKeys
import hiiragi283.lib.material.property.HTMaterialPropertyKeys
import hiiragi283.lib.property.getOrDefault
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import kotlin.system.measureTimeMillis
import net.minecraft.client.data.models.model.DelegatedModel
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

data object RagiumDynamicClientResources {
    @JvmStatic
    fun initialize() {
        HTDynamicResourceRegister.LOGGER.info("HiiragiCore Assets loading took {} ms", measureTimeMillis(::initializeInternal))
    }

    @JvmStatic
    private fun initializeInternal() {
        // Lang
        HTDynamicResourceRegister.addLang(HTLangTypes.EN_US, ::addTranslations)
        HTDynamicResourceRegister.addLang(HTLangTypes.JA_JP, ::addTranslations)
        // Model
        initializeModels()
    }

    //    Translation    //

    @JvmStatic
    private fun addTranslations(langType: HTLangType, consumer: (String, String) -> Unit) {
        val registered: HTMaterialContents.Provider = HTMaterialAccess.INSTANCE.getRegisteredContents()

        for (material: HTMaterial in HTMaterial.getManager()) {
            val key: HTMaterialKey = material.key
            // Material Name
            val materialName: HTLangName = material[HTMaterialPropertyKeys.LANG_NAME] ?: continue
            consumer(key.translationKey, materialName.getTranslatedName(langType))
            // Block
            for ((part: HTPart, block: HTMaterialContents.BlockEntry) in registered.blocks.columnPart(key)) {
                val name: String = translate(langType, part, material) ?: continue
                consumer(block.translationKey, name)
            }
            // Item
            for ((part: HTPart, item: HTMaterialContents.ItemEntry) in registered.items.columnPart(key)) {
                val name: String = translate(langType, part, material) ?: continue
                consumer(item.translationKey, name)
            }
        }
    }

    @JvmStatic
    private fun translate(type: HTLangType, part: HTPart, material: HTMaterial): String? = material.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[part.key]
        ?.getTranslatedName(type)
        ?: run {
            val materialName: HTLangName = material[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            part.getOrDefault(HTPartPropertyKeys.LANG_PATTERN).translate(type, materialName)
        }

    //    Model    //

    @JvmStatic
    private fun initializeModels() {
        val registered: HTMaterialContents.Provider = HTMaterialAccess.INSTANCE.getRegisteredContents()
        // Block
        registered.blocks.forEachPart { part: HTPart, material: HTMaterial, block: HTMaterialContents.BlockEntry ->
            if (HTPartPropertyKeys.IS_ORE in part) {
                val stoneTexture: Identifier = part[HTPartPropertyKeys.ORE_STONE_TEX] ?: return@forEachPart
                HTDynamicResourceRegister.BLOCK_MODEL_GENERATOR.createTrivialBlock(
                    block.get(),
                    TexturedModel.createDefault(
                        { _ ->
                            TextureMapping()
                                .put(TextureSlot.LAYER0, Material(stoneTexture))
                                .put(TextureSlot.LAYER1, Material(HTPart.getManager().getOrThrow(CommonParts.ORE).createId(material.key).withPrefix("block/")))
                        },
                        HTModelTemplates.LAYERED,
                    ),
                )
            } else {
                HTDynamicResourceRegister.BLOCK_MODEL_GENERATOR.createTrivialCube(block.get())
            }
        }
        registered.blocks.values.forEach { block: HTMaterialContents.BlockEntry ->
            HTDynamicResourceRegister.MODEL_OUTPUT.accept(block.itemId, DelegatedModel(block.blockId))
        }
        // Item
        registered.items.forEachPart { _, _, item: HTIdLike ->
            HTDynamicResourceRegister.addModel(HTTexturedModelProvider.FLAT_ITEM, item)
        }
    }
}
