package hiiragi283.ragium.api.data.tank

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.data.PackOutput
import net.minecraft.server.packs.PackType
import net.neoforged.neoforge.common.data.JsonCodecProvider

/**
 * @author Hiiragi Tsubasa
 */
abstract class HTTankInteractionProvider(context: HTDataGenContext, modId: String) :
    JsonCodecProvider<HTTankInteraction.Serializable>(
        context.output,
        PackOutput.Target.DATA_PACK,
        RagiumConst.TANK_INTERACTION,
        PackType.SERVER_DATA,
        HTTankInteraction.Serializable.CODEC,
        context.registries,
        modId,
        context.fileHelper,
    )
