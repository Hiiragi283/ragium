package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.InternalRagiumAPI
import hiiragi283.ragium.common.advancement.HTBuiltMachineCriterion
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApiStatus.NonExtendable
interface RagiumAPI {
    companion object {
        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        @JvmStatic
        fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

        @JvmStatic
        inline fun log(action: Logger.() -> Unit) {
            logger.action()
        }

        @JvmStatic
        fun getInstance(): RagiumAPI = InternalRagiumAPI

        private lateinit var plugins: List<RagiumPlugin>

        @JvmStatic
        fun getPlugins(): List<RagiumPlugin> {
            if (!::plugins.isInitialized) {
                plugins = FabricLoader
                    .getInstance()
                    .getEntrypoints(
                        RagiumPlugin.KEY,
                        RagiumPlugin::class.java,
                    ).sortedWith(compareBy(RagiumPlugin::priority).thenBy { it::class.java.canonicalName })
                    .filter(RagiumPlugin::shouldLoad)
                log {
                    info("=== Loaded Ragium Plugins ===")
                    plugins.forEach { plugin: RagiumPlugin ->
                        info("- Priority : ${plugin.priority} ... ${plugin.javaClass.canonicalName}")
                    }
                    info("=============================")
                }
            }
            return plugins
        }
    }

    val config: Config
    val machineRegistry: HTMachineRegistry

    fun createBuiltMachineCriterion(type: HTMachine, minTier: HTMachineTier): AdvancementCriterion<HTBuiltMachineCriterion.Condition>

    fun createFilledCube(fluid: Fluid, count: Int = 1): ItemStack

    //    Config    //

    @ApiStatus.NonExtendable
    interface Config {
        val isHardMode: Boolean
    }
}
