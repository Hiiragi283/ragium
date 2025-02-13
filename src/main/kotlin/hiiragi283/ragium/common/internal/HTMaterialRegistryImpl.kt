package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTRegisterMaterialEvent
import hiiragi283.ragium.api.extension.computeIfAbsent
import hiiragi283.ragium.api.extension.constFunction3
import hiiragi283.ragium.api.extension.createHolderSorter
import hiiragi283.ragium.api.extension.mutableTableOf
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModLoader
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object HTMaterialRegistryImpl : HTMaterialRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Init    //

    private lateinit var typeMap: Map<HTMaterialKey, HTMaterialType>

    fun initRegistry() {
        registerMaterials()
        // modifyProperties()
        LOGGER.info("Loaded material registry!")
    }

    private fun registerMaterials() {
        LOGGER.info("Invoke material events...")
        val typeCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()

        ModLoader.postEvent(
            HTRegisterMaterialEvent { key: HTMaterialKey, type: HTMaterialType ->
                check(typeCache.put(key, type) == null) { "Duplicated material registration: ${key.name}" }
            },
        )

        this.typeMap = typeCache
        LOGGER.info("Registered new materials!")
    }

    /*private fun modifyProperties() {
        val propertyCache: MutableMap<HTMaterialKey, HTPropertyHolderBuilder> = mutableMapOf()
        ModLoader.postEvent(
            HTModifyPropertyEvent.Material {
                propertyCache.computeIfAbsent(
                    it,
                    constFunction2(HTPropertyHolderBuilder()),
                )
            },
        )
        this.propertyMap = propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() }
        LOGGER.info("Modified material properties!")
    }*/

    //    HTMaterialRegistry    //

    private var tagItemCache: HTTable.Mutable<HTTagPrefix, HTMaterialKey, MutableList<Holder<Item>>> = mutableTableOf()

    @SubscribeEvent
    fun onTagsUpdated(event: TagsUpdatedEvent) {
        if (event.updateCause != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) return
        val itemLookup: HolderLookup.RegistryLookup<Item> = event.registryAccess.lookupOrThrow(Registries.ITEM)
        // Reload material items
        tagItemCache.clear()
        for (material: HTMaterialKey in keys) {
            for (prefix: HTTagPrefix in HTTagPrefix.entries) {
                itemLookup.get(prefix.createTag(material)).ifPresent { holderSet: HolderSet.Named<Item> ->
                    for (holder: Holder<Item> in holderSet) {
                        tagItemCache
                            .computeIfAbsent(prefix, material, constFunction3(mutableListOf()))
                            .add(holder)
                    }
                }
            }
        }
        tagItemCache.values.forEach { it.sortWith(createHolderSorter()) }

        LOGGER.info("Reloaded material items!")
    }

    override val keys: Set<HTMaterialKey> get() = typeMap.keys

    override fun getType(key: HTMaterialKey): HTMaterialType = typeMap[key] ?: error("Unknown material key: $key")

    override fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): List<Holder<Item>> = tagItemCache.get(prefix, key) ?: listOf()
}
