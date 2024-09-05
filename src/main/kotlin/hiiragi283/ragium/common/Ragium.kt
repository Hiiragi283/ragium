package hiiragi283.ragium.common

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Ragium : ModInitializer {

	const val MOD_ID = "ragium"
	const val MOD_NAME = "Ragium"

	@JvmStatic
	fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

	@JvmField
	val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

	override fun onInitialize() {
		logger.info("Hello Fabric world!")
	}
}