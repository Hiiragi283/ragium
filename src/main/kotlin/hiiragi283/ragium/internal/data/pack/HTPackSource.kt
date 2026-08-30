package hiiragi283.ragium.internal.data.pack

import hiiragi283.lib.text.toText
import java.util.Optional
import java.util.function.Consumer
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackSelectionConfig
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.server.packs.repository.RepositorySource

/**
 * 参照 : [GregTech Modern - GTPackSource](https://github.com/GregTechCEu/GregTech-Modern/blob/1.21/src/main/java/com/gregtechceu/gtceu/data/pack/GTPackSource.java)
 *
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
@JvmRecord
data class HTPackSource(val name: String, val packType: PackType, val position: Pack.Position, val resources: (PackLocationInfo) -> PackResources) : RepositorySource {
    override fun loadPacks(onLoad: Consumer<Pack>) {
        Pack.readMetaAndCreate(
            PackLocationInfo(name, name.toText(), PackSource.BUILT_IN, Optional.empty()),
            object : Pack.ResourcesSupplier {
                override fun openPrimary(location: PackLocationInfo): PackResources = resources(location)

                override fun openFull(location: PackLocationInfo, metadata: Pack.Metadata): PackResources = openPrimary(location)
            },
            packType,
            PackSelectionConfig(true, position, false),
        )?.let(onLoad::accept)
    }
}
