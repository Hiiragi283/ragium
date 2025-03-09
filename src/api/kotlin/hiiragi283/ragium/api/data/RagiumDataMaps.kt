package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * Ragiumが追加する[DataMapType]
 */
object RagiumDataMaps {
    /**
     * ナパームダイナマイトでのブロック置き換えで参照します。
     */
    @JvmField
    val NAPALM: DataMapType<Block, HTNapalm> = DataMapType
        .builder(
            RagiumAPI.id("napalm"),
            Registries.BLOCK,
            HTNapalm.CODEC,
        ).synced(HTNapalm.CODEC, false)
        .build()

    /**
     * 枯葉剤ダイナマイトでのブロック置き換えで参照します。
     */
    @JvmField
    val DEFOLIANT: DataMapType<Block, HTDefoliant> = DataMapType
        .builder(
            RagiumAPI.id("defoliant"),
            Registries.BLOCK,
            HTDefoliant.CODEC,
        ).synced(HTDefoliant.CODEC, false)
        .build()

    /**
     * ハンマーによる採掘でのドロップを登録します。
     */
    @JvmField
    val HAMMER_DROP: DataMapType<Block, HTHammerDrop> = DataMapType
        .builder(
            RagiumAPI.id("hammer_drop"),
            Registries.BLOCK,
            HTHammerDrop.CODEC,
        ).synced(HTHammerDrop.CODEC, false)
        .build()

    /**
     * 石鹸でブロックを洗った後のブロックを登録します。
     */
    @JvmField
    val SOAP: DataMapType<Block, HTSoap> = DataMapType
        .builder(
            RagiumAPI.id("soap"),
            Registries.BLOCK,
            HTSoap.CODEC,
        ).synced(HTSoap.CODEC, false)
        .build()
}
