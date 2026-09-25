package hiiragi283.lib.registry

import net.minecraft.resources.Identifier

/**
 * IDのエイリアスを登録できるレジストリに実装されるインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface RegistryAliasAcceptor {
    /**
     * IDのエイリアスを登録します。
     * @param from 変更前のIDの[パス][Identifier.getPath]
     * @param to 変更後のIDの[パス][Identifier.getPath]
     */
    fun addAlias(from: String, to: String)

    /**
     * IDのエイリアスを登録します。
     * @param from 変更前のIDの
     * @param to 変更後のIDの
     */
    fun addAlias(from: Identifier, to: Identifier)
}
