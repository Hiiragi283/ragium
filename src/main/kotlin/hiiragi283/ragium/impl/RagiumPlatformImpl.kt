package hiiragi283.ragium.impl

import com.google.gson.JsonObject
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.util.HTAddonHelper
import hiiragi283.ragium.impl.material.RagiumMaterialManager
import hiiragi283.ragium.impl.recipe.manager.HTSimpleRecipeCache
import hiiragi283.ragium.impl.recipe.manager.HTSimpleRecipeType
import hiiragi283.ragium.impl.value.HTJsonValueInput
import hiiragi283.ragium.impl.value.HTJsonValueOutput
import hiiragi283.ragium.impl.value.HTTagValueInput
import hiiragi283.ragium.impl.value.HTTagValueOutput
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.fml.ModList
import net.neoforged.neoforge.server.ServerLifecycleHooks

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

    private lateinit var mapCache: Map<HTMaterialKey, HTMaterialPrefix>

    override fun getMaterialMap(): Map<HTMaterialKey, HTMaterialPrefix> {
        if (!::mapCache.isInitialized) {
            mapCache = buildMap {
                val consumer: (HTMaterialKey, HTMaterialPrefix) -> Unit =
                    { key: HTMaterialKey, variant: HTMaterialPrefix ->
                        check(put(key, variant) == null) { "Duplicate base variant for ${key.name}" }
                    }

                setupMaterials(consumer)

                for (addon: RagiumAddon in getAddons()) {
                    addon.registerMaterial(consumer)
                }
            }
        }
        return mapCache
    }

    private fun setupMaterials(consumer: (HTMaterialKey, HTMaterialPrefix) -> Unit) {
        // Vanilla
        consumer(VanillaMaterialKeys.COPPER, CommonMaterialPrefixes.INGOT)
        consumer(VanillaMaterialKeys.IRON, CommonMaterialPrefixes.INGOT)
        consumer(VanillaMaterialKeys.GOLD, CommonMaterialPrefixes.INGOT)
        consumer(VanillaMaterialKeys.NETHERITE, CommonMaterialPrefixes.INGOT)

        consumer(VanillaMaterialKeys.LAPIS, CommonMaterialPrefixes.GEM)
        consumer(VanillaMaterialKeys.QUARTZ, CommonMaterialPrefixes.GEM)
        consumer(VanillaMaterialKeys.AMETHYST, CommonMaterialPrefixes.GEM)
        consumer(VanillaMaterialKeys.DIAMOND, CommonMaterialPrefixes.GEM)
        consumer(VanillaMaterialKeys.EMERALD, CommonMaterialPrefixes.GEM)
        consumer(VanillaMaterialKeys.ECHO, CommonMaterialPrefixes.GEM)

        consumer(VanillaMaterialKeys.COAL, CommonMaterialPrefixes.FUEL)
        consumer(VanillaMaterialKeys.CHARCOAL, CommonMaterialPrefixes.FUEL)
        consumer(VanillaMaterialKeys.REDSTONE, CommonMaterialPrefixes.DUST)
        consumer(VanillaMaterialKeys.OBSIDIAN, CommonMaterialPrefixes.DUST)
        consumer(VanillaMaterialKeys.WOOD, CommonMaterialPrefixes.DUST)
        // Ragium
        consumer(RagiumMaterialKeys.RAGINITE, CommonMaterialPrefixes.DUST)
        consumer(RagiumMaterialKeys.CINNABAR, CommonMaterialPrefixes.GEM)
        consumer(RagiumMaterialKeys.SALTPETER, CommonMaterialPrefixes.DUST)
        consumer(RagiumMaterialKeys.SULFUR, CommonMaterialPrefixes.DUST)

        consumer(RagiumMaterialKeys.RAGI_CRYSTAL, CommonMaterialPrefixes.GEM)
        consumer(RagiumMaterialKeys.AZURE, CommonMaterialPrefixes.GEM)
        consumer(RagiumMaterialKeys.CRIMSON_CRYSTAL, CommonMaterialPrefixes.GEM)
        consumer(RagiumMaterialKeys.WARPED_CRYSTAL, CommonMaterialPrefixes.GEM)
        consumer(RagiumMaterialKeys.ELDRITCH_PEARL, CommonMaterialPrefixes.GEM)

        consumer(RagiumMaterialKeys.RAGI_ALLOY, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.AZURE_STEEL, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.DEEP_STEEL, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.GILDIUM, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.IRIDESCENTIUM, CommonMaterialPrefixes.INGOT)

        consumer(RagiumMaterialKeys.CHOCOLATE, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.MEAT, CommonMaterialPrefixes.INGOT)
        consumer(RagiumMaterialKeys.COOKED_MEAT, CommonMaterialPrefixes.INGOT)

        consumer(RagiumMaterialKeys.COAL_COKE, CommonMaterialPrefixes.FUEL)
        consumer(RagiumMaterialKeys.PLASTIC, CommonMaterialPrefixes.PLATE)
        // Common
        for (key: HTMaterialKey in CommonMaterialKeys.METALS.values) {
            consumer(key, CommonMaterialPrefixes.INGOT)
        }
        for (key: HTMaterialKey in CommonMaterialKeys.ALLOYS.values) {
            consumer(key, CommonMaterialPrefixes.INGOT)
        }
        for (key: HTMaterialKey in CommonMaterialKeys.GEMS.values) {
            consumer(key, CommonMaterialPrefixes.GEM)
        }
    }

    //    Item    //

    override fun createSoda(potion: PotionContents, count: Int): ItemStack =
        createItemStack(RagiumItems.ICE_CREAM_SODA, DataComponents.POTION_CONTENTS, potion, count)

    //    Material    //

    override fun getMaterialDefinitions(): Map<HTMaterialKey, HTMaterialDefinition> = RagiumMaterialManager.definitions

    //    Recipe    //

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> createCache(
        finder: HTRecipeFinder<INPUT, RECIPE>,
    ): HTRecipeCache<INPUT, RECIPE> = HTSimpleRecipeCache(finder)

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> wrapRecipeType(
        recipeType: RecipeType<RECIPE>,
    ): HTRecipeType.Findable<INPUT, RECIPE> = HTSimpleRecipeType(recipeType)

    //    Server    //

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler =
        server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_BUNDLE).getHandler(color)

    override fun getEnergyNetwork(level: Level?): HTEnergyBattery? = when (level) {
        is ServerLevel -> level
        else -> level?.dimension()?.let(::getLevel)
    }?.getData(RagiumAttachmentTypes.ENERGY_NETWORK)

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
