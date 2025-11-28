package hiiragi283.ragium.impl

import com.google.gson.JsonObject
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.material.prefix.HTRegisterPrefixEvent
import hiiragi283.ragium.api.recipe.HTMaterialRecipeManager
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.data.recipe.ingredient.HTFluidIngredientCreatorImpl
import hiiragi283.ragium.impl.data.recipe.ingredient.HTItemIngredientCreatorImpl
import hiiragi283.ragium.impl.material.RagiumMaterialManager
import hiiragi283.ragium.impl.material.RagiumMaterialRecipeManager
import hiiragi283.ragium.impl.value.HTJsonValueInput
import hiiragi283.ragium.impl.value.HTJsonValueOutput
import hiiragi283.ragium.impl.value.HTTagValueInput
import hiiragi283.ragium.impl.value.HTTagValueOutput
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.util.function.Consumer

class RagiumPlatformImpl : RagiumPlatform {
    override fun getUpgradeDataType(): DataComponentType<HTMachineUpgrade> = RagiumDataComponents.MACHINE_UPGRADE

    //    Material    //

    override fun getMaterialDefinitions(): Map<HTMaterialKey, HTMaterialDefinition> = RagiumMaterialManager.definitions

    private lateinit var prefixMap: Map<String, HTMaterialPrefix>

    override fun getPrefixMap(): Map<String, HTMaterialPrefix> {
        if (!::prefixMap.isInitialized) {
            prefixMap = buildMap {
                val consumer = Consumer { prefix: HTPrefixLike ->
                    val prefix1: HTMaterialPrefix = prefix.asMaterialPrefix()
                    check(this.put(prefix1.name, prefix1) == null) {
                        "Duplicate material prefix registration: ${prefix1.name}"
                    }
                }
                CommonMaterialPrefixes.entries.forEach(consumer)

                NeoForge.EVENT_BUS.post(HTRegisterPrefixEvent(this))
            }
        }
        return prefixMap
    }

    //    Recipe    //

    override fun getMaterialRecipeManager(): HTMaterialRecipeManager = RagiumMaterialRecipeManager

    override fun createItemCreator(getter: HolderGetter<Item>): HTItemIngredientCreator = HTItemIngredientCreatorImpl(getter)

    override fun createFluidCreator(getter: HolderGetter<Fluid>): HTFluidIngredientCreator = HTFluidIngredientCreatorImpl(getter)

    //    Server    //

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler =
        server.overworld().getData(RagiumAttachmentTypes.UNIVERSAL_BUNDLE).getHandler(color)

    override fun getEnergyNetwork(level: Level?): HTEnergyBattery? = when (level) {
        is ServerLevel -> level
        else -> level?.dimension()?.let(::getLevel)
    }?.getData(RagiumAttachmentTypes.ENERGY_NETWORK)

    //    Storage    //

    override fun createValueInput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput =
        HTJsonValueInput.create(provider, jsonObject)

    override fun createValueOutput(provider: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput =
        HTJsonValueOutput(provider, jsonObject)

    override fun createValueInput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput =
        HTTagValueInput.create(provider, compoundTag)

    override fun createValueOutput(provider: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput =
        HTTagValueOutput(provider, compoundTag)
}
