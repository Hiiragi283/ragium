package hiiragi283.ragium.api

import hiiragi283.ragium.api.extension.collectEntrypoints
import hiiragi283.ragium.api.extension.isClientEnv
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Consumer

@Suppress("DEPRECATION")
interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        @JvmStatic
        fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(MOD_NAME)

        @JvmStatic
        inline fun log(action: Logger.() -> Unit) {
            LOGGER.action()
        }

        @JvmStatic
        fun getInstance(): RagiumAPI = InternalRagiumAPI

        @JvmStatic
        val plugins: List<RagiumPlugin> by lazy {
            LOGGER.info("=== Loaded Ragium Plugins ===")
            buildList {
                addAll(collectEntrypoints<RagiumPlugin>(RagiumPlugin.SERVER_KEY))
                if (isClientEnv()) {
                    addAll(collectEntrypoints<RagiumPlugin>(RagiumPlugin.CLIENT_KEY))
                }
            }.sortedWith(compareBy(RagiumPlugin::priority).thenBy { it::class.java.canonicalName })
                .filter(RagiumPlugin::shouldLoad)
                .onEach { plugin: RagiumPlugin ->
                    LOGGER.info("- Priority : ${plugin.priority} ... ${plugin.javaClass.canonicalName}")
                }.apply { LOGGER.info("=============================") }
        }

        @JvmStatic
        fun forEachPlugins(action: Consumer<RagiumPlugin>) {
            plugins.forEach(action)
        }

        @JvmName("forEachPluginsKt")
        @JvmStatic
        inline fun forEachPlugins(action: (RagiumPlugin) -> Unit) {
            plugins.forEach(action)
        }
    }

    val config: Config

    val machineRegistry: HTMachineRegistry
    val materialRegistry: HTMaterialRegistry

    fun createInteractMachineCriterion(
        key: HTMachineKey,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTInteractMachineCriterion.Condition>

    fun createFluidDrinkCriterion(vararg fluids: Fluid): AdvancementCriterion<HTDrankFluidCriterion.Condition> =
        createFluidDrinkCriterion(RegistryEntryList.of(Fluid::getRegistryEntry, *fluids))

    fun createFluidDrinkCriterion(tagKey: TagKey<Fluid>): AdvancementCriterion<HTDrankFluidCriterion.Condition> =
        createFluidDrinkCriterion(Registries.FLUID.getOrCreateEntryList(tagKey))

    fun createFluidDrinkCriterion(entryList: RegistryEntryList<Fluid>): AdvancementCriterion<HTDrankFluidCriterion.Condition>

    fun createFilledCube(fluid: Fluid, count: Int = 1): ItemStack

    fun createHardModeCondition(value: Boolean): ResourceCondition

    //    Config    //
    @ApiStatus.NonExtendable
    interface Config {
        val autoIlluminatorRadius: Int

        val isHardMode: Boolean
    }
}
