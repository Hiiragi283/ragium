package hiiragi283.ragium.internal.material

import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialManager
import hiiragi283.lib.material.part.HTPartManager

class HTMaterialAccessImpl : HTMaterialAccess {
    override fun getPartManager(): HTPartManager = HTMaterialContentsRegister.partManager

    override fun getMaterialManager(): HTMaterialManager = HTMaterialContentsRegister.materialManager

    override fun getExistingContents(): HTMaterialContents.Provider = HTMaterialContentsRegister.existingContents

    override fun getRegisteredContents(): HTMaterialContents.Provider = HTMaterialContentsRegister.registeredContents
}
