package hiiragi283.ragium.impl

import com.google.gson.JsonObject
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.util.HTAddonHelper
import hiiragi283.ragium.impl.util.RandomSourceWrapper
import hiiragi283.ragium.impl.value.HTJsonValueInput
import hiiragi283.ragium.impl.value.HTJsonValueOutput
import hiiragi283.ragium.impl.value.HTTagValueInput
import hiiragi283.ragium.impl.value.HTTagValueOutput
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.neoforged.fml.ModList
import net.neoforged.neoforge.server.ServerLifecycleHooks
import kotlin.random.Random

class RagiumPlatformImpl : RagiumPlatform {
    //    Addon    //

    private lateinit var addonCache: List<RagiumAddon>

    override fun getAddons(): List<RagiumAddon> {
        if (!::addonCache.isInitialized) {
            RagiumAPI.LOGGER.info("Collecting addons for Ragium...")
            addonCache = HTAddonHelper
                .collectInstances<RagiumAddon.Provider>()
                .flatMap { it.getAddons(ModList.get()) }
                .sortedBy(RagiumAddon::priority)
                .onEach { addon: RagiumAddon ->
                    RagiumAPI.LOGGER.info("Loaded addon from ${addon::class.qualifiedName}!")
                }
        }
        return addonCache
    }

    private lateinit var mapCache: Map<HTMaterialType, HTMaterialVariant.ItemTag>

    override fun getMaterialMap(): Map<HTMaterialType, HTMaterialVariant.ItemTag> {
        if (!::mapCache.isInitialized) {
            mapCache = buildMap {
                val consumer: (HTMaterialType, HTMaterialVariant.ItemTag) -> Unit =
                    { type: HTMaterialType, variant: HTMaterialVariant.ItemTag ->
                        check(put(type, variant) == null) { "Duplicate base variant for ${type.materialName()}" }
                    }

                setupMaterials(consumer)

                for (addon: RagiumAddon in getAddons()) {
                    addon.registerMaterial(consumer)
                }
            }
        }
        return mapCache
    }

    private fun setupMaterials(consumer: (HTMaterialType, HTMaterialVariant.ItemTag) -> Unit) {
        // Vanilla
        consumer(HTVanillaMaterialType.COPPER, HTItemMaterialVariant.INGOT)
        consumer(HTVanillaMaterialType.IRON, HTItemMaterialVariant.INGOT)
        consumer(HTVanillaMaterialType.GOLD, HTItemMaterialVariant.INGOT)
        consumer(HTVanillaMaterialType.NETHERITE, HTItemMaterialVariant.INGOT)

        consumer(HTVanillaMaterialType.LAPIS, HTItemMaterialVariant.GEM)
        consumer(HTVanillaMaterialType.QUARTZ, HTItemMaterialVariant.GEM)
        consumer(HTVanillaMaterialType.AMETHYST, HTItemMaterialVariant.GEM)
        consumer(HTVanillaMaterialType.DIAMOND, HTItemMaterialVariant.GEM)
        consumer(HTVanillaMaterialType.EMERALD, HTItemMaterialVariant.GEM)
        consumer(HTVanillaMaterialType.ECHO, HTItemMaterialVariant.GEM)

        consumer(HTVanillaMaterialType.COAL, HTItemMaterialVariant.FUEL)
        consumer(HTVanillaMaterialType.CHARCOAL, HTItemMaterialVariant.FUEL)
        consumer(HTVanillaMaterialType.REDSTONE, HTItemMaterialVariant.DUST)
        consumer(HTVanillaMaterialType.OBSIDIAN, HTItemMaterialVariant.DUST)
        consumer(HTVanillaMaterialType.WOOD, HTItemMaterialVariant.DUST)
        // Ragium
        consumer(RagiumMaterialType.RAGINITE, HTItemMaterialVariant.DUST)
        consumer(RagiumMaterialType.CINNABAR, HTItemMaterialVariant.GEM)
        consumer(RagiumMaterialType.SALTPETER, HTItemMaterialVariant.DUST)
        consumer(RagiumMaterialType.SULFUR, HTItemMaterialVariant.DUST)

        consumer(RagiumMaterialType.RAGI_CRYSTAL, HTItemMaterialVariant.GEM)
        consumer(RagiumMaterialType.AZURE, HTItemMaterialVariant.GEM)
        consumer(RagiumMaterialType.CRIMSON_CRYSTAL, HTItemMaterialVariant.GEM)
        consumer(RagiumMaterialType.WARPED_CRYSTAL, HTItemMaterialVariant.GEM)
        consumer(RagiumMaterialType.ELDRITCH_PEARL, HTItemMaterialVariant.GEM)

        consumer(RagiumMaterialType.RAGI_ALLOY, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.ADVANCED_RAGI_ALLOY, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.AZURE_STEEL, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.DEEP_STEEL, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.GILDIUM, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.IRIDESCENTIUM, HTItemMaterialVariant.INGOT)

        consumer(RagiumMaterialType.CHOCOLATE, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.MEAT, HTItemMaterialVariant.INGOT)
        consumer(RagiumMaterialType.COOKED_MEAT, HTItemMaterialVariant.INGOT)

        consumer(RagiumMaterialType.COAL_COKE, HTItemMaterialVariant.FUEL)
        consumer(RagiumMaterialType.PLASTIC, HTItemMaterialVariant.PLATE)
    }

    //    Collection    //

    override fun wrapRandom(random: RandomSource): Random = RandomSourceWrapper(random)

    //    Item    //

    override fun createSoda(potion: PotionContents, count: Int): ItemStack =
        createItemStack(RagiumItems.ICE_CREAM_SODA, DataComponents.POTION_CONTENTS, potion, count)

    //    Server    //

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler =
        server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_BUNDLE).getHandler(color)

    override fun getEnergyNetwork(level: Level?): HTEnergyBattery? = when (level) {
        is ServerLevel -> level.getData(RagiumAttachmentTypes.ENERGY_NETWORK)
        else -> level?.dimension()?.let(::getEnergyNetwork)
    }

    override fun getEnergyNetwork(key: ResourceKey<Level>): HTEnergyBattery? = RagiumPlatform.INSTANCE
        .getLevel(key)
        ?.getData(RagiumAttachmentTypes.ENERGY_NETWORK)

    //    Storage    //

    override fun createValueInput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput =
        HTJsonValueInput.create(lookup, jsonObject)

    override fun createValueOutput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput =
        HTJsonValueOutput(lookup, jsonObject)

    override fun createValueInput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput =
        HTTagValueInput.create(lookup, compoundTag)

    override fun createValueOutput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput =
        HTTagValueOutput(lookup, compoundTag)
}
