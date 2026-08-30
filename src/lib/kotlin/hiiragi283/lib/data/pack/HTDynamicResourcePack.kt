package hiiragi283.lib.data.pack

import hiiragi283.lib.HTConstants
import hiiragi283.lib.text.toText
import hiiragi283.ragium.api.RagiumAPI
import java.io.InputStream
import java.nio.file.Path
import net.minecraft.SharedConstants
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.MetadataSectionType
import net.minecraft.server.packs.metadata.pack.PackMetadataSection
import net.minecraft.server.packs.resources.IoSupplier
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLPaths

/**
 * Hiiragi Seriesで使用される動的リソースパックを管理するクラスです。
 *
 * 参照 : [GregTech Modern - GTDynamicResourcePack](https://github.com/GregTechCEu/GregTech-Modern/blob/1.21/src/main/java/com/gregtechceu/gtceu/data/pack/GTDynamicResourcePack.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
class HTDynamicResourcePack(private val locationInfo: PackLocationInfo) : PackResources {
    companion object {
        @JvmStatic
        private val DOMAINS: MutableSet<String> = HTConstants.getBuiltInIdSet(RagiumAPI.MOD_ID).toMutableSet()

        @JvmStatic
        private val CONTENTS = HTPackContents()

        @JvmStatic
        fun addDomain(domain: String) {
            DOMAINS += domain
        }

        @JvmStatic
        fun clear() {
            CONTENTS.clearData()
        }

        @JvmStatic
        fun addToData(id: Identifier, bytes: ByteArray) {
            if (!FMLEnvironment.isProduction()) {
                val parent: Path = FMLPaths.GAMEDIR.get().resolve("debug/dumped/assets")
                HTPackContents.dumpData(id, parent, bytes)
            }
            CONTENTS.addToData(id, bytes)
        }

        @JvmStatic
        fun hasResource(location: Identifier): Boolean = CONTENTS.getResource(location) != null
    }

    //    PackResources    //

    override fun getRootResource(vararg elements: String): IoSupplier<InputStream>? = when {
        elements.firstOrNull() == "pack.png" -> IoSupplier { RagiumAPI::class.java.getResourceAsStream("/icon.png")!! }
        else -> null
    }

    override fun getResource(packType: PackType, location: Identifier): IoSupplier<InputStream>? = when (packType) {
        PackType.CLIENT_RESOURCES -> CONTENTS.getResource(location)
        PackType.SERVER_DATA -> null
    }

    override fun listResources(packType: PackType, namespace: String, path: String, resourceOutput: PackResources.ResourceOutput) {
        if (packType == PackType.CLIENT_RESOURCES) {
            CONTENTS.listResources(namespace, path, resourceOutput)
        }
    }

    override fun getNamespaces(type: PackType): Set<String> = when (type) {
        PackType.CLIENT_RESOURCES -> DOMAINS
        PackType.SERVER_DATA -> setOf()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getMetadataSection(metadataSerializer: MetadataSectionType<T>): T? = when (metadataSerializer) {
        PackMetadataSection.CLIENT_TYPE -> PackMetadataSection(
            "Hiiragi Core dynamic assets".toText(),
            SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES).minorRange(),
        ) as T
        else -> null
    }

    override fun location(): PackLocationInfo = locationInfo

    override fun close(): Unit = Unit
}
